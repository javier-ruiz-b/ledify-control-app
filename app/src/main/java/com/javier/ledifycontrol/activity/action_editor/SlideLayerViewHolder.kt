package com.javier.ledifycontrol.activity.action_editor

import android.R
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SeekBar
import com.javier.ledifycontrol.code.layer.Layer
import com.javier.ledifycontrol.code.layer.SlideAnimationLayer
import kotlinx.android.synthetic.main.item_slide_layer.view.*


class SlideLayerViewHolder(view: View) : LayerViewHolder(view) {
    val spParent = view.spParent!!
    val sbSpeed = view.sbSpeed!!
    val tvSpeed = view.tvSpeed!!

    override fun bind(item: Layer, position: Int, adapter: ActionEditorActivity.LayerAdapter) {
        super.bind(item, position, adapter)

        item as SlideAnimationLayer
        val minSpeed = -1.2f
        val maxSpeed = 1.2f

        sbSpeed.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val sbSpeedValue = SpotLayerViewHolder.calculateFromSeekBarPosition(progress, minSpeed, maxSpeed)
                item.speed = sbSpeedValue
                tvSpeed.text = "%.1f".format(sbSpeedValue)
                adapter.updateStrip()
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
        sbSpeed.progress = SpotLayerViewHolder.calculateSeekBarPosition(item.speed, minSpeed, maxSpeed)

        if (adapter.actionEditor.layers().isNotEmpty()) {
            val myIndex = adapter.actionEditor.layers()[position].index
            val availableLayers = arrayListOf<String>()
            for (i in 0 until adapter.actionEditor.layers().count()) {
                val layer = adapter.actionEditor.layers()[i]
                if (layer.index != myIndex) {
                    availableLayers.add(layer.index.toString())
                }
            }
            val availableLayersAdapter = ArrayAdapter(view.context, R.layout.simple_spinner_item, availableLayers)
            availableLayersAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
            spParent.adapter = availableLayersAdapter
            spParent.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {}

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    val layerIndex = availableLayers[position].toInt()
                    val selectedLayer = adapter.actionEditor.findLayerByIndex(layerIndex)
                    if (selectedLayer != null) {
                        item.layer = selectedLayer
                        adapter.updateStrip()
                    }
                }
            }
        }
    }
}
