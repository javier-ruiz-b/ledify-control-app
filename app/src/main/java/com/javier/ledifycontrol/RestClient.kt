package com.javier.ledifycontrol

import android.util.Log
import okhttp3.*
import java.io.IOException
import java.util.*

class RestClient(val baseUrl: String = "http://192.168.178.50:8033") {
    val client = OkHttpClient()
    val commandQueue = PriorityQueue<String>()
    var onRequest = false

    fun getRequest(command: String) {
        if (onRequest) {
            commandQueue.add(command)
            return
        }
        onRequest = true
        performRequest(command)
    }

    fun performRequest(command: String)  {
        val request = Request.Builder()
                .url("$baseUrl/$command")
                .build()
        Log.i("Rest", "Sending: $command")
        client.newCall(request).enqueue(object: Callback {
            override fun onFailure(call: Call?, e: IOException?) {
                Log.e("Rest", "Failure")
                nextFromQueue()
            }

            override fun onResponse(call: Call?, response: Response?) {
                Log.i("Rest", "Response: ${response?.body()?.string()} Request: ${response?.request()?.url().toString()}")
                nextFromQueue()
            }
        })
    }

    fun nextFromQueue() {
        if (commandQueue.isEmpty()) {
            onRequest = false
        } else {
            performRequest(commandQueue.poll())
        }
    }

    fun setColor(red: String, green:String, blue: String, white: String) {
        getRequest("COLOR=0,$red,$green,$blue,$white+FADETO=1,0,2,0,500+SET=1")
    }
}