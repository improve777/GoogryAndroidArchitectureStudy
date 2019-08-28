package dev.daeyeon.gaasproject.data.source

import android.util.Log
import dev.daeyeon.gaasproject.data.StateResult
import dev.daeyeon.gaasproject.data.entity.Ticker
import dev.daeyeon.gaasproject.data.remote.response.TickerResponse
import dev.daeyeon.gaasproject.data.remote.api.UpbitApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import java.util.*

class UpbitRepository(private val upbitApi: UpbitApi) : UpbitDataSource {

    override var markets: String = ""

    override suspend fun getTicker(
        baseCurrency: String,
        searchTicker: String
    ): Flow<StateResult<List<Ticker>>> = getMarkets().flatMapMerge {
        flow {
            when (it) {
                is StateResult.Success -> {
                    markets = it.data

                    while (true) {
                        emit(
                            StateResult.success(
                                upbitApi.getTicker(markets)
                                    .filter { tickerResponse ->
                                        matchTicker(
                                            tickerResponse,
                                            getFilteringText(baseCurrency, searchTicker)
                                        )
                                    }
                                    .map(TickerResponse::toTicker)
                            )
                        )
                        delay(5000L)
                    }
                }

                is StateResult.Error -> {
                    emit(StateResult.error(Exception(it.toString())))
                }

                StateResult.Loading -> {
                    emit(StateResult.loading())
                }
            }
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


    override suspend fun getMarkets(): Flow<StateResult<String>> = flow {
        emit(StateResult.loading())

        if (markets.isEmpty()) {
            emit(
                StateResult.success(
                    upbitApi.getMarketCode().joinToString(separator = ",") { it.market }
                )
            )
        } else {
            emit(StateResult.success(markets))
        }
    }
        .catch { e -> emit(StateResult.error(Exception(e))) }
        .flowOn(Dispatchers.IO)
}