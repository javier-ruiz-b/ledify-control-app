package com.javier.ledifycontrol.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.widget.Button
import com.javier.ledifycontrol.R
import com.javier.ledifycontrol.activity.action_editor.ActionEditorActivity
import com.javier.ledifycontrol.code.action.ActionEditor
import com.javier.ledifycontrol.code.action.AvailableActions
import com.javier.ledifycontrol.code.action.DefaultActions
import com.javier.ledifycontrol.code.model.RgbwColor
import com.javier.ledifycontrol.code.net.RestClient
import com.javier.ledifycontrol.view.ColorPicker.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val mRestClient = RestClient()
    private var mLoaded = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        DefaultActions.createIfNotExist(filesDir)

        configureButton(buttonOn, AvailableActions.On)
        buttonOn.setOnClickListener {
            mRestClient.getRequest("ON")
        }
        configureButton(buttonOff, AvailableActions.Off)
        buttonOff.setOnClickListener {
            mRestClient.getRequest("OFF")
        }
        buttonRandom.setOnClickListener {
            mRestClient.getRequest("RANDOM")
        }

        colorPicker.setOnColorChangedListener(object : OnColorChangedListener{
            override fun onColorChanged(color: RgbwColor) {
                sendColor()
            }
        })

        mSendColorHandler.postDelayed({ mLoaded = true }, 250)
    }

    private fun configureButton(button: Button, action: AvailableActions) {
        button.setOnClickListener {
            mRestClient.getRequest(ActionEditor(filesDir, action.fileName).command)
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
        mRestClient.setColor(colorPicker.color())
    }

    private fun sendColor() {
        if (mLoaded and !mSendColorHandler.hasMessages(1)) {
            val message = Message.obtain(mSendColorHandler, mSendColorRunnable)
            message.what = 1
            mSendColorHandler.sendMessageDelayed(message, 100)
        }
    }

}
