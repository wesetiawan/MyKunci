package com.example.mykunci.bluetooth

import android.bluetooth.BluetoothDevice

class EmptyBluetoothEventListener : IBluetoothEventListener {
    override fun onDisconnecting() {

    }

    override fun onDisconnected() {

    }

    override fun onConnected(isSuccess: Boolean) {

    }

    override fun onPairing() {

    }

    override fun onConnecting() {

    }

    override fun onDiscovering() {

    }

    override fun onDiscovered(discoveredDevices: MutableList<BluetoothDevice>) {

    }

    override fun onPaired() {

    }

    override fun onEnable(requestEnable: Boolean) {

    }
}