package dev.daeyeon.gaasproject.data.entity

data class Ticker(
    val market: String,
    val tradePrice: String,
    val signedChangeRate: String,
    val accTradePrice24h: String
)