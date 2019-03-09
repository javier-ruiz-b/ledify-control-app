package com.javier.ledifycontrol.code.action

import com.javier.ledifycontrol.code.layer.Layer

class Action(var name: String, val layers: ArrayList<Layer>) {
    override fun toString(): String {
        return "{Action $name. LayersCount: ${layers.count()}}"
    }
}