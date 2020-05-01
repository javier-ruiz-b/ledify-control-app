package com.javier.ledifycontrol.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.View.OnLongClickListener
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.javier.ledifycontrol.R
import com.javier.ledifycontrol.code.action.ActionEditor
import com.javier.ledifycontrol.code.layer.AdditionLayer
import com.javier.ledifycontrol.code.layer.ColorLayer
import com.javier.ledifycontrol.code.layer.Layer
import com.javier.ledifycontrol.code.model.RgbwColor
import com.javier.ledifycontrol.code.net.RestClient
import io.github.yavski.fabspeeddial.SimpleMenuListenerAdapter
import kotlinx.android.synthetic.main.activity_action_editor.*
import kotlinx.android.synthetic.main.item_layer.view.*


class ActionEditorActivity : AppCompatActivity() {

    private val restClient = RestClient()
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

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
            RecyclerView.Adapter<LayerAdapter.MyViewHolder>() {

        class MyViewHolder(val view: LinearLayout) : RecyclerView.ViewHolder(view) {
            val ivIcon = view.ivIcon!!
            val tvName = view.tvName!!
            var item : Layer? = null
        }

        override fun onCreateViewHolder(parent: ViewGroup,
                                        viewType: Int): LayerAdapter.MyViewHolder {
            val ll = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_layer, parent, false) as LinearLayout

            return MyViewHolder(ll)
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
//            holder.view.setOnClickListener(View.OnClickListener { mFragment.onItemClicked(position) })
            holder.view.setOnLongClickListener(OnLongClickListener {
                actionEditor.remove(holder.item!!)
                notifyItemRemoved(position)
                true
            })
            val item = actionEditor.layers()[position]
            holder.item = item
            holder.tvName.text = item.toString().replace('+', '\n')
            holder.ivIcon.setImageResource(item.getIcon())
            holder.ivIcon.setColorFilter(item.getTint())
        }
        override fun getItemCount() = actionEditor.layers().size

    }

}