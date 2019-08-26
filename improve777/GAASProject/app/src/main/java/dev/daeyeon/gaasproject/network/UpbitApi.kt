package dev.daeyeon.gaasproject.network

import dev.daeyeon.gaasproject.data.response.MarketResponse
import dev.daeyeon.gaasproject.data.response.TickerResponse
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface UpbitApi {

    @GET("market/all")
    suspend fun getMarketCode(): List<MarketResponse>

    @GET("ticker")
    suspend fun getTicker(@Query("markets") markets: String): List<TickerResponse>
}