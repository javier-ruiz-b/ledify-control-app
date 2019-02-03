package com.javier.ledifycontrol.layers

import com.javier.ledifycontrol.model.RgbwColor

class ColorLayer(override val toIndex: Int, val color: RgbwColor)
    : BaseLayer(toIndex) {


    override fun toString() : String {
        val rgbw = color.toString()
        return "COLOR=$toIndex,$rgbw"
    }
}