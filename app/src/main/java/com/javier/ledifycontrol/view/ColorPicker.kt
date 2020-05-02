package com.javier.ledifycontrol.view

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.SeekBar
import com.javier.ledifycontrol.R
import com.javier.ledifycontrol.code.model.RgbwColor
import com.skydoves.colorpickerpreference.ColorListener
import com.skydoves.colorpickerpreference.ColorPickerView
import kotlinx.android.synthetic.main.view_color_picker.view.*

class ColorPicker @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyle: Int = 0,
        defStyleRes: Int = 0
) : LinearLayout(context, attrs, defStyle, defStyleRes) {
    private var listener : OnColorChangedListener? = null
    fun setOnColorChangedListener(l:OnColorChangedListener?) {
        listener = l
    }

    init {
        LayoutInflater.from(context)
                .inflate(R.layout.view_color_picker, this, true)
        orientation = VERTICAL

        cpView.setColorListener {
            updateColor()
        }

        sbBrightness.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                updateColor()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        sbWhite.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                updateColor()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    fun color(): RgbwColor {
        return RgbwColor.fromColorAndBrightness(cpView.color,
                sbWhite.progress,
                sbBrightness.progress)
    }

    private fun updateColor() {
        setColor(color())
    }

    fun setColor(color: RgbwColor) {
        cpView.setSavedColor(color.toArgb())

        tvRed.text = color.red.toString()
        tvGreen.text = color.green.toString()
        tvBlue.text = color.blue.toString()
        tvWhite.text = color.white.toString()

        llColor.background = ColorDrawable(color.toArgb())

        listener?.onColorChanged(color)
    }

    interface OnColorChangedListener {
        fun onColorChanged(color: RgbwColor)
    }
}