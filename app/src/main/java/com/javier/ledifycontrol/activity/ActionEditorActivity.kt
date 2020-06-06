package com.javier.ledifycontrol.activity

import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.*
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.javier.ledifycontrol.R
import com.javier.ledifycontrol.code.action.ActionEditor
import com.javier.ledifycontrol.code.layer.AdditionLayer
import com.javier.ledifycontrol.code.layer.ColorLayer
import com.javier.ledifycontrol.code.layer.Layer
import com.javier.ledifycontrol.code.layer.SpotLayer
import com.javier.ledifycontrol.code.model.LedifyInterpolator
import com.javier.ledifycontrol.code.model.RgbwColor
import com.javier.ledifycontrol.code.net.RestClient
import com.javier.ledifycontrol.view.ColorPicker
import com.javier.ledifycontrol.view.ColorPicker.*
import io.github.yavski.fabspeeddial.SimpleMenuListenerAdapter
import kotlinx.android.synthetic.main.activity_action_editor.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_color_spot_layer.view.*
import kotlinx.android.synthetic.main.item_color_spot_layer.view.colorPicker
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

        cbLiveUpdate.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                restClient.getRequest(actionEditor.command)
            }
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
                    R.id.layer_color_spot -> actionEditor.add(SpotLayer(RgbwColor(255, 255, 255, 255),150f,30f, LedifyInterpolator.Accelerate))
                    else -> return false
                }
                viewAdapter.notifyItemInserted(actionEditor.layers().count() - 1)
                return true
            }
        })
    }

    private val mSendUpdateHandler = Handler()
    private val mSendUpdateRunnable = Runnable {
        restClient.getRequest(actionEditor.command)
    }
    fun updateStrip() {
        if (cbLiveUpdate.isChecked) {
            if (!mSendUpdateHandler.hasMessages(1)) {
                val message = Message.obtain(mSendUpdateHandler, mSendUpdateRunnable)
                message.what = 1
                mSendUpdateHandler.sendMessageDelayed(message, 100)
            }
        }
    }

    private fun setActionEditor(actionEditor: ActionEditor) {
        this.actionEditor = actionEditor
        viewAdapter = LayerAdapter(actionEditor, this)
    }

    override fun onPause() {
        super.onPause()
        actionEditor.save()
    }

    override fun onResume() {
        super.onResume()
    }

    class LayerAdapter(private val actionEditor: ActionEditor, private val activity :ActionEditorActivity) :
            Adapter<ViewHolder>() {

        open class MyViewHolder(val view: View) : ViewHolder(view) {
            var item: Layer? = null
            val ivIcon = view.findViewById<ImageView>(R.id.ivIcon)
            val tvName = view.findViewById<TextView>(R.id.tvName)
        }

        class LayerViewHolder(layerView: View) : MyViewHolder(layerView) {}

        open class ColorLayerViewHolder(colorView: View) : MyViewHolder(colorView) {
            val buttonColor: Button = colorView.buttonColor!!
            val colorPicker: ColorPicker = colorView.colorPicker!!
            val elColor: ExpandableLayout = colorView.elColor!!
            init {
                buttonColor.setOnClickListener{ elColor.toggle() }
            }
        }

        class SpotLayerViewHolder(spotView: View) : ColorLayerViewHolder(spotView) {
            val sbPosition = spotView.sbPosition!!
            val tvPosition = spotView.tvPosition!!
            val sbSize = spotView.sbSize!!
            val tvSize = spotView.tvSize!!
        }

        override fun getItemViewType(position: Int): Int {
            return actionEditor.layers()[position].id()
        }

        override fun onCreateViewHolder(parent: ViewGroup,
                                        viewType: Int): ViewHolder {
            return when (viewType) {
                ColorLayer::class.java.canonicalName.hashCode() -> {
                    val ll = LayoutInflater.from(parent.context)
                            .inflate(R.layout.item_color_layer, parent, false)
                    ColorLayerViewHolder(ll)
                }
                SpotLayer::class.java.canonicalName.hashCode() -> {
                    val ll = LayoutInflater.from(parent.context)
                            .inflate(R.layout.item_color_spot_layer, parent, false)
                    SpotLayerViewHolder(ll)
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
                is SpotLayerViewHolder -> {
                    bindColorLayerVH(item, holder, position)
                    bindSpotLayerVH(item, holder, position)
                }
                is ColorLayerViewHolder -> {
                    bindColorLayerVH(item, holder, position)
                }
            }
        }

        private fun bindSpotLayerVH(item: Layer, holder: SpotLayerViewHolder, position: Int) {
            item as SpotLayer
            val minPosition = 0f
            val maxPosition = 300f

            holder.sbPosition.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    val position = calculateFromSeekBarPosition(progress, minPosition, maxPosition)
                    item.position = position
                    holder.tvPosition.text = "%.2f".format(position)
                    activity.updateStrip()
                }
                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })
            holder.sbPosition.progress = calculateSeekBarPosition(item.position, minPosition, maxPosition)

            val minSize = 0.1f
            val maxSize = 200f
            holder.sbSize.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    val size = calculateFromSeekBarPosition(progress, minSize, maxSize)
                    item.size = size
                    holder.tvSize.text = "%.2f".format(size)
                    activity.updateStrip()
                }
                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })
            holder.sbSize.progress = calculateSeekBarPosition(item.size, minSize, maxSize)
        }

        private fun bindColorLayerVH(item: Layer, holder: ColorLayerViewHolder, position: Int) {
            var itemColor :RgbwColor? = null
            when (item) {
                is ColorLayer -> {
                    itemColor = item.color
                }
                is SpotLayer -> {
                    itemColor = item.color
                }
            }
            holder.colorPicker.setColor(itemColor!!)
            holder.colorPicker.setOnColorChangedListener(object : OnColorChangedListener {
                override fun onColorChanged(color: RgbwColor) {
                    if (holder.elColor.isExpanded) {
                        itemColor.set(color)
                        activity.updateStrip()
                    }
                }
            })

            holder.buttonColor.setOnClickListener {
                if (holder.elColor.isExpanded) {
                    holder.elColor.collapse()
                    notifyItemChanged(position)
                } else {
                    holder.elColor.expand()
                }
            }
        }

        override fun getItemCount() = actionEditor.layers().size

        private fun calculateSeekBarPosition(value: Float, minValue: Float, maxValue: Float) =
                ((value - minValue) * 255f / (maxValue - minValue)).toInt()

        private fun calculateFromSeekBarPosition(value: Int, minValue: Float, maxValue: Float) =
                (value/255f)*(maxValue - minValue) + minValue

    }

}