package com.test.lmaxtest.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Instrument(
    @SerialName("name")
    val name: String? = null,
    @SerialName("symbol")
    val symbol: String? = null,
    @SerialName("bid")
    val bid: Double? = null,
    @SerialName("ask")
    val ask: Double? = null,
    @SerialName("id")
    val id: String? = null,
    @SerialName("assetClass")
    val assetClass: String? = null,
    @SerialName("quantityIncrement")
    val quantityIncrement: String? = null,
    @SerialName("priceIncrement")
    val priceIncrement: String? = null
) : java.io.Serializable