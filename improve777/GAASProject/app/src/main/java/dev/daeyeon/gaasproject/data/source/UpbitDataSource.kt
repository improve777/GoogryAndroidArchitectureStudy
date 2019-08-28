package dev.daeyeon.gaasproject.data.source

import dev.daeyeon.gaasproject.data.StateResult
import dev.daeyeon.gaasproject.data.entity.Ticker
import kotlinx.coroutines.flow.Flow

interface UpbitDataSource {

    var markets: String

    suspend fun getTicker(
        baseCurrency: String,
        searchTicker: String
    ): Flow<StateResult<List<Ticker>>>

    suspend fun getMarkets(): Flow<StateResult<String>>

    companion object {
        const val ALL_CURRENCY = "전체"
        const val ALL_MARKET = ""
    }
}