package com.javier.ledifycontrol.model

class RgbwColor(val red: Int, val green: Int, val blue: Int, val white: Int) {
    override fun toString(): String {
        return "$red,$green,$blue,$white"
    }

    companion object{
        fun fromColorAndBrightness(rgb: Int, white: Int, brightness: Int) : RgbwColor {
            val red = ((rgb shr 16) and 0xFF) * brightness / 255
            val green = ((rgb shr 8) and 0xFF) * brightness / 255
            val blue = (rgb and 0xFF) * brightness / 255
            val white = white * brightness / 255
            return RgbwColor(red, green, blue, white)
        }
    }
}