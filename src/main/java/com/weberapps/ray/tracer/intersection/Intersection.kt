package com.weberapps.ray.tracer.intersection

import com.weberapps.ray.tracer.math.Light
import com.weberapps.ray.tracer.constants.EPSILON
import com.weberapps.ray.tracer.constants.TAU
import com.weberapps.ray.tracer.material.AIR
import com.weberapps.ray.tracer.math.Point
import com.weberapps.ray.tracer.math.Ray
import com.weberapps.ray.tracer.math.Vector
import com.weberapps.ray.tracer.math.Color
import com.weberapps.ray.tracer.renderer.World
import com.weberapps.ray.tracer.shape.MaterializedShape
import com.weberapps.ray.tracer.shape.Shape

class Intersection(
  val t: Float,
  val shape: Shape,
  var inside: Boolean = false,
  var point: Point = Point(0f, 0f, 0f),
  var eyeVector: Vector = Vector(0f, 0f, 1f),
  var normalVector: Vector = Vector(0f, 0f, 1f),
  var reflectVector: Vector = Vector(0f, 0f, -1f),
  var n1: Float = 1f,
  var n2: Float = 1f,
  var underPoint: Point = Point(0f, 0f, 0f),
  val u: Float? = null,
  val v: Float? = null
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
    return (shape as MaterializedShape).material.lighting(this, light, world, isShadowed(light, world), refractionsLeft)
  }

  fun isShadowed(light: Light, world: World?): Boolean {
    if (world == null) return false

    val v = light.position - point
    val dir = v.normalize()

    val ray = Ray(point, dir)
    val hit = world.intersect(ray).hit(includeTransparentMaterial = false) ?: return false

    return hit.t < v.magnitude
  }

  fun prepareHit(ray: Ray, intersections: Intersections = Intersections(), surfaceOffset: Float = 0.0001f): Intersection {
    val hitPoint = ray.positionAt(t)
    val eyeVector = -ray.direction
    var normalVector = shape.normal(hitPoint, this)
    val reflectVector = reflect(ray.direction, normalVector)
    val inside = normalVector.dot(eyeVector) < 0f
    if (inside) normalVector = -normalVector
    val point = Point(hitPoint + normalVector * surfaceOffset)
    val underPoint = Point(hitPoint - normalVector * surfaceOffset)
    val (n1, n2) = determineRefractiveIndices(intersections)

    return Intersection(
      t,
      shape,
      inside,
      point,
      eyeVector,
      normalVector,
      reflectVector,
      n1,
      n2,
      underPoint,
      u = this.u,
      v = this.v
    )
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

    val r0 = Math.pow(((n1 - n2) / (n1 + n2)).toDouble(), 2.0)
    return (r0 + (1.0 - r0) * Math.pow((1 - cos), 5.0)).toFloat()
  }

  private fun determineRefractiveIndices(intersections: Intersections, defaultRefractiveIndex: Float = AIR): Pair<Float, Float> {
    val containers = ArrayList<Shape>()
    var newN1 = n1
    var newN2 = n2

    for (intersection in intersections) {
      if (intersection == this) {
        newN1 =
          if (containers.isEmpty()) {
            defaultRefractiveIndex
          } else {
            (containers.last() as MaterializedShape).material.refractiveIndex
          }
      }

      if (containers.indexOf(intersection.shape) < 0) {
        containers.add(intersection.shape)
      } else {
        containers.remove(intersection.shape)
      }

      if (intersection == this) {
        newN2 =
          if (containers.isEmpty()) {
            defaultRefractiveIndex
          } else {
            (containers.last() as MaterializedShape).material.refractiveIndex
          }
        break
      }
    }

    return Pair(newN1, newN2)
  }

  private fun reflect(direction: Vector, normal: Vector): Vector {
    return direction - normal * direction.dot(normal) * 2f
  }

  private fun closeEnough(f1: Float, f2: Float, eps: Float = EPSILON): Boolean {
    return (Math.abs(f1 - f2) < eps)
  }
}
