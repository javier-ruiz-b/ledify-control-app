package com.javier.ledifycontrol.activity.action_editor

import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.*
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.javier.ledifycontrol.R
import com.javier.ledifycontrol.code.action.ActionEditor
import com.javier.ledifycontrol.code.layer.*
import com.javier.ledifycontrol.code.model.LedifyInterpolator
import com.javier.ledifycontrol.code.model.RgbwColor
import com.javier.ledifycontrol.code.net.RestClient
import io.github.yavski.fabspeeddial.SimpleMenuListenerAdapter
import kotlinx.android.synthetic.main.activity_action_editor.*


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
                val lastLayer = if (actionEditor.layers().isNotEmpty()) {
                    actionEditor.layers().last()
                } else {
                    null
                }
                val layer = when (menuItem?.itemId) {
                    R.id.layer_addition -> AdditionLayer(emptyList())
                    R.id.layer_color -> ColorLayer(RgbwColor(255, 255, 255, 255))
                    R.id.layer_color_spot -> SpotLayer(RgbwColor(255, 255, 255, 255),150f,30f, LedifyInterpolator.Accelerate)
                    R.id.layer_slide-> lastLayer?.let { SlideAnimationLayer(it, 1f) }
                    R.id.layer_fade_to -> lastLayer?.let {  FadeToLayer(lastLayer, LedifyInterpolator.Linear, 0, 2000) }
                    else -> null
                }
                if (layer == null) {
                    Toast.makeText(this@ActionEditorActivity, "Add other layer first!", Toast.LENGTH_SHORT).show()
                    return false
                }

                actionEditor.add(layer)
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
        viewAdapter = LayerAdapter(actionEditor, updateStrip = { updateStrip() })
    }

    override fun onPause() {
        super.onPause()
        actionEditor.save()
    }

    class LayerAdapter(val actionEditor: ActionEditor, val updateStrip: () -> Unit) :
            Adapter<LayerViewHolder>() {

        override fun getItemViewType(position: Int): Int {
            return actionEditor.layers()[position].id()
        }

        override fun onCreateViewHolder(parent: ViewGroup,  viewType: Int): LayerViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            return when (viewType) {
                ColorLayer::class.java.canonicalName.hashCode() -> {
                    ColorLayerViewHolder(inflater.inflate(R.layout.item_color_layer, parent, false))
                }
                SpotLayer::class.java.canonicalName.hashCode() -> {
                    SpotLayerViewHolder(inflater.inflate(R.layout.item_color_spot_layer, parent, false))
                }
                SlideAnimationLayer::class.java.canonicalName.hashCode() -> {
                    SlideLayerViewHolder(inflater.inflate(R.layout.item_slide_layer, parent, false))
                }
                else -> {
                    LayerViewHolder(inflater.inflate(R.layout.item_layer, parent, false))
                }
            }
        }

        override fun onBindViewHolder(holder: LayerViewHolder, position: Int) {
            val item = actionEditor.layers()[position]
            holder.bind(item, position, this)
        }

        override fun getItemCount() = actionEditor.layers().size
    }




}