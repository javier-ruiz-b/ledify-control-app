package com.javier.ledifycontrol.code.action

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import com.javier.ledifycontrol.code.layer.Layer
import mu.KotlinLogging
import java.io.File
import java.lang.Exception

class ActionEditor(filesDir: File, name: String) {
    private val logger = KotlinLogging.logger {}
    private val file: File = filePath(filesDir, name)
    private val objectMapper = ObjectMapper().registerModule(KotlinModule())
    private val action : Action

    companion object {
        fun fileExists(filesDir: File, name: String): Boolean{
            return filePath(filesDir, name).exists()
        }
        fun filePath(filesDir: File, name: String) : File {
            return File(filesDir.path + File.separator + name + ".json")
        }
    }

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

    val command: String
        get() {return layers().last().setLayerString()}

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
    fun add(layers: Collection<Layer>) {
        action.layers.addAll(layers)
    }

}