package com.javier.ledifycontrol.activity.action_editor

import android.view.View
import android.widget.Button
import com.javier.ledifycontrol.code.layer.ColorLayer
import com.javier.ledifycontrol.code.layer.Layer
import com.javier.ledifycontrol.code.layer.SpotLayer
import com.javier.ledifycontrol.code.model.RgbwColor
import com.javier.ledifycontrol.view.ColorPicker
import kotlinx.android.synthetic.main.item_color_layer.view.*
import net.cachapa.expandablelayout.ExpandableLayout

open class ColorLayerViewHolder(colorView: View) : LayerViewHolder(colorView) {
        private val buttonColor: Button = colorView.buttonColor!!
        private val colorPicker: ColorPicker = colorView.colorPicker!!
        val elColor: ExpandableLayout = colorView.elColor!!
        init {
            buttonColor.setOnClickListener{ elColor.toggle() }
        }

        override fun bind(item: Layer, position: Int, adapter: ActionEditorActivity.LayerAdapter) {
            super.bind(item, position, adapter)

            var itemColor : RgbwColor? = null
            when (item) {
                is ColorLayer -> {
                    itemColor = item.color
                }
                is SpotLayer -> {
                    itemColor = item.color
                }
            }
            colorPicker.setColor(itemColor!!)
            colorPicker.setOnColorChangedListener(object : ColorPicker.OnColorChangedListener {
                override fun onColorChanged(color: RgbwColor) {
                    if (elColor.isExpanded) {
                        itemColor.set(color)
                        adapter.updateStrip()
                    }
                }
            })

            buttonColor.setOnClickListener {
                if (elColor.isExpanded) {
                    elColor.collapse()
                    adapter.notifyItemChanged(position)
                } else {
                    elColor.expand()
                }
            }
        }
    }