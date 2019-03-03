package com.javier.ledifycontrol.layers

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonTypeInfo
import mu.KotlinLogging
import java.lang.Exception

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY)
open class BaseLayer {
    @JsonProperty("@class") val type = javaClass.canonicalName

    val myIndex = availableIndex()

    override fun toString() : String {
        throw Exception("BaseLayer has no implementation")
    }

    fun setLayerString() : String {
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
        fun freeIndex(index:Int) {
            if (!indices.remove(index)) {
                logger.warn { "Index $index not found!" }
            }
        }
    }

}