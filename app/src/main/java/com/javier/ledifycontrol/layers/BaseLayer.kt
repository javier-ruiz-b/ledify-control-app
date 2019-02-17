package com.javier.ledifycontrol.layers

import mu.KotlinLogging
import java.lang.Exception

open class BaseLayer {

    val myIndex = availableIndex()

    override fun toString() : String {
        throw Exception("BaseLayer has no implementation")
    }

    public fun setLayerString() : String {
            return "${toString()}+SET=${myIndex}"
    }

    companion object {
        private val logger = KotlinLogging.logger {}
        private val indices: MutableSet<Int> = mutableSetOf()
        fun availableIndex() : Int {
            var value = 1
            if (!indices.isEmpty()) {
                value = indices.last() + 1
            }
            indices.add(value)
            return value
        }
        public fun freeIndex(index:Int) {
            if (!indices.remove(index)) {
                logger.warn { "Index $index not found!" }
            }
        }
    }

}