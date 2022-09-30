package com.example.didim_2022.ui

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.*
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
import android.os.Build
import androidx.core.app.ActivityCompat

import android.content.pm.PackageManager
import android.opengl.Visibility
import android.util.Log
import android.widget.AdapterView.*
import androidx.core.os.trace
import androidx.fragment.app.Fragment


class FootActivity2: AppCompatActivity() {

    var receiveData : TextView? = null
    var connecting = false

    val REQUEST_ENABLE_BT : Int = 10
    lateinit var mBluetoothAdapter : BluetoothAdapter
    var mPairedDeviceCount : Int = 0
    lateinit var pairedDevices : Set<BluetoothDevice>
    lateinit var mRemoteDevice : BluetoothDevice
    var mSocket: BluetoothSocket? = null
    var mOutputStream: OutputStream? = null
    var mInputStream: InputStream? = null
    lateinit var mWorkerThread: Thread

    lateinit var readBuffer : ByteArray
    var bufferPosition : Int = 0

    lateinit var item : ArrayList<String>
    lateinit var receiveValue : Array<String>

    private val sharedManager : SharedManager by lazy { SharedManager(this) }

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

        item = ArrayList(listDevices.size)
        var items = listDevices.toArray(item.toArray())

        Log.d("itemValue", "selectPairedDevice: " + items)

