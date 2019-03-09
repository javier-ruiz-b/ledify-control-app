package com.javier.ledifycontrol.code.layer

import com.javier.ledifycontrol.R
import com.javier.ledifycontrol.code.model.LedifyInterpolator
import com.javier.ledifycontrol.code.model.RgbwColor


class SpotLayer(val color: RgbwColor,
                val position: Float,
                val size: Float,
                val interpolator: LedifyInterpolator)
    : Layer() {

    override fun toString() : String {
        val rgbw = color.toString()
        return "SPOT=$myIndex,$rgbw,$position,$size,${interpolator.value}"
    }

    override fun getIcon() : Int {
        return R.drawable.ic_arrow_drop_down_48dp
    }

    override fun getTint() : Int {
        return color.toArgb()
    }

}