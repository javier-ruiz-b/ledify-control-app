package com.javier.ledifycontrol.code.net

import com.javier.ledifycontrol.code.layer.ColorLayer
import com.javier.ledifycontrol.code.layer.FadeToLayer
import com.javier.ledifycontrol.code.model.LedifyInterpolator
import com.javier.ledifycontrol.code.model.RgbwColor
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

    fun setColor(color: RgbwColor) {
        val layer = FadeToLayer(ColorLayer(color), LedifyInterpolator.Decelerate4x, 0, 500)
        getRequest(layer.setLayerString())
    }

    private fun performRequest(command: String)  {
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

    private fun nextFromQueue() {
        if (commandQueue.isEmpty()) {
            onRequest = false
        } else {
            performRequest(commandQueue.poll()!!)
        }
    }

}