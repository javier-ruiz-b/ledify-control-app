package com.javier.ledifycontrol.code.layer

import android.graphics.Color
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.javier.ledifycontrol.R
import mu.KotlinLogging
import java.lang.Exception

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY)
open class Layer {
    @JsonProperty("@class") val type = javaClass.canonicalName

    val index = availableIndex()

    fun id() : Int {
        return type.hashCode()
    }

    override fun toString() : String {
        throw Exception("Layer has no implementation")
    }

    @JsonIgnore
    open fun getIcon() : Int {
        return R.drawable.ic_error_outline_48dp
    }

    @JsonIgnore
    open fun getTint() : Int {
        return Color.argb(255, 240,0,0)
    }

    fun setLayerString() : String {
        return "${toString()}+SET=$index"
    }

    companion object {
        private val logger = KotlinLogging.logger {}
        private val indices: MutableSet<Int> = mutableSetOf()
        fun availableIndex() : Int {
            var value = 1
            if (indices.isNotEmpty()) {
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