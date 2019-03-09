package com.javier.ledifycontrol.code.action

import android.transition.Slide
import com.javier.ledifycontrol.code.layer.*
import com.javier.ledifycontrol.code.model.LedifyInterpolator
import com.javier.ledifycontrol.code.model.RgbwColor
import java.io.File

class DefaultActions {
    companion object{
        fun createIfNotExist(filesDir: File) {
            if (!ActionEditor.fileExists(filesDir,AvailableActions.Off.fileName)) {
//                ActionEditor.filePath(filesDir,AvailableActions.Off.fileName).delete()
                val editor = ActionEditor(filesDir,AvailableActions.Off.fileName)
                editor.add(ColorLayer(RgbwColor(0,0,0,0)))
                editor.add(FadeToLayer(editor.layers()[0],LedifyInterpolator.Decelerate,0,1500))
                editor.save()
            }
//            if (!ActionEditor.fileExists(filesDir,AvailableActions.On.fileName)) {
                ActionEditor.filePath(filesDir,AvailableActions.On.fileName).delete()
                val red = SpotLayer(RgbwColor(200,0,0,0),150f,20f, LedifyInterpolator.Accelerate)
                val green = SpotLayer(RgbwColor(0,200,0,0),150f,20f, LedifyInterpolator.Accelerate)
                val blue = SpotLayer(RgbwColor(0,0,200,0),150f,20f, LedifyInterpolator.Accelerate)
                val white = SpotLayer(RgbwColor(203,80,1,203),150f,10f, LedifyInterpolator.Accelerate)
                val spot = SpotLayer(RgbwColor(203,80,1,203),150f,150f, LedifyInterpolator.Accelerate)

                val redAnimation = SlideAnimationLayer(red,0.12f)
                val redAnimation2 = SlideAnimationLayer(red,-0.12f)
                val greenAnimation = SlideAnimationLayer(green,-0.25f)
                val blueAnimation = SlideAnimationLayer(blue,0.25f)
                val whiteAnimation = SlideAnimationLayer(white,-0.085f)
                val whiteAnimation2 = SlideAnimationLayer(white,0.085f)

                val allLayers = AdditionLayer(arrayListOf(redAnimation, redAnimation2, greenAnimation, blueAnimation, whiteAnimation, whiteAnimation2))

                val fadeStart = FadeToLayer(allLayers,LedifyInterpolator.Accelerate,0,1000)
                val fadeEnd = FadeToLayer(allLayers,LedifyInterpolator.Accelerate,0,1000)

                val editor = ActionEditor(filesDir,AvailableActions.On.fileName)
                editor.add(arrayListOf(red,green,blue,white,spot,redAnimation,redAnimation2,greenAnimation,blueAnimation,whiteAnimation,whiteAnimation2,allLayers,fadeStart,fadeEnd))
                editor.save()
//            }
        }

    }
}