package com.javier.ledifycontrol

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import com.javier.ledifycontrol.layers.BaseLayer
import com.javier.ledifycontrol.layers.ColorLayer
import com.javier.ledifycontrol.layers.FadeToLayer
import com.javier.ledifycontrol.model.LedifyInterpolator
import com.javier.ledifycontrol.model.RgbwColor
import org.junit.After
import org.junit.Test

import java.io.File

class LayerSerializingTest {
    private val objectMapper = ObjectMapper().registerModule(KotlinModule())
    private val layersFile = File("layers.json")
    var layers = ArrayList<BaseLayer>()

    fun load() {
        layers = objectMapper.readValue(layersFile)
    }

    fun save() {
        createLayers()
        objectMapper.writeValue(layersFile, layers)
        System.out.println(objectMapper.writeValueAsString(layers))
    }

    private fun createLayers() {
        layers.add(ColorLayer(RgbwColor(255, 150, 50, 0)))
        layers.add(ColorLayer(RgbwColor(5, 15, 100, 1)))
        layers.add(FadeToLayer(layers[0], 1, LedifyInterpolator.Accelerate))
    }

    @Test
    fun saveAndLoadLayers() {
        save()
        load()
    }

    @After
    fun teardown() {
        layersFile.delete()
    }


}
