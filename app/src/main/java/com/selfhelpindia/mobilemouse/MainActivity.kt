package com.selfhelpindia.mobilemouse

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import com.google.zxing.integration.android.IntentIntegrator
import com.journeyapps.barcodescanner.CaptureActivity
import com.selfhelpindia.mobilemouse.databinding.ActivityMainBinding
import com.selfhelpindia.mobilemouse.networkutils.CommandSender

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var mSender: CommandSender

    //    lateinit var constants: Constants
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Extracting the information from the received connection info


        binding.connectWifiBtn.setOnClickListener {

            val ipAddress = binding.ipEditText.text.toString().trim()
            val port = binding.portEditText.text.toString().trim()
            if (ipAddress.isEmpty())
                binding.ipEditText.error = "empty ip address"
            if (port.isEmpty())
                binding.portEditText.error = "empty port number"
            //constants = Constants(ipAddress, port.toInt())
            startCursorActivity(ipAddress, port.toInt())
            //Thread(Thread1()).start()
        }

//        binding.connectBtBtn.setOnClickListener {
//            val intent = Intent(this,BluetoothList::class.java)
//            startActivity(intent)
//        }
        binding.materialToolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.setting -> {
                    Toast.makeText(this, "Setting is clicked!", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.exit -> {
                    //Toast.makeText(this, "QR clicked", Toast.LENGTH_SHORT).show()
                    finish()
                    true
                }
                else -> {
                    false
                }
            }

        }

        binding.scanQrBtn.setOnClickListener {
            scanQR()
        }
    }

    private fun scanQR() {
        val intentIntegrator = IntentIntegrator(this)
        intentIntegrator.setPrompt("Scan IP Address\nClick Vol. Up for FLASH")
        intentIntegrator.setOrientationLocked(true)
        intentIntegrator.setBeepEnabled(true)
        intentIntegrator.captureActivity = Capture::class.java
        intentIntegrator.initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)

        if (intentResult != null) {
            if (intentResult.contents == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show()
            } else {
                val data = intentResult.contents.split(" ")
                //constants = Constants(data[0], data[1].toInt())
                binding.textView.text = intentResult.contents
                startCursorActivity(data[0], data[1].toInt())
                //Thread(Thread1()).start()


            }
        }
    }

    private fun startCursorActivity(IP: String, PORT: Int) {
        binding.llProgressBar.visibility = View.VISIBLE
        mSender = CommandSender(IP, PORT)
        Thread(mSender).start()
        val intent = Intent(this, DashboardActivity::class.java).apply {
//            putExtra("IP_ADDRESS", IP)
//            putExtra("PORT", PORT)
        }
//        startActivity(intent)
        Handler().postDelayed({
            binding.llProgressBar.visibility = View.GONE
            if (mSender.isConnected.get()) {
                startActivity(intent)
            } else
                Toast.makeText(this, "Not connected", Toast.LENGTH_SHORT).show()
        }, 3000)

    }
}

class Capture : CaptureActivity() {

}
