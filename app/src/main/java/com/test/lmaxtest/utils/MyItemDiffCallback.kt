//package com.test.lmaxtest.utils
//
//import androidx.recyclerview.widget.DiffUtil
//
//class MyItemDiffCallback(private val oldList: ArrayList<ProductResult?>?, private val newList: ArrayList<ProductResult?>?) : DiffUtil.Callback() {
//
//    override fun getOldListSize(): Int {
//        return oldList?.size!!
//    }
//
//    override fun getNewListSize(): Int {
//        return newList?.size!!
//    }
//
//    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
//        val oldItem = oldList?.get(oldItemPosition)
//        val newItem = newList?.get(newItemPosition)
//        return oldItem?.id == newItem?.id
//    }
//
//    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
//        val oldItem = oldList?.get(oldItemPosition)
//        val newItem = newList?.get(newItemPosition)
//        return oldItem == newItem
//    }
//}