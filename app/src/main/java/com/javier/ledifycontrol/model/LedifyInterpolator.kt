package com.javier.ledifycontrol.model

enum class LedifyInterpolator(val value: Int) {
    Linear(1),
    Accelerate(10),
    Accelerate4x(11),
    Decelerate(20),
    Decelerate4x(21)
}
