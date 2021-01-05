package com.example.mykunci.bluetooth

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.util.Log
import com.example.mykunci.bluetooth.request.ConnectionRequest
import com.example.mykunci.bluetooth.request.DiscoverRequest
import com.example.mykunci.bluetooth.request.EnableRequest
import com.example.mykunci.bluetooth.request.PairRequest

class BluetoothConnectionService(private val context: Context, private var bluetoothAdapter: BluetoothAdapter) {
    private var eventListener = context as IBluetoothEventListener
    private val enableRequest = EnableRequest(context, eventListener, bluetoothAdapter)
    private val discoverRequest = DiscoverRequest(context, eventListener,bluetoothAdapter)
    private val pairRequest = PairRequest(context, eventListener)
    private val connectionRequest = ConnectionRequest(context, eventListener)


    fun enableBluetoothAdapter() {
        enableRequest.enableBluetooth()
    }

    fun disableBluetoothAdapter() {
        enableRequest.disableBluetooth()
    }

    fun discoverDevices() {
        discoverRequest.discover()
    }

    fun pairDevice(device : BluetoothDevice) {
        pairRequest.pair(device)
    }

    fun connectDevice(device: BluetoothDevice) {
        connectionRequest.connect(device)
    }

    fun stopConnectDevice() {
        connectionRequest.stopConnect()
    }


    fun cleanUp() {
        enableRequest.cleanup()
        discoverRequest.cleanup()
        pairRequest.cleanup()
    }


}