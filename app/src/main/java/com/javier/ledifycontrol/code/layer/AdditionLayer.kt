package com.javier.ledifycontrol.code.layer

import com.javier.ledifycontrol.R

class AdditionLayer(val layers: List<Layer>)
    : Layer() {

    override fun toString() : String {
        var layers = ""
        var indices = ""
        this.layers.forEach { layer ->
            layers += "$layer+"
            val layerIndex = layer.myIndex
            freeIndex(layerIndex)
            indices += "$layerIndex,"
        }
        indices = indices.substring(0,indices.length - 2)
        return "${layers}ADD=$myIndex,$indices"
    }

    override fun getIcon() : Int {
        return R.drawable.ic_add_circle_48dp
    }

    override fun getTint() : Int {
        return layers.last().getTint()
    }
}