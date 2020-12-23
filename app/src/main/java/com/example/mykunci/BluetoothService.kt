package com.example.mykunci

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.*
import kotlin.collections.ArrayList

class BluetoothService(){


    private lateinit var mBtAdapter: BluetoothAdapter


    private lateinit var mDevicesArrayAdapter:ArrayAdapter<String>

    private lateinit var toastEdit:Toast

    private var address :String = ""

    private lateinit var pairedDeviceList: MutableSet<BluetoothDevice>

    private var activityAnterior1 :Class<*>? = null

    private lateinit var context :Context

    private lateinit var listView:ListView

    private var msg = ""

    private lateinit var segundaActivity:Class<*>


    constructor(context: Context,
                listView: ListView?) : this() {


        this.context = context
        this.listView = listView!!
    }


    fun onBluetooth (){
        mBtAdapter = BluetoothAdapter.getDefaultAdapter()

        if (mBtAdapter == null){
            Toast.makeText(context,"Dispositivo no soporta bluetooth",Toast.LENGTH_LONG).show()
        }else{
            if (mBtAdapter.isEnabled){
            }else{
                val enableBlue = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                context.startActivity(enableBlue)
            }
        }

        if (listView!=null){
            pairedDeviceList = mBtAdapter.bondedDevices
            mDevicesArrayAdapter = ArrayAdapter(context,R.layout.device_name)
            viewDips(mBtAdapter.bondedDevices)
        }else{
            pairedDeviceList = mBtAdapter.bondedDevices
        }

    }

    private fun viewDips(dispEmparejados: MutableSet<BluetoothDevice>) {
        if(dispEmparejados.size>0){
            for (device in dispEmparejados){
                mDevicesArrayAdapter.add(device.name+"\n"+device.address)
            }
            listView.adapter = mDevicesArrayAdapter
        }else{
            mDevicesArrayAdapter.add("no existen dispositivos vinculados")
        }
    }

    fun dispEmparejados():ArrayList<String>{
        val arrayList = ArrayList<String>()
        if(pairedDeviceList.size>0){
            for (device in pairedDeviceList){
                arrayList.add(device.name+"|"+device.address+device)
                println(device.name+" -- "+device.address)
            }
        }else{
            arrayList.add("no existen dispositivos vinculados")
        }
        return  arrayList
    }

    fun bluetoothSeleccion(position: Int){


        val content = listView.getItemAtPosition(position).toString()
        address = content.substring(content.length-17)
        val sp  = context.getSharedPreferences("direccion", Activity.MODE_PRIVATE)
        val editor = sp.edit()
        editor.putString("address", address)
        editor.apply()
    }


    fun bluetoothSeleccionAddres(address:String){
        val intent = Intent(context,segundaActivity)
        val sp  = context.getSharedPreferences("direccion", Activity.MODE_PRIVATE)
        val editor = sp.edit()
        editor.putString("address", address)
        editor.apply()
        context.startActivity(intent)


    }


    /**
     * Constructor 2 -----------------------------------------------
     */


    private var btSocket: BluetoothSocket? = null
    private var meInStream: InputStream? = null
    private var msOuStream: OutputStream? = null
    private lateinit var ctx2 :Context
    private var exitError = true
    private var mensajeError = "La conexion fallo"
    private var mensajeConectado = "jhr"


    constructor(context: Context,activityAnterior:Class<*>?) : this(){
        ctx2 = context
        this.activityAnterior1 = activityAnterior
    }


    private val BTMODULEUUID =
            UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")


    fun conectaBluetooth() :Boolean{
        var inicioConexion = false
        mBtAdapter = BluetoothAdapter.getDefaultAdapter()
        val sp: SharedPreferences = ctx2.getSharedPreferences("direccion", Activity.MODE_PRIVATE)
        val direccion = sp.getString("address", "")
        val dispositivo = mBtAdapter.getRemoteDevice(direccion)
        try {
            btSocket = crearSocket(device = dispositivo)
        } catch (e: IOException) {
            Toast.makeText(ctx2,"La creacion del socket fallo",Toast.LENGTH_LONG).show()
        }

        try {
            btSocket!!.connect()
        } catch (var5: IOException) {
            try {
                btSocket!!.close()
            } catch (var4: IOException) {
                Toast.makeText(ctx2,"algo salio mal",Toast.LENGTH_LONG).show()
            }
        }
        connectedThread(btSocket)
        inicioConexion = true
        mTx(mensajeConectado)
        return inicioConexion
    }


    private fun connectedThread(socket: BluetoothSocket?){
        var DatosIn: InputStream? = null
        var DatosOut: OutputStream? = null
        try {
            DatosIn = socket!!.inputStream
            DatosOut = socket.outputStream
        } catch (var6: IOException) {
        }
        meInStream = DatosIn
        msOuStream = DatosOut
    }


    fun mRx() :String {
        try{
            val memoriaTemporal = ByteArray(256)
            val bytes = meInStream!!.read(memoriaTemporal)
            msg += String(memoriaTemporal, 0, bytes)
        }catch (var4:IOException){
            msg = "error"
            if (exitError){
                ctx2.startActivity(Intent(ctx2,activityAnterior1))
            }
        }
        return msg
    }


    fun mensajeReset(){
        msg = ""
    }




    fun mTx(Entrada: String) {
        val MensajeBuffer = Entrada.toByteArray()
        try {
            msOuStream!!.write(MensajeBuffer)
        } catch (var10: IOException) {
            Toast.makeText(ctx2, mensajeError, Toast.LENGTH_LONG).show()
            var finaliza = Intent(ctx2, activityAnterior1)
            ctx2.startActivity(finaliza)
            try {
                btSocket!!.close()
            } catch (var9: IOException) {
                try {
                    btSocket!!.close()
                } catch (var8: IOException) {
                }
            }
        }
    }


    fun exitConexion(){
        btSocket!!.close()
    }

    fun mensajeErrorTx(string: String){
        mensajeError = string
    }

    fun mensajeConexion(string: String){
        mensajeConectado = string
    }

    fun exitErrorOk(boolean: Boolean){
        exitError = boolean
    }

    @Throws(IOException::class)
    private fun crearSocket(device: BluetoothDevice): BluetoothSocket? {
        return device.createRfcommSocketToServiceRecord(BTMODULEUUID)
    }



}