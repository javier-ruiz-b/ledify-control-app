package com.javier.ledifycontrol

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.SeekBar
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    val restClient = RestClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        colorPickerView.setColorListener {
            updateColor()
        }

        buttonSend.setOnClickListener {
            restClient.setColor(
                    etRed.text.toString(),
                    etGreen.text.toString(),
                    etBlue.text.toString(),
                    etWhite.text.toString()
            )
        }
        sbBrightness.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                updateColor()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        buttonOn.setOnClickListener {
            restClient.getRequest("ON")
        }
        buttonOff.setOnClickListener {
            restClient.getRequest("OFF")
        }
        buttonRandom.setOnClickListener {
            restClient.getRequest("RANDOM")
        }
    }

    fun updateColor() {
        val color = colorPickerView.color
        val brightness = sbBrightness.progress
        val red = ((color shr 16) and 0xFF) * brightness / 255
        val green = (color shr 8) and 0xFF * brightness / 255
        val blue = color and 0xFF * brightness / 255

        tvBrightness.text = brightness.toString()

        etRed.setText(red.toString())
        etGreen.setText(green.toString())
        etBlue.setText(blue.toString())
    }
}
