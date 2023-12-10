package com.example.googlemapsproject.presentation.screens.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.googlemapsproject.R
import com.example.googlemapsproject.domain.models.Consignee
import com.example.googlemapsproject.presentation.screens.storagemanager.viewModel.StorageManagerViewModel

class ConsigneeItemsAdapter(
    private val viewModel: StorageManagerViewModel
): RecyclerView.Adapter<ConsigneeItemsAdapter.MyHolder>() {


    var items = ArrayList<Consignee>()
    class MyHolder(private val itemView: View) : RecyclerView.ViewHolder(itemView){
        val address =itemView.findViewById<TextView>(R.id.textAddress)
        val volume =itemView.findViewById<TextView>(R.id.textVolume)
        fun setData(item: Consignee) {
            address.text = item.address
            volume.text = item.volume.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MyHolder(
            inflater.inflate(R.layout.storage_manager_item, parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.setData(items[position])
    }
    fun updateAdapter(items: List<Consignee>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }
    fun removeItem(position:Int){
        viewModel.deleteConsignee(items[position])
        notifyItemRangeChanged(0,items.size)
        notifyItemRemoved(position)
    }
}