package com.test.lmaxtest.views.mainPage.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.test.lmaxtest.R
import com.test.lmaxtest.data.Instrument
import com.test.lmaxtest.data.network.ListItem
import com.test.lmaxtest.databinding.InstrumentItemLayoutBinding
import com.test.lmaxtest.databinding.MainDataItemBinding
import com.test.lmaxtest.interfaces.ListSelector


class MainAdapter(private val itemList : ListItem.InstrumentItem, val mCallBack: ListSelector): RecyclerView.Adapter<MainAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val mBinding = DataBindingUtil.inflate<MainDataItemBinding>(
            LayoutInflater.from(viewGroup.context),
            R.layout.main_data_item, viewGroup, false
        )
        return ViewHolder(mBinding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.mBinding.tvName?.text = itemList?.instrument?.get(i)?.name
        viewHolder.mBinding.tvSymbol?.text = itemList?.instrument?.get(i)?.symbol
        viewHolder.mBinding.tvBid?.text = itemList?.instrument?.get(i)?.bid.toString()
        viewHolder.mBinding.tvAsk?.text = itemList?.instrument?.get(i)?.ask.toString()

        viewHolder.mBinding.layoutService.setOnClickListener(View.OnClickListener {
            mCallBack.selectedList("item", i)
        })

    }

    override fun getItemCount(): Int {
        return itemList?.instrument?.size!!

    }

    inner class ViewHolder(val mBinding: MainDataItemBinding) :
        RecyclerView.ViewHolder(mBinding.root)

}