package com.weberapps.rayTracer

class Intersection(
  val t: Float,
  val shape: Shape,
  var inside: Boolean = false,
  var point: Point = Point(0f, 0f, 0f),
  var eyeVector: Vector = Vector(0f, 0f, 1f),
  var normalVector: Vector = Vector(0f, 0f,1f),
  var rayVector: Vector = Vector(0f, 0f, 1f),
  var reflectVector: Vector = Vector(0f, 0f, -1f),
  var n1: Float = 1f,
  var n2: Float = 1f,
  var underPoint: Point = Point(0f, 0f, 0f)
): Comparable<Intersection> {
  override fun equals(other: Any?): Boolean {
    if (other !is Intersection) return false

    return shape == other.shape && closeEnough(t, other.t)
  }

  override operator fun compareTo(other: Intersection): Int {
    if (closeEnough(t, other.t)) return 0
    return if (t < other.t) -1 else 1
  }

  fun colorAt(light: Light, refractionsLeft: Int = 0, world: World? = null): Color? {
    return shape.material.lighting(this, light, world, isShadowed(light, world), refractionsLeft)
  }

  fun isShadowed(light: Light, world: World?): Boolean {
    if (world == null) return false

    val v = light.position - point
    val dist = v.magnitude()
    val dir = v.normalize()

    val ray = Ray(point, dir)
    val hit = world.intersect(ray).hit(includeTransparentMaterial = false) ?: return false

    return hit.t < dist
  }

  fun prepareHit(ray: Ray, intersections: Intersections = Intersections(), surfaceOffset: Float = 0.0001f): Intersection {
    val hitPoint = ray.positionAt(t)
    rayVector = ray.direction
    eyeVector = -ray.direction
    normalVector = shape.normal(hitPoint)
    reflectVector = reflect(rayVector, normalVector)
    inside = normalVector.dot(eyeVector) < 0f
    if (inside) normalVector = -normalVector
    point = Point(hitPoint + normalVector * surfaceOffset)
    underPoint = Point(hitPoint  - normalVector * surfaceOffset)
    determineRefractiveIndices(intersections)

    return this
  }

  fun schlick(): Float {
    var cos = eyeVector.dot(normalVector).toDouble()

    // total internal refraction only occurs if n1 is greater than n2
    if (n1 > n2) {
      val n = n1 / n2
      val sin2T = n * n * (1f - (cos * cos))
      if (sin2T > 1f) return 1f

      val cosT = Math.sqrt(1.0 - sin2T)
      cos = cosT
    }

    val r0 = Math.pow((n1 - n2) / (n1 + n2).toDouble(), 2.0)
    return (r0 + (1.0 - r0) * Math.pow((1 - cos), 5.0)).toFloat()
  }

  private fun determineRefractiveIndices(intersections: Intersections, defaultRefractiveIndex: Float = 1f) {
    val containers = ArrayList<Shape>()
    for (intersection in intersections) {
      if (intersection == this) {
        n1 = if (containers.isEmpty()) {
          defaultRefractiveIndex
        } else {
          containers.last().material.refractiveIndex
        }
      }

      if (containers.indexOf(intersection.shape) < 0) {
        containers.add(intersection.shape)
      } else {
        containers.remove(intersection.shape)
      }

      if (intersection == this) {
        n2 = if (containers.isEmpty()) {
          defaultRefractiveIndex
        } else {
          containers.last().material.refractiveIndex
        }
        break
      }
    }
  }

  private fun reflect(direction: Vector, normal: Vector): Vector {
    return direction - normal * direction.dot(normal) * 2f
  }

  private fun closeEnough(f1: Float, f2: Float, eps: Float = EPSILON): Boolean {
    return (Math.abs(f1 - f2) < eps)
  }
}