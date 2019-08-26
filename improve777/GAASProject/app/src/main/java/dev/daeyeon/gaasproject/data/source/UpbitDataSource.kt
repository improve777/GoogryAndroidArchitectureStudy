package dev.daeyeon.gaasproject.data.source

import dev.daeyeon.gaasproject.data.NetResult
import dev.daeyeon.gaasproject.data.Ticker
import kotlinx.coroutines.flow.Flow

interface UpbitDataSource {

    var markets: String

    suspend fun getTicker(
        baseCurrency: String,
        searchTicker: String
    ): Flow<NetResult<List<Ticker>>>

    suspend fun getMarkets(): Flow<NetResult<String>>

    companion object {
        const val ALL_CURRENCY = "전체"
        const val ALL_MARKET = ""
    }
}