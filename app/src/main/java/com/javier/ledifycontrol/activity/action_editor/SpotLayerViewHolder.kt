package com.javier.ledifycontrol.activity.action_editor

import android.view.View
import android.widget.SeekBar
import com.javier.ledifycontrol.code.layer.Layer
import com.javier.ledifycontrol.code.layer.SpotLayer
import kotlinx.android.synthetic.main.item_color_spot_layer.view.*

class SpotLayerViewHolder(spotView: View) : ColorLayerViewHolder(spotView) {
        val sbPosition = spotView.sbPosition!!
        val tvPosition = spotView.tvPosition!!
        val sbSize = spotView.sbSize!!
        val tvSize = spotView.tvSize!!

        override fun bind(item: Layer, position: Int, adapter: ActionEditorActivity.LayerAdapter) {
            super.bind(item, position, adapter)

            item as SpotLayer
            val minPosition = 0f
            val maxPosition = 300f

            sbPosition.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    val sbPosition = Companion.calculateFromSeekBarPosition(progress, minPosition, maxPosition)
                    item.position = sbPosition
                    tvPosition.text = "%.1f".format(sbPosition)
                    adapter.updateStrip()
                }
                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })
            sbPosition.progress = Companion.calculateSeekBarPosition(item.position, minPosition, maxPosition)

            val minSize = 0.1f
            val maxSize = 300f
            sbSize.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    val size = Companion.calculateFromSeekBarPosition(progress, minSize, maxSize)
                    item.size = size
                    tvSize.text = "%.1f".format(size)
                    adapter.updateStrip()
                }
                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })
            sbSize.progress = Companion.calculateSeekBarPosition(item.size, minSize, maxSize)
        }

    companion object {
        fun calculateSeekBarPosition(value: Float, minValue: Float, maxValue: Float) =
                ((value - minValue) * 1000f / (maxValue - minValue)).toInt()

        fun calculateFromSeekBarPosition(value: Int, minValue: Float, maxValue: Float) =
                    (value / 1000f)*(maxValue - minValue) + minValue
    }
}
