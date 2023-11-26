package com.example.googlemapsproject.presentation.screens.storagemanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.example.googlemapsproject.R
import com.example.googlemapsproject.presentation.screens.storagemanager.viewModel.StorageManagerViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StorageManager : AppCompatActivity() {

    private val viewModel: StorageManagerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_storage_manager)
    }

    fun onShipperAddClick(view: View) {
        AddManagerStorageItemFragment().show(supportFragmentManager, AddManagerStorageItemFragment().tag)
    }


    fun onConsigneeAddClick(view: View) {

    }

}