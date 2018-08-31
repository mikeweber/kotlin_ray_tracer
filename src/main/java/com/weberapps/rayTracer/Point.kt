package com.weberapps.rayTracer

class Point : Tuple {
    constructor(x: Float, y: Float, z: Float) : super(x, y, z, 1.0f)
    constructor(tuple: Tuple) : this(tuple.x, tuple.y, tuple.z)
}