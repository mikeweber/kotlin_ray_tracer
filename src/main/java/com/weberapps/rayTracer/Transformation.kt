package com.weberapps.rayTracer

import java.lang.Math.cos
import java.lang.Math.sin

class Transformation {
    companion object {
        fun translation(x: Float, y: Float, z: Float): Matrix {
            val translation = Matrix.eye(4)
            translation.setElement(0, 3, x)
            translation.setElement(1, 3, y)
            translation.setElement(2, 3, z)

            return translation
        }

        fun scale(x: Float, y: Float, z: Float): Matrix {
            val scale = Matrix.eye(4)
            scale.setElement(0, 0, x)
            scale.setElement(1, 1, y)
            scale.setElement(2, 2, z)

            return scale
        }

        fun rotateX(rads: Double): Matrix {
            val rotation = Matrix.eye(4)
            rotation.setElement(1, 1,  cos(rads).toFloat())
            rotation.setElement(1, 2, -sin(rads).toFloat())
            rotation.setElement(2, 1,  sin(rads).toFloat())
            rotation.setElement(2, 2,  cos(rads).toFloat())

            return rotation
        }

        fun rotateY(rads: Double): Matrix {
            val rotation = Matrix.eye(4)
            rotation.setElement(0, 0,  cos(rads).toFloat())
            rotation.setElement(0, 2,  sin(rads).toFloat())
            rotation.setElement(2, 0, -sin(rads).toFloat())
            rotation.setElement(2, 2,  cos(rads).toFloat())

            return rotation
        }

        fun rotateZ(rads: Double): Matrix {
            val rotation = Matrix.eye(4)
            rotation.setElement(0, 0,  cos(rads).toFloat())
            rotation.setElement(0, 1, -sin(rads).toFloat())
            rotation.setElement(1, 0,  sin(rads).toFloat())
            rotation.setElement(1, 1,  cos(rads).toFloat())

            return rotation
        }
    }
}