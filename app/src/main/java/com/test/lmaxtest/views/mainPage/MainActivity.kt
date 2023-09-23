package com.test.lmaxtest.views.mainPage

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.test.lmaxtest.data.Instrument
import com.test.lmaxtest.data.Resource
import com.test.lmaxtest.data.network.ListItem
import com.test.lmaxtest.databinding.ActivityMainBinding
import com.test.lmaxtest.interfaces.ListSelector
import com.test.lmaxtest.interfaces.MainListSelector
import com.test.lmaxtest.utils.hideKeyboard
import com.test.lmaxtest.views.detailPage.DetailActivity
import com.test.lmaxtest.views.mainPage.adapter.InstrumentAdapter
import com.test.lmaxtest.views.mainPage.adapter.MainAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var assetClassList: MutableList<ListItem>
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private lateinit var mInstrumentAdapter: InstrumentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        simulateDataLoading("1")

        binding.imSort.setOnClickListener(View.OnClickListener {
            sortAssetClassList(viewModel.isAscendingSort)
        })

        binding.imFilter.setOnClickListener(View.OnClickListener {
            if (binding.searchLay.visibility != View.VISIBLE) {
                binding.searchLay.visibility = View.VISIBLE
            }
        })

        binding.imClose.setOnClickListener(View.OnClickListener {
            binding.etSearch.setText("")
            binding.searchLay.visibility = View.GONE
            hideKeyboard(binding.imClose)
        })

        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                val searchQuery = s.toString()
                if (searchQuery.isNotEmpty()) {
                    filterAssetClassList(searchQuery)
                } else {
                    listSetup(assetClassList)
                    mInstrumentAdapter.notifyDataSetChanged()
                }
            }

        })

        lifecycleScope.launch(Dispatchers.IO) {
            viewModel.dataResponse.collectLatest {
                when (it) {
                    is Resource.Failure -> {
                        Toast.makeText(
                            this@MainActivity,
                            it.exception.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    is Resource.Loading -> {

                    }

                    is Resource.Success -> {
                        try {
                            // Parse the JSON response into a JSONObject
                            val jsonResponse = it.result.toString()
                            val jsonObject = JSONObject(jsonResponse)

                            // Extract instruments and prices
                            val instruments = jsonObject.getJSONObject("instruments")
                            val prices = jsonObject.getJSONObject("prices")

                            // Create a map to store instruments grouped by assetClass
                            val instrumentsByAssetClass =
                                mutableMapOf<String, MutableList<Instrument>>()

                            // Declare assetClassInstruments outside the forEach loop
                            var assetClassInstruments: MutableList<Instrument>? = null

                            assetClassList = mutableListOf<ListItem>()

                            // Iterate through the instruments
                            instruments.keys().forEach { instrumentSymbol ->

                                var bid: Double? = null
                                var ask: Double? = null
                                try {
                                    // Extract "bid" and "ask" values from prices for a specific instrument
                                    bid = prices.getJSONObject(instrumentSymbol).getDouble("bid")
                                    ask = prices.getJSONObject(instrumentSymbol).getDouble("ask")
                                } catch (e: Exception) {
                                    Log.e("errorOnLoop", e.message.toString())
                                }

                                val instrumentData = instruments.getJSONObject(instrumentSymbol)
                                val assetClass = instrumentData.getString("assetClass")

                                // Create or retrieve the list of instruments for the assetClass
                                assetClassInstruments =
                                    instrumentsByAssetClass.getOrPut(assetClass) { mutableListOf() }

                                // Extract the required fields from the instrumentData
                                val instrument = Instrument(
                                    name = instrumentData.getString("name"),
                                    symbol = instrumentSymbol,
                                    bid = bid,
                                    ask = ask,
                                    id = instrumentData.getString("id"),
                                    assetClass = assetClass,
                                    quantityIncrement = instrumentData.getString("quantityIncrement"),
                                    priceIncrement = instrumentData.getString("priceIncrement")
                                )

                                assetClassInstruments?.add(instrument)
                            }

                            instrumentsByAssetClass.forEach { (assetClass, instruments) ->
                                println("Asset_Class: $assetClass")
                                assetClassList.add(ListItem.AssetClassItem(assetClass))
                                var intrumentList = mutableListOf<Instrument>()
                                instrumentsByAssetClass.values.flatten()
                                    .forEachIndexed { index, _ ->
                                        if (instrumentsByAssetClass.values.flatten()[index].assetClass == assetClass
                                        ) {
                                            instrumentsByAssetClass.values.flatten()[index]
                                                ?.let { it1 -> intrumentList.add(it1) }
                                        }
                                    }
                                assetClassList.add(ListItem.InstrumentItem(intrumentList))
                            }

                            withContext(Dispatchers.Main) {
                                delay(1000)
                                listSetup(assetClassList)
                            }

                        } catch (e: Exception) {
                            println("error_log : ${e.message}")
                        }
                    }

                    else -> {
                        Unit
                    }
                }
            }
        }
    }

    private fun filterAssetClassList(searchQuery: String) {
        val filteredAssetClassList = mutableListOf<ListItem>()

        for (item in assetClassList) {
            when (item) {
                is ListItem.AssetClassItem -> {
                    filteredAssetClassList.add(item)
                }

                is ListItem.InstrumentItem -> {
                    val filteredInstruments = item.instrument.filter { instrument ->
                        instrument.name!!.contains(searchQuery, ignoreCase = true)
                    }
                    filteredAssetClassList.add(ListItem.InstrumentItem(filteredInstruments))
                }
            }
        }

        listSetup(filteredAssetClassList)
        mInstrumentAdapter.notifyDataSetChanged()
    }

    private fun sortAssetClassList(ascending: Boolean) {

        val sortedAssetClassList = mutableListOf<ListItem>()

        for (item in assetClassList) {
            when (item) {
                is ListItem.AssetClassItem -> {
                    sortedAssetClassList.add(item)
                }

                is ListItem.InstrumentItem -> {
                    val sortedInstruments = if (ascending) {
                        viewModel.isAscendingSort = false
                        item.instrument.sortedByDescending { it.name }
                    } else {
                        viewModel.isAscendingSort = true
                        item.instrument.sortedBy { it.name }
                    }
                    sortedAssetClassList.add(ListItem.InstrumentItem(sortedInstruments))
                }
            }
        }
        assetClassList.clear()
        assetClassList.addAll(sortedAssetClassList)
        mInstrumentAdapter.notifyDataSetChanged()
    }

    private fun simulateDataLoading(type: String) {
        if (type == "1") {
            // Show the shimmer layout while loading data
            binding.shimmerLayout.startShimmer()
        } else {
            binding.shimmerLayout.stopShimmer()
            binding.shimmerLayout.visibility = View.GONE
            binding.dataRecyclerView.visibility = View.VISIBLE
        }
    }

    private fun listSetup(itemList: List<ListItem>) {
        // RecyclerView Selected List
        var layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mInstrumentAdapter = InstrumentAdapter(this, itemList, object : MainListSelector {
            override fun selectedList(id: String?, position: Int, subPosition: Int) {
                val instrumentItem = itemList[position] as ListItem.InstrumentItem
                val intent = Intent(this@MainActivity, DetailActivity::class.java)
                intent.putExtra("itemData", instrumentItem.instrument[subPosition])
                startActivity(intent)
            }
        })
        binding.dataRecyclerView.layoutManager = layoutManager
        binding.dataRecyclerView.adapter = mInstrumentAdapter

        simulateDataLoading("0")
    }
}