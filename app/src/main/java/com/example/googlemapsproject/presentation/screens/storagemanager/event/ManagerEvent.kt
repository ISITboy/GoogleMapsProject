package com.example.googlemapsproject.presentation.screens.storagemanager.event

sealed class ManagerEvent{
    object AddShipperItem:ManagerEvent()
    object AddConsigneeItem:ManagerEvent()
}
