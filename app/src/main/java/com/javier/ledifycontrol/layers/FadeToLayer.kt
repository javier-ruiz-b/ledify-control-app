package com.javier.ledifycontrol.layers

import com.javier.ledifycontrol.R
import com.javier.ledifycontrol.model.LedifyInterpolator

class FadeToLayer(val layer: Layer, val durationMs: Int, val interpolator : LedifyInterpolator)
    : Layer() {

    var startMs = 0

    override fun toString() : String {
        val interpolatorInt = interpolator.value
        val previousCommand = layer.toString()
        val layerIndex = layer.myIndex
        Layer.freeIndex(layerIndex)
        return "$previousCommand+FADETO=$myIndex,$layerIndex,$interpolatorInt,$startMs,$durationMs"
    }

    override fun getIcon() : Int {
        return R.drawable.ic_keyboard_arrow_right_48dp
    }

    override fun getTint() : Int {
        return layer.getTint()
    }
}