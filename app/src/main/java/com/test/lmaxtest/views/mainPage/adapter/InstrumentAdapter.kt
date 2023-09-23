package com.test.lmaxtest.views.mainPage.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.test.lmaxtest.data.network.ListItem
import com.test.lmaxtest.databinding.AssetClassItemLayoutBinding
import com.test.lmaxtest.databinding.InstrumentItemLayoutBinding
import com.test.lmaxtest.interfaces.ListSelector
import com.test.lmaxtest.interfaces.MainListSelector

class InstrumentAdapter(val context : Context, private val items: List<ListItem>, val mCallBack: MainListSelector) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val ASSET_CLASS_VIEW_TYPE = 0
    private val INSTRUMENT_VIEW_TYPE = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ASSET_CLASS_VIEW_TYPE -> {
                val binding = AssetClassItemLayoutBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                AssetClassViewHolder(binding)
            }
            INSTRUMENT_VIEW_TYPE -> {
                val binding = InstrumentItemLayoutBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                InstrumentViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            ASSET_CLASS_VIEW_TYPE -> {
                val assetClassItem = items[position] as ListItem.AssetClassItem
                val assetClassViewHolder = holder as AssetClassViewHolder
                assetClassViewHolder.bind(assetClassItem.assetClass, position)
            }
            INSTRUMENT_VIEW_TYPE -> {
                val instrumentItem = items[position] as ListItem.InstrumentItem
                val instrumentViewHolder = holder as InstrumentViewHolder
                instrumentViewHolder.bind(instrumentItem, position, mCallBack, context)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is ListItem.AssetClassItem -> ASSET_CLASS_VIEW_TYPE
            is ListItem.InstrumentItem -> INSTRUMENT_VIEW_TYPE
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}

class AssetClassViewHolder(private val binding: AssetClassItemLayoutBinding) :
    RecyclerView.ViewHolder(binding.root) {
    // Bind asset class header data to the view
    fun bind(assetClass: String, position: Int) {
        binding.tvAssClass.text = assetClass
    }
}

class InstrumentViewHolder(private val binding: InstrumentItemLayoutBinding) :
    RecyclerView.ViewHolder(binding.root) {
    private lateinit var mMainAdapter: MainAdapter
    private lateinit var layoutManager: LinearLayoutManager

    // Bind instrument data to the view
    fun bind(
        instruments: ListItem.InstrumentItem,
        pos: Int,
        mCallBack: MainListSelector,
        context: Context
    ) {
        layoutManager = GridLayoutManager(context, 2)
        mMainAdapter = MainAdapter(instruments, object : ListSelector {
            override fun selectedList(id: String?, position: Int) {
                mCallBack.selectedList(id, pos, position)
            }
        })
        binding.itemRecycler.layoutManager = layoutManager
        binding.itemRecycler.adapter = mMainAdapter
    }
}