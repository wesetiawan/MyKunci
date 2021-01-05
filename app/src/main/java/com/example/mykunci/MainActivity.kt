package com.example.mykunci

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.mykunci.bluetooth.BluetoothConnectionService
import com.example.mykunci.bluetooth.IBluetoothEventListener
import kotlinx.android.synthetic.main.action_bar_layout.*
import kotlinx.android.synthetic.main.bluetooth_connection.*
import kotlinx.android.synthetic.main.main_page.*

class MainActivity : AppCompatActivity(), View.OnClickListener, IBluetoothEventListener {

    private lateinit var mainLayout: LinearLayout
    private lateinit var bluetoothDeviceLayout: LinearLayout
    private lateinit var bluetoothConnectionService: BluetoothConnectionService
    private var pairedDevice : Set<BluetoothDevice>? = null
    private lateinit var bluetoothAdapter: BluetoothAdapter
    private lateinit var selectedDevice: BluetoothDevice
    private lateinit var btState: String

    private val REQUEST_ENABLE_BLUETOOTH = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

        bluetoothConnectionService = BluetoothConnectionService(this,bluetoothAdapter)

        mainLayout = findViewById(R.id.mainPage)
        bluetoothDeviceLayout = findViewById(R.id.layoutBtDevice)


        switchView(mainLayout)

        btnActionTopBar.setOnClickListener(this)
        btnBtSetupMain.setOnClickListener(this)
        btnAction.setOnClickListener(this)
        btnRefresh.setOnClickListener(this)
        bluetoothConnectionService.enableBluetoothAdapter()

    }


    override fun onClick(v: View) {
        when (v) {
            btnActionTopBar -> {
                val adminLoginActivity = Intent(this, AdminLoginActivity::class.java)
                startActivity(adminLoginActivity)
            }
            btnBtSetupMain -> {
                switchView(bluetoothDeviceLayout)
                bluetoothConnectionService.enableBluetoothAdapter()
            }
            btnRefresh -> {
                bluetoothConnectionService.discoverDevices()
            }
            btnAction -> {
                when(btnAction.text){
                    "pair" -> {

                    }
                    "connect" ->{

                    }

                }
            }

        }
    }


    private fun switchBluetoothIcon(action: String){
        when(action){
            "enable" ->{
                btnActionTopBar.setImageResource(R.drawable.ic_bluetooth_enable)
            }
            "disable" ->{
                btnActionTopBar.setImageResource(R.drawable.ic_bluetooth_disable)
            }
            "discovering" ->{
                btnActionTopBar.setImageResource(R.drawable.ic_bluetooth_searching)
            }
            "connected" ->{
                btnActionTopBar.setImageResource(R.drawable.ic_bluetooth_connected)
            }
            "admin" ->{
                btnActionTopBar.setImageResource(R.drawable.ic_account)
            }
        }
    }
    private fun switchView(v: View) {
        when (v) {
            mainLayout -> {
                mainLayout.visibility = View.VISIBLE
                bluetoothDeviceLayout.visibility = View.GONE
                tv_tittle.text = "Android Key"
                switchBluetoothIcon("admin")
            }
            bluetoothDeviceLayout -> {
                mainLayout.visibility = View.GONE
                bluetoothDeviceLayout.visibility = View.VISIBLE
                tv_tittle.text = "Pengaturan Bluetooth"
                pairedDeviceList()
                switchBluetoothIcon(checkBtState())
            }
        }
    }

    private fun msg(msg:String){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show()
    }

    private fun pairedDeviceList() {
        pairedDevice = bluetoothAdapter.bondedDevices
        val listPairedBt: ArrayList<BluetoothDevice> = ArrayList()
        val listBtAddress: ArrayList<String> = ArrayList()
        if (pairedDevice!!.isNotEmpty()) {
            for (device: BluetoothDevice in pairedDevice!!) {
                listPairedBt.add(device)
                listBtAddress.add(device.name)
            }

        }
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, listBtAddress)
        lvPairedDevice.adapter = adapter
        lvPairedDevice.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            btnAction.text = "Sambungkan"
            selectedDevice = listPairedBt[position]
        }

    }

    private fun checkBtState(): String{
        return when {
            bluetoothAdapter.isEnabled -> {
                "enable"
            }
            bluetoothAdapter.isDiscovering -> {
                "discovering"
            }
            else -> {
                "disable"
            }
        }
    }

    override fun onBackPressed() {
        if (mainLayout.visibility == View.GONE && bluetoothDeviceLayout.visibility == View.VISIBLE) {
            switchView(mainLayout)
        } else {
            super.onBackPressed()
        }

    }

    override fun onEnable(requestEnable: Boolean) {
        if(requestEnable){
            val enableIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableIntent,REQUEST_ENABLE_BLUETOOTH)
        }
    }

    override fun onDiscovering() {
        msg("Bluetooth discovering")
        switchBluetoothIcon(checkBtState())
    }

    override fun onDiscovered(discoveredDevices: MutableList<BluetoothDevice>) {
        switchBluetoothIcon("discovering")
        if (discoveredDevices.isNotEmpty()){
            val listBtDevice: ArrayList<BluetoothDevice> = ArrayList()
            val listBtDeviceAddress: ArrayList<String> = ArrayList()
                for (device: BluetoothDevice in discoveredDevices) {
                    listBtDevice.add(device)
                    listBtDeviceAddress.add(device.name)
                }

            val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, listBtDevice)
            discoverDevice.adapter = adapter
            discoverDevice.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
                btnAction.text = "Pair"
            }
            bluetoothConnectionService.cleanUp()
        }
    }

    override fun onConnecting() {

    }

    override fun onConnected(isSuccess: Boolean) {

    }

    override fun onPairing() {

    }

    override fun onPaired() {

    }

    override fun onDisconnecting() {

    }

    override fun onDisconnected() {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_ENABLE_BLUETOOTH && requestCode == RESULT_OK){
                Toast.makeText(this,"Bluetooth di aktifkan", Toast.LENGTH_LONG).show()
        }
    }


}