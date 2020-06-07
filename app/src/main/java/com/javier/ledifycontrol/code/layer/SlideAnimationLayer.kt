package com.javier.ledifycontrol.code.layer

import com.javier.ledifycontrol.R

class SlideAnimationLayer(var layer: Layer, var speed: Float)
    : Layer() {

    override fun toString() : String {
        val previousCommand = layer.toString()
        val layerIndex = layer.index
//        freeIndex(layerIndex)
        return "$previousCommand+SLIDE=$index,$layerIndex,$speed"
    }

    override fun getIcon() : Int {
        return R.drawable.ic_play_arrow_48dp
    }

    override fun getTint() : Int {
        return layer.getTint()
    }
}