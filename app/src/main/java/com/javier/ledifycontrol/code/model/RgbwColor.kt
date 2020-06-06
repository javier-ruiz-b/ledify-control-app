package com.javier.ledifycontrol.code.model

class RgbwColor(var red: Int, var green: Int, var blue: Int, var white: Int) {
    override fun toString(): String {
        return "$red,$green,$blue,$white"
    }
    fun toArgb(): Int {
        val brightness = (red + green + blue + white) / 4

        val convRed = (red * brightness) / 255
        val convGreen = (green * brightness) / 255
        val convBlue = (blue * brightness) / 255
        return (255 shl 24) or (red shl 16) or (green shl 8) or blue
    }

    fun set(color: RgbwColor) {
        this.red = color.red
        this.green = color.green
        this.blue = color.blue
        this.white = color.white
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