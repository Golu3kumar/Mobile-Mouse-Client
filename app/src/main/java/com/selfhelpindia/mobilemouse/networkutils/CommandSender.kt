package com.selfhelpindia.mobilemouse.networkutils

import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import com.selfhelpindia.mobilemouse.CursorActivity
import java.io.IOException
import java.io.OutputStreamWriter
import java.io.Writer
import java.net.Socket
import java.util.concurrent.atomic.AtomicBoolean


class CommandSender() : Runnable {

    val SERVER_IP = "192.168.43.42"
    val SERVER_PORT = 6666
    val SEND_COMAND = 0
    private val TAG = "CommandSender"
    public var isConnected = AtomicBoolean()

    private var mServerIP: String? = null
    private var mServerPort = 0
    public var socket: Socket? = null


    constructor(serverIp: String?, port: Int) : this() {
        mServerIP = serverIp ?: SERVER_IP
        mServerPort = if (port == -1) SERVER_PORT else port


    }

    public fun send(str: String): Boolean {
        return try {
            Log.i("fuck", "ip: " + mServerIP + "port: " + SERVER_PORT)
            socket = Socket(mServerIP, SERVER_PORT)
            Log.i("fuck", "socket: $socket")
            val writer: Writer = OutputStreamWriter(socket!!.getOutputStream())
            writer.append(str)
            writer.flush()
            writer.close()
            true
        } catch (e: IOException) {
            Log.d(TAG, "send: got error")
            socket?.close()
            false
        }
    }


    override fun run() {
        isConnected.set(send(" "))
        Log.d(TAG, "run: ${isConnected.get()}")
        Looper.prepare()
        mHandler = object : Handler() {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                when (msg.what) {
                    SEND_COMAND -> {
                        val flag = send(msg.obj.toString())
                        if (!flag) {
                            Looper.myLooper()?.quit()

                        }
                    }
                    else -> Log.e(
                        CommandSender::class.java.name,
                        "Unknown command msg.what = " + msg.what
                    )
                }
            }
        }

        Looper.loop()
    }

    companion object MyHandler {
        var mHandler: Handler? = null

    }


}