package com.example.mykunci.bluetooth

import android.bluetooth.BluetoothDevice

interface IBluetoothEventListener {
    fun onEnable(requestEnable: Boolean)
    fun onDiscovering()
    fun onDiscovered(discoveredDevices:MutableList<BluetoothDevice>)
    fun onConnecting()
    fun onConnected(isSuccess: Boolean)
    fun onPairing()
    fun onPaired()
    fun onDisconnecting()
    fun onDisconnected()
}