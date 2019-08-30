package dev.daeyeon.gaasproject.data.source

import android.util.Log
import dev.daeyeon.gaasproject.data.StateResult
import dev.daeyeon.gaasproject.data.entity.Market
import dev.daeyeon.gaasproject.data.entity.Ticker
import dev.daeyeon.gaasproject.data.remote.api.UpbitApi
import dev.daeyeon.gaasproject.data.remote.response.MarketResponse
import dev.daeyeon.gaasproject.data.remote.response.TickerResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import java.util.*

class UpbitRepository(private val upbitApi: UpbitApi) : UpbitDataSource {

    override suspend fun getTicker(
        markets: String
    ): Flow<StateResult<List<Ticker>>> = flow {
        emit(StateResult.loading())
        while (true) {
            emit(StateResult.success(upbitApi.getTicker(markets).map(TickerResponse::toTicker)))
            delay(5000L)
        }
    }
        // 에러 핸들링
        .catch { e ->
            Log.e("UpbitRepository", "catch getTicker")
            emit(StateResult.error(Exception(e)))
        }
        // 최신 데이터만
        .conflate()
        // context 변경
        .flowOn(Dispatchers.IO)

    private fun matchTicker(tickerResponse: TickerResponse, filteringText: String): Boolean =
        tickerResponse.market.contains(filteringText)

    private fun getFilteringText(baseCurrency: String, searchTicker: String): String {
        return "$baseCurrency-" +
                if (searchTicker == UpbitDataSource.ALL_CURRENCY) {
                    ""
                } else {
                    searchTicker.toUpperCase(Locale.KOREA)
                }
    }

    override suspend fun getMarkets(): Flow<StateResult<List<Market>>> =
        flow {
            emit(StateResult.loading())
            emit(StateResult.success(upbitApi.getMarketCode()))
        }
            .catch { e -> emit(StateResult.error(Exception(e))) }
            .map {
                when (it) {
                    is StateResult.Success -> {
                        StateResult.success(
                            it.data
                                .groupBy { response: MarketResponse ->
                                    response.market.substringBefore(
                                        "-"
                                    )
                                }
                                .map { (key, value) ->
                                    Market(
                                        currency = key,
                                        searchWord = value.joinToString(separator = ",") { it.market })
                                }
                        )
                    }
                    is StateResult.Error -> it
                    is StateResult.Loading -> it
                }
            }
}