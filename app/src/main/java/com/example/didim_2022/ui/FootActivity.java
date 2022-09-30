package com.example.didim_2022.ui; //코드 설명은 책에 있습니다.

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.didim_2022.R;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class FootActivity extends AppCompatActivity {

    TextView receiveData;
    Boolean connecting = false;

    static final int REQUEST_ENABLE_BT = 10;
    BluetoothAdapter mBluetoothAdapter;
    int mPairedDeviceCount = 0;
    Set<BluetoothDevice> pairedDevices;
    BluetoothDevice mRemoteDevice;
    BluetoothSocket mSocket = null;
    OutputStream mOutputStream = null;
    InputStream mInputStream = null;

    Thread mWorkerThread = null;

    byte[] readBuffer;
    int bufferPosition;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_ENABLE_BT:
                if (resultCode == RESULT_OK) {

                    selectPairedDevice();
                } else if (resultCode == RESULT_CANCELED) {

                    finish();
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    void activateBluetooth() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Toast.makeText(getApplicationContext(), "블루투스를 지원하지 않습니다!", Toast.LENGTH_SHORT);
            finish();
        } else {

            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent =
                        new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            } else {
                selectPairedDevice();
            }
        }
    }

    void selectPairedDevice() {
        pairedDevices = mBluetoothAdapter.getBondedDevices();
        mPairedDeviceCount = pairedDevices.size();


        if (mPairedDeviceCount == 0) {

            Toast.makeText(getApplicationContext(), "페어링된 장치가 없습니다!", Toast.LENGTH_SHORT);
            finish();
        }


        final List<String> listDevices = new ArrayList<>();
        for (BluetoothDevice device : pairedDevices) {
            listDevices.add(device.getName());
        }

        ArrayAdapter mAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listDevices);
        final ListView listView = findViewById(R.id.listview);
        listView.setAdapter(mAdapter);

        final String[] items = listDevices.toArray(new String[listDevices.size()]);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (connecting == false)
                    connectToBluetoothDevice(items[position]);

                connecting = true;
                findViewById(R.id.selectBT).setVisibility(View.INVISIBLE);
                findViewById(R.id.listview).setVisibility(View.INVISIBLE);
            }
        });

    }

    void receiveData() {
        final Handler handler = new Handler();

        readBuffer = new byte[1024];
        bufferPosition = 0;

        mWorkerThread = new Thread(new Runnable() {
            public void run() {
                while (!Thread.currentThread().isInterrupted()) {

                    try {
                        int bytesAvailable = mInputStream.available();

                        if (bytesAvailable > 0) {
                            byte[] packetBytes = new byte[bytesAvailable];
                            mInputStream.read(packetBytes);

                            int i = 0;
                            while (i < bytesAvailable) {

                                if (packetBytes[i] == '\n') {
                                    final String data = new String(readBuffer, "US-ASCII");
                                    bufferPosition = 0;

                                    handler.post(new Runnable() {
                                        public void run() {

                                            receiveData.setText(data);

                                        }

                                    });
                                } else {
                                    readBuffer[bufferPosition++] = packetBytes[i];
                                }

                                i += 1;
                            }   //end of for
                        }
                    } catch (IOException ex) {
                        // 데이터 수신 중 오류 발생
                        finish();
                    }
                }
            }
        });

        mWorkerThread.start();
    }
//
//    void transmitData(String msg) {
//        msg += "\n";
//
//        try {
//
//            mOutputStream.write(msg.getBytes());        // 문자열 전송
//        } catch (Exception e) {
//            // 오류가 발생한 경우
//            finish();        // 액티비티 종료
//        }
//    }

    BluetoothDevice getDeviceFromBondedList(String name) {
        BluetoothDevice selectedDevice = null;

        for (BluetoothDevice device : pairedDevices) {
            if (name.equals(device.getName())) {
                selectedDevice = device;
                break;
            }
        }

        return selectedDevice;
    }

    @Override
    protected void onDestroy() {
        try {
            mWorkerThread.interrupt();
            mInputStream.close();
            mOutputStream.close();
            mSocket.close();
        } catch (Exception e) {
        }

        super.onDestroy();
    }

    void connectToBluetoothDevice(String selectedDeviceName) {
        mRemoteDevice = getDeviceFromBondedList(selectedDeviceName);
        UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

        try {
            mSocket = mRemoteDevice.createRfcommSocketToServiceRecord(uuid);

            mSocket.connect();

            mOutputStream = mSocket.getOutputStream();
            mInputStream = mSocket.getInputStream();

            receiveData();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "connect error", Toast.LENGTH_SHORT).show();
            finish();        // 앱 종료
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foot);

        receiveData = findViewById(R.id.receiveData);

        activateBluetooth();
    }

}