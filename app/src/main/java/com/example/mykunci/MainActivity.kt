package com.example.mykunci

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.action_bar_layout.*
import kotlinx.android.synthetic.main.bluetooth_connection.*
import kotlinx.android.synthetic.main.main_page.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private var bluetoothAdapter: BluetoothAdapter? = null
    private lateinit var pairedDevice: Set<BluetoothDevice>
    var REQUEST_ENABLE_BLUETOOTH = 1

    private lateinit var mainLayout: LinearLayout
    private lateinit var bluetoothDeviceLayout: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val languages = listOf("Java", "Kotlin", "Javascript", "PHP", "Python")
        lvBtDevice.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, languages)

        mainLayout = findViewById(R.id.mainPage)
        bluetoothDeviceLayout = findViewById(R.id.layoutBtDevice)

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        checkBluetooth()

        mainLayout.visibility = View.VISIBLE
        bluetoothDeviceLayout.visibility = View.INVISIBLE

        tv_tittle.text = "Android Key"

        btn_adminPage.setOnClickListener(this)
        btn_scanBtMain.setOnClickListener(this)
    }


    private fun checkBluetooth(){
        if (bluetoothAdapter == null){
            Toast.makeText(this,"Bluetooth Tidak Tersedia Pada Perangkat Ini", Toast.LENGTH_SHORT).show()
            return
        }
        if (!bluetoothAdapter!!.isEnabled){
            val enableBluetoothAdapter = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBluetoothAdapter, REQUEST_ENABLE_BLUETOOTH)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_ENABLE_BLUETOOTH){
            if (resultCode == Activity.RESULT_OK){
                if (bluetoothAdapter!!.isEnabled){
                    Toast.makeText(this,"Bluetooth Berhasil di Aktifkan", Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(this,"Bluetooth Gagal di Aktifkan", Toast.LENGTH_SHORT).show()
                }
            }
            else if (resultCode == Activity.RESULT_CANCELED){
                Toast.makeText(this,"Izin Menyalakan Bluetooth di Batalkan", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onClick(v: View) {
        when(v){
            btn_adminPage -> {
                val adminLoginActivity = Intent(this, AdminLoginActivity::class.java)
                startActivity(adminLoginActivity)
            }
            btn_scanBtMain -> {
                mainLayout.visibility = View.INVISIBLE
                bluetoothDeviceLayout.visibility = View.VISIBLE

            }

        }
    }

    override fun onBackPressed() {
        if (mainLayout.visibility == View.INVISIBLE && bluetoothDeviceLayout.visibility == View.VISIBLE){
            mainLayout.visibility = View.VISIBLE
            bluetoothDeviceLayout.visibility = View.INVISIBLE
        }
        else{
            super.onBackPressed()
        }

    }



}