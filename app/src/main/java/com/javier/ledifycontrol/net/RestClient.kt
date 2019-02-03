package com.javier.ledifycontrol.net

import android.util.Log
import com.javier.ledifycontrol.layers.ColorLayer
import com.javier.ledifycontrol.model.RgbwColor
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
                Log.e("Rest", "Failure: ${e.toString()}")
                nextFromQueue()
            }

            override fun onResponse(call: Call?, response: Response?) {
                val responseStr = response?.body()?.string()
                val requestStr = response?.request()?.url().toString()
                Log.i("Rest", "Response: $responseStr Request: $requestStr")
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

    fun setColor(color: RgbwColor) {
        val layerString = ColorLayer(0, color).toString()
        getRequest("$layerString+FADETO=1,0,2,0,500+SET=1")
    }
}