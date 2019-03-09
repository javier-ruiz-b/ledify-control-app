package com.javier.ledifycontrol.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import com.javier.ledifycontrol.R
import com.javier.ledifycontrol.layers.Layer
import com.javier.ledifycontrol.layers.ColorLayer
import com.javier.ledifycontrol.layers.FadeToLayer
import com.javier.ledifycontrol.model.LedifyInterpolator
import com.javier.ledifycontrol.model.RgbwColor
import kotlinx.android.synthetic.main.item_layer.view.*

class LayersActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_layers)

        var layers = ArrayList<Layer>()
        layers.add(ColorLayer(RgbwColor(255, 150, 50, 0)))
        layers.add(ColorLayer(RgbwColor(5, 15, 100, 1)))
        layers.add(FadeToLayer(layers[0], 1, LedifyInterpolator.Accelerate))

        viewManager = LinearLayoutManager(this)
        viewAdapter = LayerAdapter(layers)

        recyclerView = findViewById<RecyclerView>(R.id.rv).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter

        }
    }

    class LayerAdapter(private val layers: ArrayList<Layer>) :
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