package dev.daeyeon.gaasproject.data.source

import dev.daeyeon.gaasproject.data.NetResult
import dev.daeyeon.gaasproject.data.Ticker
import dev.daeyeon.gaasproject.network.UpbitApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import java.util.*

class UpbitRepository(private val upbitApi: UpbitApi) : UpbitDataSource {

    override var markets: String = ""

    override suspend fun getTicker(
        baseCurrency: String,
        searchTicker: String
    ): Flow<NetResult<List<Ticker>>> = flow {

        emit(NetResult.loading())

        getMarkets().collect {
            when (it) {
                is NetResult.Success -> {
                    markets = it.data

                    while (true) {
                        emit(
                            NetResult.success(
                                upbitApi.getTicker(markets)
                                    .filter { list ->
                                        list.market.contains(
                                            "$baseCurrency-" +
                                                    if (searchTicker == UpbitDataSource.ALL_CURRENCY) {
                                                        ""
                                                    } else {
                                                        searchTicker.toUpperCase(Locale.KOREA)
                                                    }
                                        )
                                    }
                                    .map { response -> response.toTicker() }
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

    override suspend fun getMarkets(): Flow<NetResult<String>> = flow {
        emit(NetResult.loading())
        try {
            if (markets.isEmpty()) {
                emit(
                    NetResult.success(
                        upbitApi.getMarketCode().joinToString(separator = ",") { it.market }
                    )
                )
            } else {
                emit(NetResult.success(markets))
            }

        } catch (e: Exception) {
            emit(NetResult.error(e))
        }
    }
}