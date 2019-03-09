package com.javier.ledifycontrol.layers

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import com.javier.ledifycontrol.model.Action
import mu.KotlinLogging
import java.io.File
import java.lang.Exception

class LayersCoordinator(filesDir: File, name: String) {
    private val logger = KotlinLogging.logger {}
    private val file: File = File(filesDir.path + File.separator + name + ".json")
    private val objectMapper = ObjectMapper().registerModule(KotlinModule())
    private val action : Action

    init {
        action = if (file.exists()) {
            try {
                objectMapper.readValue<Action>(file)
            } catch (e: Exception) {
                logger.error { e }
                Action(name, ArrayList())
            }
        } else{
            Action(name, ArrayList())
        }
        logger.info { "Loaded $action from $file" }
    }

    fun layers() : List<Layer> {
        return action.layers
    }

    var actionName: String
        get() { return action.name }
        set(value) {action.name = value}

    fun deleteFile() {
        file.delete()
    }

    fun save() {
        objectMapper.writeValue(file, action)
        logger.info { "Saved $action in $file" }
    }

    fun add(layer: Layer) {
        action.layers.add(layer)
    }

}