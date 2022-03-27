package com.selfhelpindia.mobilemouse

import com.google.gson.Gson




class CommandParser {
    companion object{
        fun parseCommand(operation: OperationData?): String? {
            val gson = Gson()
            return gson.toJson(operation, OperationData::class.java)
        }
    }


}
