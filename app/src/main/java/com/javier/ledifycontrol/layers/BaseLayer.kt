package com.javier.ledifycontrol.layers

import java.lang.Exception

open class BaseLayer(open val toIndex: Int) {
    override fun toString() : String {
        throw Exception("BaseLayer has no implementation")
    }
}