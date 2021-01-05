package com.example.mykunci.bluetooth.request

import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import android.widget.Toast
import com.example.mykunci.bluetooth.IBluetoothEventListener

class EnableRequest(private val context : Context, private val eventListener: IBluetoothEventListener , private val bluetoothAdapter: BluetoothAdapter) : IBluetoothRequest{

    private var requestEnable = false

    fun enableBluetooth() {

        if (!bluetoothAdapter.isEnabled) {
            requestEnable = true
        }
        eventListener.onEnable(requestEnable)

    }

    fun disableBluetooth() {
        bluetoothAdapter.disable()
    }


    private val enableReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val action = intent?.action

            if (!requestEnable &&
                    BluetoothAdapter.ACTION_STATE_CHANGED != action)
                return;

            requestEnable = false
            eventListener.onEnable(requestEnable)
        }
    }

    override fun cleanup() {
        context.unregisterReceiver(enableReceiver)
    }

}