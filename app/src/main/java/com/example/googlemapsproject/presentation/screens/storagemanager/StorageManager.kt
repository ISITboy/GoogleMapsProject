package com.example.googlemapsproject.presentation.screens.storagemanager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.googlemapsproject.databinding.ActivityStorageManagerBinding
import com.example.googlemapsproject.presentation.screens.adapter.ConsigneeItemsAdapter
import com.example.googlemapsproject.presentation.screens.adapter.ShipperItemsAdapter
import com.example.googlemapsproject.presentation.screens.main.MainActivity
import com.example.googlemapsproject.presentation.screens.storagemanager.event.ManagerEvent
import com.example.googlemapsproject.presentation.screens.storagemanager.viewModel.StorageManagerViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StorageManager : AppCompatActivity() {
    lateinit var binding: ActivityStorageManagerBinding
    private val viewModel: StorageManagerViewModel by viewModels()
    private val shipperItemAdapter by lazy { ShipperItemsAdapter(viewModel) }
    private val consigneeItemAdapter by lazy { ConsigneeItemsAdapter(viewModel) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStorageManagerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initRecyclerViews()
        initItemTouchHelper()
        viewModel.getAllShipperItems()
        viewModel.getAllConsigneeItems()

        viewModel.getShipper().observe(this, Observer {
//            showMessage(it.isEmpty())
            shipperItemAdapter.updateAdapter(it)
            Log.d("MyLog","sizeStorageManager: ${it.size}")
        })
        viewModel.getConsignee().observe(this, Observer {
//            showMessage(it.isEmpty())
            consigneeItemAdapter.updateAdapter(it)
        })
    }

    private fun initRecyclerViews() = with(binding) {
        shipperRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@StorageManager)
            adapter = shipperItemAdapter
        }
        consigneeRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@StorageManager)
            adapter = consigneeItemAdapter
        }
    }

    fun onShipperAddClick(view: View) {
        viewModel.event.value = ManagerEvent.AddShipperItem
        AddManagerStorageItemFragment().show(
            supportFragmentManager,
            AddManagerStorageItemFragment().tag
        )
    }

    fun onConsigneeAddClick(view: View) {
        viewModel.event.value = ManagerEvent.AddConsigneeItem
        Log.d("MyLog", "value = ${viewModel.event.value}")
        AddManagerStorageItemFragment().show(
            supportFragmentManager,
            AddManagerStorageItemFragment().tag
        )
    }

    private fun initItemTouchHelper() = with(binding) {
        val swapHelper1 = ItemTouchHelper(itemTouchCallback())
        swapHelper1.attachToRecyclerView(consigneeRecyclerView)
        val swapHelper2 = ItemTouchHelper(itemTouchCallback())
        swapHelper2.attachToRecyclerView(shipperRecyclerView)

    }

    private fun itemTouchCallback()= object : ItemTouchHelper.SimpleCallback(
        0, // dragDirs - не используется для удаления, поэтому 0
        ItemTouchHelper.RIGHT // swipeDirs - разрешаем удаление свайпом влево и вправо
    ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                findRecyclerViewFromViewHolder(viewHolder)
//                showMessage("Note deleted")
            }

        }
    private fun findRecyclerViewFromViewHolder(viewHolder: RecyclerView.ViewHolder){
        // Проверяем тип ViewHolder или другие параметры, чтобы определить `RecyclerView`

        // Например, если у вас есть два разных типа ViewHolder:
        if (viewHolder is ConsigneeItemsAdapter.MyHolder) {
            // Этот ViewHolder относится к первому RecyclerView
            consigneeItemAdapter.removeItem(viewHolder.adapterPosition)
        } else if (viewHolder is ShipperItemsAdapter.MyHolder) {
            // Этот ViewHolder относится ко второму RecyclerView
            shipperItemAdapter.removeItem(viewHolder.adapterPosition)
        }
    }

    fun findRoute(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
    //    private fun showMessage(emptyList:Boolean){
//        if (emptyList){
//            binding.message.visibility = View.VISIBLE
//        } else binding.message.visibility = View.GONE
//    }
}