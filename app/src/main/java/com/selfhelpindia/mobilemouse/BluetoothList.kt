package com.selfhelpindia.mobilemouse

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.widget.Adapter
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class BluetoothList : AppCompatActivity() {

    private var bluetoothAdapter: BluetoothAdapter? = null
    private val REQUEST_ENABLE_BT = 0
    lateinit var listView: ListView
    private val allDevices = ArrayList<String>()
    private var deviceListAdapter: ArrayAdapter<String>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bluetooth_list)
        listView = findViewById(R.id.btList)
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if (bluetoothAdapter == null) {
            // Device doesn't support Bluetooth
            Toast.makeText(this, "Bluetooth Not Available", Toast.LENGTH_SHORT).show()
        }
        deviceListAdapter =
            ArrayAdapter(this, android.R.layout.simple_list_item_1, android.R.id.text1, allDevices)
        listView.adapter = deviceListAdapter

        if (bluetoothAdapter?.isEnabled == false) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
        } else {
            startScanning()
        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_ENABLE_BT) {
            startScanning()
        }
    }

    private fun startScanning() {
        bluetoothAdapter?.startDiscovery()
        getPairedDevices()
        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        registerReceiver(mReceiver, filter)
    }

    private val mReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            val action = intent.action
            if (BluetoothDevice.ACTION_FOUND == action) {
                val device =
                    intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                if (!allDevices.contains(device?.name + "\n" + device?.address))
                    allDevices.add(device?.name + "\n" + device?.address)
                deviceListAdapter?.notifyDataSetChanged()
            }
        }
    }

    private fun getPairedDevices() {
        val pairedDevices: Set<BluetoothDevice> = bluetoothAdapter?.bondedDevices!!
        if (pairedDevices.isNotEmpty()) {
            for (curDevice in pairedDevices) {
                allDevices.add(curDevice.name + "\n" + curDevice.address + "paired")
                deviceListAdapter?.notifyDataSetChanged()
            }
        }
    }

    override fun onDestroy() {
        unregisterReceiver(mReceiver)
        super.onDestroy()
    }
}