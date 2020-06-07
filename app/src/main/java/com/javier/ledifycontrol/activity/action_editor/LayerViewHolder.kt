package com.javier.ledifycontrol.activity.action_editor

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.javier.ledifycontrol.R
import com.javier.ledifycontrol.code.layer.Layer

open class LayerViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        var item: Layer? = null
        val ivIcon = view.findViewById<ImageView>(R.id.ivIcon)
        val tvName = view.findViewById<TextView>(R.id.tvName)

        open fun bind(item: Layer, position: Int, adapter: ActionEditorActivity.LayerAdapter) {
            this.item = item
            tvName.text = item.toString().replace('+', '\n')
            ivIcon.setImageResource(item.getIcon())
            ivIcon.setColorFilter(item.getTint())
            view.setOnClickListener { false }
            view.setOnLongClickListener {
                adapter.actionEditor.remove(this.item!!)
                adapter.notifyItemRemoved(position)
                adapter.updateStrip()
                true
            }
        }
    }