package com.javier.ledifycontrol.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.widget.SeekBar
import com.javier.ledifycontrol.R
import com.javier.ledifycontrol.layers.LayersCoordinator
import com.javier.ledifycontrol.model.RgbwColor
import com.javier.ledifycontrol.net.RestClient
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
        mRestClient.setColor(color())
    }

    private fun sendColor() {
        if (mLoaded and !mSendColorHandler.hasMessages(1)) {
            val message = Message.obtain(mSendColorHandler, mSendColorRunnable)
            message.what = 1
            mSendColorHandler.sendMessageDelayed(message, 100)
        }
    }
    private fun color(): RgbwColor {
        return RgbwColor.fromColorAndBrightness(colorPickerView.color,
                sbWhite.progress,
                sbBrightness.progress)
    }

    private fun updateColor() {
        val color = color()

        tvRed.text = color.red.toString()
        tvGreen.text = color.green.toString()
        tvBlue.text = color.blue.toString()
        tvWhite.text = color.white.toString()

        sendColor()
    }
}
