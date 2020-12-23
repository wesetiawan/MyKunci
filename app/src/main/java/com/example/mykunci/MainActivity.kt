package com.example.mykunci

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.action_bar_layout.*
import kotlinx.android.synthetic.main.bluetooth_connection.*
import kotlinx.android.synthetic.main.main_page.*
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(), View.OnClickListener {

    var m_myUUID: UUID = UUID.fromString("0000112f-0000-1000-8000-00805f9b34fb")


    private var permissionsRequired = arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION)
    private var permissionStatus: SharedPreferences? = null
    private var REQUEST_ENABLE_BT = 1
    private val REQUEST_PERMISSION_SETTING = 101
    private val PERMISSION_CALLBACK_CONSTANT = 100
    private var sentToSettings = false

    private var bluetoothAdapter: BluetoothAdapter? = null
    private lateinit var pairedDevice: Set<BluetoothDevice>
    lateinit var selectedDeviceAddress: String
    lateinit var selectedDeviceName: String

    private lateinit var mainLayout: LinearLayout
    private lateinit var bluetoothDeviceLayout: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainLayout = findViewById(R.id.mainPage)
        bluetoothDeviceLayout = findViewById(R.id.layoutBtDevice)

        permissionStatus = getSharedPreferences("permissionStatus", Context.MODE_PRIVATE)
        requestPermission()

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

        switchView(mainLayout)

        btnAdminPage.setOnClickListener(this)
        btnBtSetupMain.setOnClickListener(this)
        btnConnect.setOnClickListener(this)
        btnRefresh.setOnClickListener(this)

    }


    private fun checkBluetooth() {
        if (bluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth Tidak Tersedia Pada Perangkat Ini", Toast.LENGTH_SHORT).show()
            return
        }
        if (!bluetoothAdapter!!.isEnabled) {
            val enableBluetoothAdapter = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBluetoothAdapter, REQUEST_ENABLE_BT)
        }
    }

    override fun onClick(v: View) {
        when (v) {
            btnAdminPage -> {
                val adminLoginActivity = Intent(this, AdminLoginActivity::class.java)
                startActivity(adminLoginActivity)
            }
            btnBtSetupMain -> {
                switchView(bluetoothDeviceLayout)
                checkBluetooth()

            }
            btnRefresh -> {

            }
            btnConnect -> {

            }

        }
    }

    private fun pairedDeviceList() {
        pairedDevice = bluetoothAdapter!!.bondedDevices
        val listPairedBt: ArrayList<BluetoothDevice> = ArrayList()
        val listBtAddress: ArrayList<String> = ArrayList()
        if (pairedDevice.isNotEmpty()) {
            for (device: BluetoothDevice in pairedDevice) {
                listPairedBt.add(device)
                listBtAddress.add(device.name)
            }

        }
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, listBtAddress)
        lvBtDevice.adapter = adapter
        lvBtDevice.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            selectedDeviceAddress = listPairedBt[position].address
            selectedDeviceName = listBtAddress[position]
            Log.i("selectedDevice : ", selectedDeviceAddress)
        }

    }

    private fun switchView(v: View) {
        when (v) {
            mainLayout -> {
                mainLayout.visibility = View.VISIBLE
                bluetoothDeviceLayout.visibility = View.GONE
                tv_tittle.text = "Android Key"
            }
            bluetoothDeviceLayout -> {
                mainLayout.visibility = View.GONE
                bluetoothDeviceLayout.visibility = View.VISIBLE
                tv_tittle.text = "Pengaturan Bluetooth"
                pairedDeviceList()
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_CALLBACK_CONSTANT) {
            //check if all permissions are granted
            var allgranted = false
            for (i in grantResults.indices) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    allgranted = true
                } else {
                    allgranted = false
                    break
                }
            }

            if (allgranted) {
                Toast.makeText(applicationContext, "Allowed All Permissions", Toast.LENGTH_LONG).show()
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissionsRequired[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(this, permissionsRequired[1])
                    || ActivityCompat.shouldShowRequestPermissionRationale(this, permissionsRequired[2])) {

                getAlertDialog()
            } else {
                Toast.makeText(applicationContext, "Unable to get Permission", Toast.LENGTH_LONG).show()
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

    private fun requestPermission() {
        if (ActivityCompat.checkSelfPermission(this, permissionsRequired[0]) != PackageManager.PERMISSION_GRANTED) {
            when {
                ActivityCompat.shouldShowRequestPermissionRationale(this, permissionsRequired[0]) -> {

                    getAlertDialog()
                }
                permissionStatus!!.getBoolean(permissionsRequired[0], false) -> {
                    //Previously Permission Request was cancelled with 'Dont Ask Again',
                    // Redirect to Settings after showing Information about why you need the permission
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("Need Multiple Permissions")
                    builder.setMessage("This app needs permissions.")
                    builder.setPositiveButton("Grant") { dialog, which ->
                        dialog.cancel()
                        sentToSettings = true
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        val uri = Uri.fromParts("package", packageName, null)
                        intent.data = uri
                        startActivityForResult(intent, REQUEST_PERMISSION_SETTING)
                        Toast.makeText(applicationContext, "Go to Permissions to Grant ", Toast.LENGTH_LONG).show()
                    }
                    builder.setNegativeButton("Cancel") { dialog, which -> dialog.cancel() }
                    builder.show()
                }
                else -> {
                    //just request the permission
                    ActivityCompat.requestPermissions(this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT)
                }
            }

            val editor = permissionStatus!!.edit()
            editor.putBoolean(permissionsRequired[0], true)
            editor.apply()
        } else {
            //You already have the permission, just go ahead.
            checkBluetooth()
        }
    }

    private fun getAlertDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Need Multiple Permissions")
        builder.setMessage("This app needs permissions.")
        builder.setPositiveButton("Grant") { dialog, _ ->
            dialog.cancel()
            ActivityCompat.requestPermissions(this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT)
        }
        builder.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
        builder.show()
    }

    override fun onPostResume() {
        super.onPostResume()
        if (sentToSettings) {
            if (ActivityCompat.checkSelfPermission(this, permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                Toast.makeText(applicationContext, "Allowed All Permissions", Toast.LENGTH_LONG).show()
            }
        }
    }


}