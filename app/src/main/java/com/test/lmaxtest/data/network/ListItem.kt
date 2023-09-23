package com.test.lmaxtest.data.network

import com.test.lmaxtest.data.Instrument
import com.test.lmaxtest.views.mainPage.MainActivity

sealed class ListItem {
    data class AssetClassItem(val assetClass: String) : ListItem()
    data class InstrumentItem(val instrument: List<Instrument>) : ListItem()
}
