package dev.daeyeon.gaasproject.data.source

import android.util.Log
import dev.daeyeon.gaasproject.data.NetResult
import dev.daeyeon.gaasproject.data.Ticker
import dev.daeyeon.gaasproject.data.response.TickerResponse
import dev.daeyeon.gaasproject.network.UpbitApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import java.util.*

class UpbitRepository(private val upbitApi: UpbitApi) : UpbitDataSource {

    override var markets: String = ""

    override suspend fun getTicker(
        baseCurrency: String,
        searchTicker: String
    ): Flow<NetResult<List<Ticker>>> = getMarkets().flatMapMerge {
        flow {
            when (it) {
                is NetResult.Success -> {
                    markets = it.data

                    while (true) {
                        emit(
                            NetResult.success(
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

                is NetResult.Error -> {
                    emit(NetResult.error(Exception(it.toString())))
                }

                NetResult.Loading -> {
                    emit(NetResult.loading())
                }
            }
        }
    }
        // 에러 핸들링
        .catch { e ->
            Log.e("UpbitRepository", "catch getTicker")
            emit(NetResult.error(Exception(e)))
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


    override suspend fun getMarkets(): Flow<NetResult<String>> = flow {
        emit(NetResult.loading())

        if (markets.isEmpty()) {
            emit(
                NetResult.success(
                    upbitApi.getMarketCode().joinToString(separator = ",") { it.market }
                )
            )
        } else {
            emit(NetResult.success(markets))
        }
    }
        .catch { e -> emit(NetResult.error(Exception(e))) }
        .flowOn(Dispatchers.IO)
}