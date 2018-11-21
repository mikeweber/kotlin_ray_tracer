package com.weberapps.ray.tracer.math

class Ray(val origin: Point, val direction: Vector) {
  constructor(originTuple: Tuple, directionTuple: Tuple) : this(
    Point(originTuple),
    Vector(directionTuple)
  )

  fun positionAt(t: Float): Point {
    return Point(origin + direction * t)
  }

  fun transform(transformation: Matrix): Ray {
    return Ray(
      transformation * origin,
      transformation * direction
    )
  }
}