package com.javier.ledifycontrol

import com.javier.ledifycontrol.layers.ColorLayer
import com.javier.ledifycontrol.layers.FadeToLayer
import com.javier.ledifycontrol.layers.LayersCoordinator
import com.javier.ledifycontrol.model.LedifyInterpolator
import com.javier.ledifycontrol.model.RgbwColor
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File
import org.junit.Rule



class LayersCoordinatorTest {
    private lateinit var tested: LayersCoordinator

    @get:Rule
    val temporaryFolder = TemporaryFolder()

    private fun load() {
        tested = LayersCoordinator(temporaryFolder.root, "tested")
    }

    @Before
    fun setup() {
        load()
    }

    @After
    fun teardown() {
        tested.deleteFile()
    }

    @Test
    fun addOneLayer() {
        tested.add(ColorLayer(RgbwColor(255, 150, 50, 0)))

        Assert.assertEquals(1, tested.layers().count())
    }

    @Test
    fun savesAndLoadsName() {
        tested.actionName = "42"

        tested.save()
        load()

        Assert.assertEquals("42", tested.actionName)
    }

    @Test
    fun savesAndLoadsWithoutLayers() {
        tested.save()
        load()

        Assert.assertTrue(tested.layers().isEmpty())
    }

    @Test
    fun savesAndLoadsLayers() {
        tested.add(ColorLayer(RgbwColor(255, 150, 50, 0)))
        tested.add(ColorLayer(RgbwColor(5, 15, 100, 1)))
        tested.add(FadeToLayer(tested.layers()[0], 1, LedifyInterpolator.Accelerate))

        tested.save()
        load()

        Assert.assertEquals(3, tested.layers().count())
        Assert.assertEquals(RgbwColor(255, 150, 50, 0).toArgb(), tested.layers()[0].getTint())
    }

    @Test
    fun isEmpty() {
        Assert.assertTrue(tested.layers().isEmpty())
    }


}
