package com.javier.ledifycontrol.layers

import com.javier.ledifycontrol.model.RgbwColor


class ColorLayer(val color: RgbwColor)
    : BaseLayer() {

    override fun toString() : String {
        val rgbw = color.toString()
        return "COLOR=$myIndex,$rgbw"
    }


}