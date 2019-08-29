package dev.daeyeon.gaasproject.data.source

import dev.daeyeon.gaasproject.data.StateResult
import dev.daeyeon.gaasproject.data.entity.Market
import dev.daeyeon.gaasproject.data.entity.Ticker
import kotlinx.coroutines.flow.Flow

interface UpbitDataSource {

    suspend fun getMarkets(): Flow<StateResult<List<Market>>>

    companion object {
        const val ALL_CURRENCY = "전체"
        const val ALL_MARKET = ""
    }

    suspend fun getTicker(
        markets: String,
        baseCurrency: String,
        searchTicker: String
    ): Flow<StateResult<List<Ticker>>>
}