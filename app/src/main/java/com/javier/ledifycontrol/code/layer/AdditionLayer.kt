package com.javier.ledifycontrol.code.layer

import com.javier.ledifycontrol.R
import com.javier.ledifycontrol.code.model.RgbwColor

class AdditionLayer(val layers: List<Layer>)
    : Layer() {

    override fun toString() : String {
        var layers = ""
        var indices = ""
        this.layers.forEach { layer ->
            layers += "$layer+"
            val layerIndex = layer.index
            freeIndex(layerIndex)
            indices += "$layerIndex,"
        }
        if (!indices.isEmpty()) {
            indices = indices.substring(0, indices.length - 1)
        }
        return "${layers}ADD=$index,$indices"
    }

    override fun getIcon() : Int {
        return R.drawable.ic_add_circle_48dp
    }

    override fun getTint() : Int {
        if (layers.isEmpty()) {
            return RgbwColor(255,255,255,255).toArgb()
        }
        return layers.last().getTint()
    }
}