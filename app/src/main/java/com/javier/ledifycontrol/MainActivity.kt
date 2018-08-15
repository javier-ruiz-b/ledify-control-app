package com.javier.ledifycontrol

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.widget.SeekBar
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val mRestClient = RestClient()
    private var mLoaded = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        colorPickerView.setColorListener {
            updateColor()
        }

        sbBrightness.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                updateColor()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        sbWhite.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                updateColor()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        buttonOn.setOnClickListener {
            mRestClient.getRequest("ON")
        }
        buttonOff.setOnClickListener {
            mRestClient.getRequest("OFF")
        }
        buttonRandom.setOnClickListener {
            mRestClient.getRequest("RANDOM")
        }

        mSendColorHandler.postDelayed({ mLoaded = true}, 250)
    }

    private val mSendColorHandler = Handler()
    private val mSendColorRunnable = Runnable {
        mRestClient.setColor(
                tvRed.text.toString(),
                tvGreen.text.toString(),
                tvBlue.text.toString(),
                tvWhite.text.toString()
        ) }

    private fun sendColor() {
        if (mLoaded and !mSendColorHandler.hasMessages(1)) {
            val message = Message.obtain(mSendColorHandler, mSendColorRunnable)
            message.what = 1
            mSendColorHandler.sendMessageDelayed(message, 100)
        }
    }

    fun updateColor() {
        val color = colorPickerView.color
        val brightness = sbBrightness.progress
        val red = ((color shr 16) and 0xFF) * brightness / 255
        val green = ((color shr 8) and 0xFF) * brightness / 255
        val blue = (color and 0xFF) * brightness / 255
        val white = sbWhite.progress * brightness / 255


        tvRed.text = red.toString()
        tvGreen.text = green.toString()
        tvBlue.text = blue.toString()
        tvWhite.text = white.toString()

        sendColor()
    }
}
