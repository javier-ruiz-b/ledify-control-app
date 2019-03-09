package com.javier.ledifycontrol.code.layer

import com.javier.ledifycontrol.R
import com.javier.ledifycontrol.code.model.LedifyInterpolator

class FadeLayer(val source: Layer, val destination: Layer,  val interpolator : LedifyInterpolator, val startMs: Int, val durationMs: Int)
    : Layer() {

    override fun toString() : String {
        val interpolatorInt = interpolator.value
        val sourceIndex = source.myIndex
        val destinationIndex = destination.myIndex
        freeIndex(sourceIndex)
        freeIndex(destinationIndex)
        return "$source+$destination+FADE=$myIndex,$sourceIndex,$destinationIndex,$interpolatorInt,$startMs,$durationMs"
    }

    override fun getIcon() : Int {
        return R.drawable.ic_blur_linear_48dp
    }

    override fun getTint() : Int {
        return destination.getTint()
    }
}