package com.selfhelpindia.mobilemouse

import android.os.Message
import android.util.Log
import com.selfhelpindia.mobilemouse.networkutils.CommandSender

class Utility {


    companion object {

        fun sendCommand(operationKind: Int, x: Int, y: Int, input: String?) {
            val msg: Message = Message.obtain(CommandSender.mHandler)
            val operation = OperationData()
            operation.setOperationKind(operationKind)
            if (x != 0) operation.setMoveX(x)
            if (y != 0) operation.setMoveY(y)
            if (input != null) operation.setInputStr(input)
            Log.i("operation", "" + operation)
            msg.obj = CommandParser.parseCommand(operation)
            msg.sendToTarget()
        }

    }
}