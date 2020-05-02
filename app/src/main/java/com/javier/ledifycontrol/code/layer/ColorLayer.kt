package com.javier.ledifycontrol.code.layer

import com.javier.ledifycontrol.R
import com.javier.ledifycontrol.code.model.RgbwColor


class ColorLayer(var color: RgbwColor)
    : Layer() {

    override fun toString() : String {
        val rgbw = color.toString()
        return "COLOR=$myIndex,$rgbw"
    }

    override fun getIcon() : Int {
        return R.drawable.ic_brightness_48dp
    }

    override fun getTint() : Int {
        return color.toArgb()
    }

}