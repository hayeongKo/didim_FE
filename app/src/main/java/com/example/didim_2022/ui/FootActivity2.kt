package com.example.didim_2022.ui

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import androidx.appcompat.app.AppCompatActivity
import com.example.didim_2022.R
import com.example.didim_2022.databinding.ActivityFootBinding
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.lang.Exception
import java.nio.charset.Charset
import java.util.*
import kotlin.collections.ArrayList

class FootActivity2: AppCompatActivity() {

    var receiveData : TextView? = null
    var connecting = false

    val REQUEST_ENABLE_BT : Int = 10
    var mBluetoothAdapter : BluetoothAdapter? = null
    var mPairedDeviceCount : Int = 0
    var pairedDevices : Set<BluetoothDevice>? = null
    var mRemoteDevice : BluetoothDevice? = null
    var mSocket: BluetoothSocket? = null
    var mOutputStream: OutputStream? = null
    var mInputStream: InputStream? = null
    var mWorkerThread: Thread = Thread()

    var readBuffer : ByteArray? = null
    var bufferPosition : Int = 0

    private lateinit var binding: ActivityFootBinding

    @Override
    protected override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when(requestCode) {
            REQUEST_ENABLE_BT -> {
                if (resultCode == RESULT_OK) {
                    selectPairedDevice();
                } else if (resultCode == RESULT_CANCELED) {
                    finish()
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    fun activateBluetooth() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if (mBluetoothAdapter == null) {
            Toast.makeText(applicationContext, "블루투스를 지원하지 않습니다!", Toast.LENGTH_SHORT)
            finish()
        } else {
            if (!mBluetoothAdapter!!.isEnabled) {
                var enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
            } else {
                selectPairedDevice()
            }
        }
    }

    fun selectPairedDevice() {
        pairedDevices = mBluetoothAdapter?.bondedDevices // error 가능성 존재
        mPairedDeviceCount = pairedDevices!!.size

        if (mPairedDeviceCount == 0) {
            Toast.makeText(applicationContext, "페어링된 장치가 없습니다!", Toast.LENGTH_SHORT)
            finish()
        }

        val listDevices = ArrayList<String>()
        for (device in pairedDevices!!) {
            listDevices.add(device.name)
        }
        var mAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, listDevices)
        val listView = findViewById<ListView>(R.id.listview)
        listView.adapter = mAdapter

        val items = listDevices.toTypedArray()

        listView.onItemClickListener =
            OnItemClickListener { parent, view, position, id ->
                if (connecting == false) {
                    connectToBluetoothDevice(items[position])
                }
                connecting = true
                findViewById<View>(R.id.selectBT).visibility = View.INVISIBLE
                findViewById<View>(R.id.listview).visibility = View.INVISIBLE
            }


    }

    fun receiveData() {
        val handler : Handler = Handler()
        readBuffer = ByteArray(1024)
        bufferPosition = 0

        mWorkerThread = Thread(Runnable {
            fun run() {
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        val bytesAvailable : Int = mInputStream!!.available()

                        if (bytesAvailable > 0) {
                            var packetBytes : ByteArray = ByteArray(bytesAvailable)
                            mInputStream!!.read(packetBytes)

                            var i = 0
                            while (i < bytesAvailable) {
                                if (packetBytes[i].toChar() == '\n') {
                                    val charset: Charset = Charsets.US_ASCII
                                    val data = String(readBuffer!!, charset)
                                    bufferPosition = 0

                                    handler.post(Runnable {
                                        fun run() {
                                            receiveData!!.setText(data)
                                        }
                                    })
                                } else {
                                    readBuffer!![bufferPosition++] = packetBytes[i]
                                }
                                i += 1
                            }
                        }
                    } catch (ex: IOException) {
                        finish()
                    }
                }
            }
        })

        mWorkerThread.start()
    }

    fun transmitData(msg: String) {
        var msg = msg
        msg += "\n"
        try {
            mOutputStream!!.write(msg.toByteArray()) // 문자열 전송
        } catch (e: Exception) {
            // 오류가 발생한 경우
            finish() // 액티비티 종료
        }
    }

    fun getDeviceFromBondedList(name: String): BluetoothDevice? {
        var selectedDevice: BluetoothDevice? = null

        for (device in pairedDevices!!) {
            if (name == device.name) {
                selectedDevice = device
                break
            }
        }

        return selectedDevice
    }

    @Override
    protected override fun onDestroy() {
        try {
            mWorkerThread.interrupt()
            mInputStream?.close()
            mOutputStream?.close()
            mSocket?.close()
        } catch (e: Exception) {

        }
        super.onDestroy()
    }

    fun connectToBluetoothDevice(selectedDeviceName: String) {
        mRemoteDevice = getDeviceFromBondedList(selectedDeviceName)
        var uuid : UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb")

        try {
            mSocket = mRemoteDevice?.createRfcommSocketToServiceRecord(uuid)

            mSocket?.connect()
            mOutputStream = mSocket?.outputStream
            mInputStream = mSocket?.inputStream

            receiveData()
        } catch (e: Exception) {
            Toast.makeText(getApplicationContext(), "connect error", Toast.LENGTH_SHORT).show();
            finish()
        }
    }



    protected override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFootBinding.inflate(layoutInflater)
        setContentView(binding.root)

        receiveData = findViewById(R.id.receiveData)

        activateBluetooth()
    }
}