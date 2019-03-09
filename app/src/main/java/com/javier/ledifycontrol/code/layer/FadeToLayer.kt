package com.javier.ledifycontrol.code.layer

import com.javier.ledifycontrol.R
import com.javier.ledifycontrol.code.model.LedifyInterpolator

class FadeToLayer(val layer: Layer, val interpolator : LedifyInterpolator, val startMs: Int, val durationMs: Int)
    : Layer() {

    override fun toString() : String {
        val interpolatorInt = interpolator.value
        val previousCommand = layer.toString()
        val layerIndex = layer.myIndex
        freeIndex(layerIndex)
        return "$previousCommand+FADETO=$myIndex,$layerIndex,$interpolatorInt,$startMs,$durationMs"
    }

    override fun getIcon() : Int {
        return R.drawable.ic_blur_linear_48dp
    }

    override fun getTint() : Int {
        return layer.getTint()
    }
}