package com.javier.ledifycontrol.layers

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import com.javier.ledifycontrol.model.LedifyInterpolator
import com.javier.ledifycontrol.model.RgbwColor
import mu.KotlinLogging
import java.io.File

class LayerBuilder(saveDir: String) {
    private val logger = KotlinLogging.logger {}
    private val objectMapper = ObjectMapper().registerModule(KotlinModule())
    var layers = ArrayList<BaseLayer>()

}