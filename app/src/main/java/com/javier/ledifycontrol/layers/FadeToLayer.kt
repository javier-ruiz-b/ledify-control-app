package com.javier.ledifycontrol.layers

import com.javier.ledifycontrol.model.LedifyInterpolator

class FadeToLayer(val layer: BaseLayer, val durationMs: Int, val interpolator : LedifyInterpolator)
    : BaseLayer() {

    var startMs = 0

    override fun toString() : String {
        val interpolatorInt = interpolator.value
        val previousCommand = layer.toString()
        val layerIndex = layer.myIndex
        BaseLayer.freeIndex(layerIndex)
        return "$previousCommand+FADETO=$myIndex,$layerIndex,$interpolatorInt,$startMs,$durationMs"
    }
}