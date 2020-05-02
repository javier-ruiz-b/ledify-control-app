package com.javier.ledifycontrol.activity

import android.app.Dialog
import android.os.Bundle
import android.view.*
import android.view.View.OnLongClickListener
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.*
import com.javier.ledifycontrol.R
import com.javier.ledifycontrol.code.action.ActionEditor
import com.javier.ledifycontrol.code.layer.AdditionLayer
import com.javier.ledifycontrol.code.layer.ColorLayer
import com.javier.ledifycontrol.code.layer.Layer
import com.javier.ledifycontrol.code.layer.SpotLayer
import com.javier.ledifycontrol.code.model.RgbwColor
import com.javier.ledifycontrol.code.net.RestClient
import com.javier.ledifycontrol.view.ColorPicker
import com.javier.ledifycontrol.view.ColorPicker.*
import io.github.yavski.fabspeeddial.SimpleMenuListenerAdapter
import kotlinx.android.synthetic.main.activity_action_editor.*
import kotlinx.android.synthetic.main.item_color_spot_layer.view.*
import kotlinx.android.synthetic.main.item_layer.view.*
import kotlinx.android.synthetic.main.item_layer.view.ivIcon
import kotlinx.android.synthetic.main.item_layer.view.tvName
import net.cachapa.expandablelayout.ExpandableLayout


class ActionEditorActivity : AppCompatActivity() {

    private val restClient = RestClient()
    private lateinit var viewAdapter: Adapter<*>
    private lateinit var viewManager: LayoutManager

    private lateinit var actionEditor: ActionEditor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_action_editor)

        var actionName = intent.getStringExtra("actionName")
        if (actionName == null) {
            actionName = "default"
        }

        etTitle.setText(actionName)
        etTitle.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                setActionEditor(actionEditor.copy(filesDir, etTitle.text.toString()))
            }
        }

        buttonTest.setOnClickListener {
            restClient.getRequest(actionEditor.command)
        }

        viewManager = LinearLayoutManager(this)
        setActionEditor(ActionEditor(filesDir, actionName))

        rv.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }

        fabSpeedDial.setMenuListener(object : SimpleMenuListenerAdapter() {
            override fun onMenuItemSelected(menuItem: MenuItem?): Boolean {
                when (menuItem?.itemId) {
                    R.id.layer_addition -> actionEditor.add(AdditionLayer(emptyList()))
                    R.id.layer_color -> actionEditor.add(ColorLayer(RgbwColor(255, 255, 255, 255)))
                    else -> return false
                }
                viewAdapter.notifyItemInserted(actionEditor.layers().count() - 1)
                return true
            }
        })
    }

    private fun setActionEditor(actionEditor: ActionEditor) {
        this.actionEditor = actionEditor
        viewAdapter = LayerAdapter(actionEditor)
    }

    override fun onPause() {
        super.onPause()
        actionEditor.save()
    }

    override fun onResume() {
        super.onResume()
    }

    class LayerAdapter(private val actionEditor: ActionEditor) :
            Adapter<ViewHolder>() {

        open class MyViewHolder(val view: View) : ViewHolder(view) {
            var item: Layer? = null
            val ivIcon = view.findViewById<ImageView>(R.id.ivIcon)
            val tvName = view.findViewById<TextView>(R.id.tvName)
        }

        class LayerViewHolder(layerView: View) : MyViewHolder(layerView) {}

        class ColorLayerViewHolder(colorView: View) : MyViewHolder(colorView) {
            val buttonColor: Button = view.buttonColor!!
            val colorPicker: ColorPicker = view.colorPicker!!
            val elColor: ExpandableLayout = view.elColor!!
            init {
                buttonColor.setOnClickListener{ elColor.toggle() }
            }
        }
//        class SpotLayerViewHolder(val view: View) : ViewHolder(view) {
//            var item: Layer? = null
//            val ivIcon = view.ivIcon!!
//            val tvName = view.tvName!!
//            val buttonColor: Button = view.buttonColor!!
//            val colorPicker: ColorPicker = view.colorPicker!!
//            val elColor: ExpandableLayout = view.elColor!!
//            init {
//                buttonColor.setOnClickListener{ elColor.toggle() }
//            }
//        }

        override fun getItemViewType(position: Int): Int {
            return actionEditor.layers()[position].id()
        }

        override fun onCreateViewHolder(parent: ViewGroup,
                                        viewType: Int): ViewHolder {
            return when (viewType) {
                ColorLayer::class.java.canonicalName.hashCode(),
                SpotLayer::class.java.canonicalName.hashCode() -> {
                    val ll = LayoutInflater.from(parent.context)
                            .inflate(R.layout.item_color_spot_layer, parent, false)
                    ColorLayerViewHolder(ll)
                }
                else -> {
                    val ll = LayoutInflater.from(parent.context)
                            .inflate(R.layout.item_layer, parent, false)
                    LayerViewHolder(ll)
                }
            }
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//            holder.view.setOnClickListener(View.OnClickListener { mFragment.onItemClicked(position) }
            val item = actionEditor.layers()[position]
            when (holder) {
                is MyViewHolder -> {
                    holder.item = item
                    holder.tvName.text = item.toString().replace('+', '\n')
                    holder.ivIcon.setImageResource(item.getIcon())
                    holder.ivIcon.setColorFilter(item.getTint())
                    holder.view.setOnClickListener { false }
                    holder.view.setOnLongClickListener {
                        actionEditor.remove(holder.item!!)
                        notifyItemRemoved(position)
                        true
                    }
                }
            }

            when (holder) {
                is ColorLayerViewHolder -> {
                    item as ColorLayer
                    holder.colorPicker.setColor(item.color)
                    holder.colorPicker.setOnColorChangedListener(object: OnColorChangedListener {
                        override fun onColorChanged(color: RgbwColor) {
                            if (holder.elColor.isExpanded) {
                                item.color = color
                            }
                        }
                    })

                    holder.buttonColor.setOnClickListener{
                        if (holder.elColor.isExpanded) {
                            holder.elColor.collapse()
                            notifyItemChanged(position)
                        } else {
                            holder.elColor.expand()
                        }
                    }
                }
            }
        }

        override fun getItemCount() = actionEditor.layers().size

    }

}