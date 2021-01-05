package com.example.mykunci.bluetooth.request

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import com.example.mykunci.bluetooth.IBluetoothEventListener

class DiscoverRequest(private val context : Context, private val eventListener: IBluetoothEventListener, private var bluetoothAdapter: BluetoothAdapter) : IBluetoothRequest  {

    private var discoveredDevices:MutableList<BluetoothDevice> = mutableListOf()

    init {
        registerReceiver()
    }

    fun discover() {
        discoveredDevices = mutableListOf()

        if (bluetoothAdapter.isDiscovering){
            bluetoothAdapter.cancelDiscovery()
        }

        eventListener.onDiscovering()
        bluetoothAdapter.startDiscovery()

    }

    private fun registerReceiver() {
        context.registerReceiver(discoverReceiver, IntentFilter(BluetoothDevice.ACTION_FOUND))
        context.registerReceiver(discoverReceiver, IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED))
    }

    private fun addDiscoveredDevice(device: BluetoothDevice) {
        if (device.bondState != BluetoothDevice.BOND_BONDED){
            return
        }

        for (device in discoveredDevices) {
            if (device.address == device.address)
                return
        }

        discoveredDevices.add(device)
    }

    private val discoverReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {

            if (BluetoothDevice.ACTION_FOUND == intent.action) {
                val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                if (device != null) {
                    addDiscoveredDevice(device)
                }
            }
            else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED == intent.action) {
                Log.d("DISCOVER FINISH, DEVICE : ", "$discoveredDevices")
                eventListener.onDiscovered(discoveredDevices)
            }
        }
    }

    override fun cleanup() {
        context.unregisterReceiver(discoverReceiver)
    }
}