        listView.onItemClickListener =
            OnItemClickListener { parent, view, position, id ->
                if (connecting == false) {
                    connectToBluetoothDevice(items[position] as String)
                }
                connecting = true
                findViewById<View>(R.id.selectBT).visibility = View.INVISIBLE
                findViewById<View>(R.id.listview).visibility = View.INVISIBLE
            }

    }


    fun receiveData() {
        val handler = Handler()
        readBuffer = ByteArray(1024)
        bufferPosition = 0

        mWorkerThread = Thread(Runnable {
            fun run() {
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        val bytesAvailable : Int = mInputStream!!.available()

                        if (bytesAvailable > 0) {
                            var packetBytes = ByteArray(bytesAvailable)
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
                                            receiveValue = data.split(",").toTypedArray()
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
    override fun onDestroy() {
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
        mRemoteDevice = getDeviceFromBondedList(selectedDeviceName)!!
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



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFootBinding.inflate(layoutInflater)
        setContentView(binding.root)

        receiveData = findViewById(R.id.receiveData)


        checkPermissions()
        activateBluetooth()
        //setPeakLeft(receiveData[3].toInt())
        //setPeakRight(receiveData[4].toInt())
        //sharedpreference
        val currentSensor = Sensor().apply {
            count += receiveValue[0].toInt()
            when(receiveValue[1]){
                "0" -> {
                    miss += 1
                    score = "miss"
                }
                "1" -> {
                    bad += 1
                    score = "bad"
                }
                "2" -> {
                    good += 1
                    score = "good"
                }
                "3" -> {
                    perfect += 1
                    score = "perfect"
                }
            }
            when(receiveValue[2]){
                "1" -> ajudge = "바른 걸음"
                "2" -> ajudge = "팔자 걸음"
                "3" -> ajudge = "안짱 걸음"
                else -> ajudge = "이전과 똑같이 걷고 있어"
            }
        }
        sharedManager.saveCurrentSensor(currentSensor)

        finish()
    }

    private val PERMISSIONS_STORAGE = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS,
        Manifest.permission.BLUETOOTH_SCAN,
        Manifest.permission.BLUETOOTH_CONNECT,
        Manifest.permission.BLUETOOTH_PRIVILEGED
    )
    private val PERMISSIONS_LOCATION = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS,
        Manifest.permission.BLUETOOTH_SCAN,
        Manifest.permission.BLUETOOTH_CONNECT,
        Manifest.permission.BLUETOOTH_PRIVILEGED
    )

    private fun checkPermissions() {
        val permission1 =
            ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val permission2 =
            ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN)
        if (permission1 != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                this,
                PERMISSIONS_STORAGE,
                1
            )
        } else if (permission2 != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                PERMISSIONS_LOCATION,
                1
            )
        }
    }

    fun setPeakLeft(int : Int) {
        when(int) {
            1 -> {
                binding.footLeftCircleChecked1Iv.visibility = VISIBLE
                binding.footLeftCircleChecked2Iv.visibility = GONE
                binding.footLeftCircleChecked3Iv.visibility = GONE
                binding.footLeftCircleChecked4Iv.visibility = GONE
                binding.footLeftCircleChecked6Iv.visibility = GONE
            }
            2 -> {
                binding.footLeftCircleChecked1Iv.visibility = GONE
                binding.footLeftCircleChecked2Iv.visibility = VISIBLE
                binding.footLeftCircleChecked3Iv.visibility = GONE
                binding.footLeftCircleChecked4Iv.visibility = GONE
                binding.footLeftCircleChecked6Iv.visibility = GONE
            }
            4-> {
                binding.footLeftCircleChecked1Iv.visibility = GONE
                binding.footLeftCircleChecked2Iv.visibility = GONE
                binding.footLeftCircleChecked3Iv.visibility = VISIBLE
                binding.footLeftCircleChecked4Iv.visibility = GONE
                binding.footLeftCircleChecked6Iv.visibility = GONE
            }
            5 -> {
                binding.footLeftCircleChecked1Iv.visibility = GONE
                binding.footLeftCircleChecked2Iv.visibility = GONE
                binding.footLeftCircleChecked3Iv.visibility = GONE
                binding.footLeftCircleChecked4Iv.visibility = VISIBLE
                binding.footLeftCircleChecked6Iv.visibility = GONE
            }
            6 -> {
                binding.footLeftCircleChecked1Iv.visibility = GONE
                binding.footLeftCircleChecked2Iv.visibility = GONE
                binding.footLeftCircleChecked3Iv.visibility = GONE
                binding.footLeftCircleChecked4Iv.visibility = GONE
                binding.footLeftCircleChecked6Iv.visibility = VISIBLE
            }
        }
    }

    fun setPeakRight(int : Int) {
        when(int) {
            1 -> {
                binding.footRightCircleChecked1Iv.visibility = VISIBLE
                binding.footRightCircleChecked2Iv.visibility = GONE
                binding.footRightCircleChecked3Iv.visibility = GONE
                binding.footRightCircleChecked4Iv.visibility = GONE
                binding.footRightCircleChecked6Iv.visibility = GONE
            }
            2 -> {
                binding.footRightCircleChecked1Iv.visibility = GONE
                binding.footRightCircleChecked2Iv.visibility = VISIBLE
                binding.footRightCircleChecked3Iv.visibility = GONE
                binding.footRightCircleChecked4Iv.visibility = GONE
                binding.footRightCircleChecked6Iv.visibility = GONE
            }
            4-> {
                binding.footRightCircleChecked1Iv.visibility = GONE
                binding.footRightCircleChecked2Iv.visibility = GONE
                binding.footRightCircleChecked3Iv.visibility = VISIBLE
                binding.footRightCircleChecked4Iv.visibility = GONE
                binding.footRightCircleChecked6Iv.visibility = GONE
            }
            5 -> {
                binding.footRightCircleChecked1Iv.visibility = GONE
                binding.footRightCircleChecked2Iv.visibility = GONE
                binding.footRightCircleChecked3Iv.visibility = GONE
                binding.footRightCircleChecked4Iv.visibility = VISIBLE
                binding.footRightCircleChecked6Iv.visibility = GONE
            }
            6 -> {
                binding.footRightCircleChecked1Iv.visibility = GONE
                binding.footRightCircleChecked2Iv.visibility = GONE
                binding.footRightCircleChecked3Iv.visibility = GONE
                binding.footRightCircleChecked4Iv.visibility = GONE
                binding.footRightCircleChecked6Iv.visibility = VISIBLE
            }
        }
    }

    fun setDataAtFragment(fragment: Fragment) {
        val currentSensor = sharedManager.getCurrentSensor()
        val bundle = Bundle()
        bundle.putInt("count", currentSensor.count)
        bundle.putString("score", currentSensor.score)
        bundle.putString("ajudge", currentSensor.ajudge)
        bundle.putInt("miss", currentSensor.miss)
        bundle.putInt("bad", currentSensor.bad)
        bundle.putInt("good", currentSensor.good)
        bundle.putInt("perfect", currentSensor.perfect)

        fragment.arguments = bundle
        setFragment(fragment)
    }
    fun setFragment(fragment: Fragment) {
        val transaction = supportFragmentManager. beginTransaction()
        transaction.replace(R.id.FragmentHome, fragment)
        transaction.commit()
    }


}