package dev.daeyeon.gaasproject.data.remote.api

import dev.daeyeon.gaasproject.data.remote.response.MarketResponse
import dev.daeyeon.gaasproject.data.remote.response.TickerResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface UpbitApi {

    @GET("market/all")
    suspend fun getMarketCode(): List<MarketResponse>

    @GET("ticker")
    suspend fun getTicker(@Query("markets") markets: String): List<TickerResponse>
}