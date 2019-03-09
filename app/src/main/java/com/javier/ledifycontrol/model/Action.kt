package com.javier.ledifycontrol.model

import com.javier.ledifycontrol.layers.Layer

class Action(var name: String, val layers: ArrayList<Layer>) {
    override fun toString(): String {
        return "Action $name. LayersCount: ${layers.count()}"
    }
}