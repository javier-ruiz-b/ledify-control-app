package com.javier.ledifycontrol.code.layer

import com.javier.ledifycontrol.R
import com.javier.ledifycontrol.code.model.LedifyInterpolator

class SlideAnimationLayer(val layer: Layer, val speed: Float)
    : Layer() {

    override fun toString() : String {
        val previousCommand = layer.toString()
        val layerIndex = layer.myIndex
        freeIndex(layerIndex)
        return "$previousCommand+SLIDE=$myIndex,$speed"
    }

    override fun getIcon() : Int {
        return R.drawable.ic_play_arrow_48dp
    }

    override fun getTint() : Int {
        return layer.getTint()
    }
}