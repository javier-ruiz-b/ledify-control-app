package com.javier.ledifycontrol.acitivity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.widget.Button
import android.widget.SeekBar
import com.javier.ledifycontrol.R
import com.javier.ledifycontrol.code.action.ActionEditor
import com.javier.ledifycontrol.code.action.AvailableActions
import com.javier.ledifycontrol.code.action.DefaultActions
import com.javier.ledifycontrol.code.model.RgbwColor
import com.javier.ledifycontrol.code.net.RestClient
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val mRestClient = RestClient()
    private var mLoaded = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        DefaultActions.createIfNotExist(filesDir)

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

        configureButton(buttonOn, AvailableActions.On)
        configureButton(buttonOff, AvailableActions.Off)
        buttonRandom.setOnClickListener {
            mRestClient.getRequest("RANDOM")
        }

        mSendColorHandler.postDelayed({ mLoaded = true}, 250)
    }

    fun configureButton(button: Button, action: AvailableActions) {
        button.setOnClickListener {
            //            mRestClient.getRequest("OFF")
            val command = ActionEditor(filesDir, action.fileName).command
            mRestClient.getRequest(command)
        }
        button.setOnLongClickListener {
            val intent = Intent(this, ActionEditorActivity::class.java)
                    .putExtra("actionName", action.fileName)
            startActivity(intent)
            true
        }
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
