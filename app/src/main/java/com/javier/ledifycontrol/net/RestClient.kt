package com.javier.ledifycontrol.net

import com.javier.ledifycontrol.layers.ColorLayer
import com.javier.ledifycontrol.layers.FadeToLayer
import com.javier.ledifycontrol.model.LedifyInterpolator
import com.javier.ledifycontrol.model.RgbwColor
import mu.KotlinLogging
import okhttp3.*
import java.io.IOException
import java.util.*

class RestClient(val baseUrl: String = "http://192.168.178.50:8033") {
    private val logger = KotlinLogging.logger {}
    private val client = OkHttpClient()
    private val commandQueue = PriorityQueue<String>()
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
        logger.info { "Sending: $command" }
        client.newCall(request).enqueue(object: Callback {
            override fun onFailure(call: Call?, e: IOException?) {
                logger.error { e }
                nextFromQueue()
            }

            override fun onResponse(call: Call?, response: Response?) {
                val responseStr = response?.body()?.string()
                val requestStr = response?.request()?.url().toString()
                logger.info { "Response: $responseStr Request: $requestStr" }
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

    fun setColor(color: RgbwColor) {
        val layer = FadeToLayer(ColorLayer(color), 500, LedifyInterpolator.Decelerate4x)
        getRequest(layer.setLayerString())
    }
}