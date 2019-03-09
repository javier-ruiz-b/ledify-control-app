package com.javier.ledifycontrol.acitivity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import com.javier.ledifycontrol.R
import com.javier.ledifycontrol.code.action.ActionEditor
import com.javier.ledifycontrol.code.layer.Layer
import kotlinx.android.synthetic.main.item_layer.view.*

class ActionEditorActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_action_editor)

        val actionName = intent.getStringExtra("actionName")
        val actionEditor = ActionEditor(filesDir, actionName)

        viewManager = LinearLayoutManager(this)
        viewAdapter = LayerAdapter(actionEditor.layers())

        recyclerView = findViewById<RecyclerView>(R.id.rv).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }
    }

    class LayerAdapter(private val layers: List<Layer>) :
            RecyclerView.Adapter<LayerAdapter.MyViewHolder>() {

        class MyViewHolder(view: LinearLayout) : RecyclerView.ViewHolder(view) {
            val ivIcon = view.ivIcon!!
            val tvName = view.tvName!!
        }

        override fun onCreateViewHolder(parent: ViewGroup,
                                        viewType: Int): LayerAdapter.MyViewHolder {
            val ll = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_layer, parent, false) as LinearLayout

            return MyViewHolder(ll)
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            val item = layers[position]
            holder.tvName.text = item.toString().replace('+', '\n')
            holder.ivIcon.setImageResource(item.getIcon())
            holder.ivIcon.setColorFilter(item.getTint())
        }

        override fun getItemCount() = layers.size
    }

}