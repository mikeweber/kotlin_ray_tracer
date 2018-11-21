import com.weberapps.ray.tracer.*
import com.weberapps.ray.tracer.constants.TAU
import com.weberapps.ray.tracer.intersection.Intersection
import com.weberapps.ray.tracer.intersection.Intersections
import com.weberapps.ray.tracer.material.Material
import com.weberapps.ray.tracer.math.Point
import com.weberapps.ray.tracer.math.Ray
import com.weberapps.ray.tracer.math.Transformation
import com.weberapps.ray.tracer.math.Vector
import com.weberapps.ray.tracer.math.Color
import com.weberapps.ray.tracer.math.Light
import com.weberapps.ray.tracer.renderer.World
import com.weberapps.ray.tracer.shape.Plane
import com.weberapps.ray.tracer.shape.Sphere
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.*
import org.junit.jupiter.api.Assertions.*

object WorldSpec: Spek({
  context("initialization") {
    it("should be empty") {
      val world = World()
      assertEquals(0, world.sceneObjects.size)
      assertEquals(0, world.lightSources.size)
    }
  }

  context("defaultWorld") {
    val world = World.default()

    it("should have two objects and a single light source") {
      val s1 = Sphere(
        material = Material(
          color = Color(0.8f, 1f, 0.6f),
          diffuse = 0.7f,
          specular = 0.2f
        )
      )
      val s2 = Sphere(transform = Transformation.scale(0.5f, 0.5f, 0.5f))
      val light = Light(Point(-10f, 10f, -10f), Color.WHITE)

      assertEquals(2, world.sceneObjects.size)
      assertEquals(1, world.lightSources.size)
      assertTrue(world.sceneObjects.contains(s1))
      assertTrue(world.sceneObjects.contains(s2))
      assertTrue(world.lightSources.contains(light))
    }
  }

  context("ray casting") {
    var world = World.default()
    beforeEachTest {
      world = World.default()
    }

    it("should intersect every object in a scene") {
      val ray = Ray(Point(0f, 0f, -5f), Vector(0f, 0f, 1f))
      val xs = world.intersect(ray)
      assertEquals(4, xs.size)
      assertEquals(  4f, xs[0].t)
      assertEquals(4.5f, xs[1].t)
      assertEquals(5.5f, xs[2].t)
      assertEquals(  6f, xs[3].t)
    }

    it("should shade an intersection") {
      val ray = Ray(Point(0f, 0f, -5f), Vector(0f, 0f, 1f))
      val shape = world.sceneObjects[0]
      val hit = Intersection(4f, shape)
      val comps = hit.prepareHit(ray)
      val c = world.shadeHit(comps)

      assertEquals(Color(0.38066f, 0.47583f, 0.2855f), c)
    }

    it("should shade an intersection from the inside") {
      world.lightSources[0] = Light(Point(0f, 0.25f, 0f))
      val ray = Ray(Point(0f, 0f, 0f), Vector(0f, 0f, 1f))
      val shape = world.sceneObjects[1]
      val hit = Intersection(0.5f, shape)
      val comps = hit.prepareHit(ray)
      val c = world.shadeHit(comps)

      assertEquals(Color(0.90495f, 0.90495f, 0.90495f), c)
    }

    it("should return black when a ray misses") {
      val ray = Ray(Point(0f, 0f, -5f), Vector(0f, 1f, 0f))
      val c = world.colorAt(ray)
      assertEquals(Color.BLACK, c)
    }

    it("should return a color when the ray hits") {
      val ray = Ray(Point(0f, 0f, -5f), Vector(0f, 0f, 1f))
      val c = world.colorAt(ray)
      assertEquals(Color(0.38066f, 0.47583f, 0.2855f), c)
    }
  }

  context("when shadeHit is given an intersection in a shadow") {
    it("should only return the ambient color") {
      val light = Light(Point(0f, 0f, -10f))
      val s1 = Sphere()
      val s2Transform = Transformation.translation(0f, 0f, 10f)
      val s2 = Sphere(transform = s2Transform)
      val world =
        World(sceneObjects = arrayListOf(s1, s2), lightSources = arrayListOf(light))
      val ray = Ray(Point(0f, 0f, 5f), Vector(0f, 0f, 1f))
      val hit = Intersection(4f, s2)
      val comps = hit.prepareHit(ray)
      val color = world.shadeHit(comps)

      assertEquals(Color(0.1f, 0.1f, 0.1f), color)
    }
  }

  context("when shadeHit is a reflective surface") {
    it("should return a mix of reflected and material color") {
      val world = World.default()
      val moveDown = Transformation.translation(0f, -1f, 0f)
      val shape = Plane(transform = moveDown, material = Material(reflective = 0.5f))
      val f = (Math.sqrt(2.0) / 2.0).toFloat()
      val ray = Ray(Point(0f, 0f, -3f), Vector(0f, -f, f))
      val hit = Intersection(Math.sqrt(2.0).toFloat(), shape)
      val color = world.shadeHit(hit.prepareHit(ray))
      assertEquals(Color(0.87677f, 0.92436f, 0.82918f), color)
    }

    it("should return the shadeHit from the reflection's viewpoint") {
      val glass = Material(
        color = Color.BLACK,
        reflective = 1f,
        ambient = 0f,
        specular = 0f,
        diffuse = 0f,
        shininess = 0
      )
      val wallTransform = Transformation.rotateX(TAU / 4)
      val wall = Plane(material = glass, transform = wallTransform)
      val sphereTransform = Transformation.translation(2f, 0f, -3f)
      val sphereColor =
        Material(ambient = 1f, specular = 0f, diffuse = 0f, shininess = 0)
      val sphere = Sphere(transform = sphereTransform, material = sphereColor)
      val light = Light(Point(2f, 0f, -1f))
      val world = World(arrayListOf(wall, sphere), arrayListOf(light))

      val directRay = Ray(Point(2f, 0f, -4f), Vector(0f, 0f, 1f))
      val missRay   = Ray(Point(0f, 0f, -2f), Vector(0f, 0f, 1f))
      val hitRay    = Ray(Point(-2f, 0f, -2f), Vector(1f, 0f, 1f).normalize())

      val directHit = world.intersect(directRay).hit() ?: fail("Should be pointed directly at sphere")
      val miss    = world.intersect(missRay).hit()   ?: fail("Expected a hit against the glass")
      val hit     = world.intersect(hitRay).hit()  ?: fail("Expected a hit against the glass")

      assertEquals(Color.WHITE, world.shadeHit(directHit))
      val c = world.shadeHit(miss.prepareHit(missRay))
      assertEquals(Color.BLACK, c)
      assertEquals(Color.WHITE, world.shadeHit(hit.prepareHit(hitRay)))
    }

    it("should return the shadeHit from the reflection's viewpoint when the plane is angled") {
      val glass = Material(reflective = 1f, ambient = 0f)
      val wallTransform = Transformation.rotateY(-TAU / 8) * Transformation.rotateX(
        TAU / 4)
      val wall = Plane(material = glass, transform = wallTransform)
      val sphereTransform = Transformation.translation(3f, 0f, 0f)
      val sphereColor =
        Material(ambient = 1f, specular = 0f, diffuse = 0f, shininess = 0)
      val sphere = Sphere(transform = sphereTransform, material = sphereColor)
      val light = Light(Point(2f, 0f, 0f))
      val world = World(arrayListOf(wall, sphere), arrayListOf(light))

      val directRay = Ray(Point(1f, 0f, 0f), Vector(1f, 0f, 0f))
      val missRay   = Ray(Point(1f, 0f, 0f), Vector(-1f, 0f, 0f))
      val hitRay  = Ray(Point(0f, 0f, -2f), Vector(0f, 0f, 1f))

      val directHit = world.intersect(directRay).hit() ?: fail("Should be pointed directly at sphere")
      val miss    = world.intersect(missRay).hit()   ?: fail("Expected a hit against the glass")
      val hit     = world.intersect(hitRay).hit()  ?: fail("Expected a hit against the glass")

      assertEquals(Color.WHITE, world.shadeHit(directHit))
      assertEquals(Color.BLACK, world.shadeHit(miss.prepareHit(missRay)))
      assertEquals(Color.WHITE, world.shadeHit(hit.prepareHit(hitRay)))
    }

    it("should also work for semi-transparent material") {
      val world = World.default()
      val f = (Math.sqrt(2.0) / 2.0).toFloat()
      val r = Ray(Point(0f, 0f, -3f), Vector(0f, -f, f))
      val halfReflectiveTransparent =
        Material(reflective = 0.5f, transparency = 0.5f, refractiveIndex = 1.5f)
      val floor = Plane(
        transform = Transformation.translation(0f, -1f, 0f),
        material = halfReflectiveTransparent
      )
      val ball = Sphere(
        Transformation.translation(0f, -3.5f, -0.5f),
        Material(color = Color.RED, ambient = 0.5f)
      )

      world.sceneObjects.add(floor)
      world.sceneObjects.add(ball)

      val xs = Intersections(
        1,
        arrayListOf(Intersection(Math.sqrt(2.0).toFloat(), floor))
      )
      val comps = xs[0].prepareHit(r, xs)
      val color = world.shadeHit(comps, 5)
      assertEquals(Color(0.93391f, 0.69643f, 0.69243f), color)
    }
  }

  context("refracted color") {
    it("should return black when no opaque surface") {
      val world = World.default()
      val shape = world.sceneObjects.first()
      val r = Ray(Point(0f, 0f, -5f), Vector(0f, 0f, 1f))
      val xs = Intersections(
        2,
        arrayListOf(
          Intersection(4f, shape),
          Intersection(6f, shape)
        )
      )
      val comps = xs[0].prepareHit(r, xs)
      val c = shape.material.refractedColor(comps)
      assertEquals(Color.BLACK, c)
    }

    it("should return black when the max recursive depth is hit") {
      val world = World.default()
      val shape = world.sceneObjects.first()
      shape.material = Material(transparency = 1f, refractiveIndex = 1.5f)
      val r = Ray(Point(0f, 0f, -5f), Vector(0f, 0f, 1f))
      val xs = Intersections(
        2,
        arrayListOf(
          Intersection(4f, shape),
          Intersection(6f, shape)
        )
      )
      val comps = xs[0].prepareHit(r, xs)
      val c = shape.material.refractedColor(comps, world, 0)
      assertEquals(Color.BLACK, c)
    }

    it("should return black when under total internal reflection") {
      val world = World.default()
      val shape = world.sceneObjects.first()
      shape.material = Material(transparency = 1f, refractiveIndex = 1.5f)
      val f = (Math.sqrt(2.0) / 2.0).toFloat()
      val r = Ray(Point(0f, 0f, f), Vector(0f, 1f, 0f))
      val xs = Intersections(
        2,
        arrayListOf(
          Intersection(-f, shape),
          Intersection(f, shape)
        )
      )

      val comps = xs[1].prepareHit(r, xs)
      val c = shape.material.refractedColor(comps, world)
      assertEquals(Color.BLACK, c)
    }

    it("should spawn a second ray") {
      val world = World.default()
      val a = world.sceneObjects.first()
      val b = world.sceneObjects.last()
      a.material = TestMaterial(ambient = 1f)
      b.material = Material(transparency = 1f, refractiveIndex = 1.5f)
      val r = Ray(Point(0f, 0f, 0.1f), Vector(0f, 1f, 0f))
      val xs = Intersections(
        4,
        arrayListOf(
          Intersection(-0.9899f, a),
          Intersection(-0.4899f, b),
          Intersection(0.4899f, b),
          Intersection(0.9899f, a)
        )
      )
      val comps = xs[2].prepareHit(r, xs)
      val c = comps.shape.material.refractedColor(comps, world)
      assertEquals(Color(0f, 0.99878f, 0.04724f), c)
    }

    it("should reflect and refract") {
      val world = World.default()
      val floor = Plane(
        transform = Transformation.translation(0f, -1f, 0f),
        material = Material(transparency = 0.5f, refractiveIndex = 1.5f)
      )
      val ball = Sphere(
        transform = Transformation.translation(0f, -3.5f, -0.5f),
        material = Material(color = Color.RED, ambient = 0.5f)
      )
      world.sceneObjects.add(floor)
      world.sceneObjects.add(ball)
      val f = (Math.sqrt(2.0) / 2.0).toFloat()
      val r = Ray(Point(0f, 0f, -3f), Vector(0f, -f, f))
      val xs = Intersections(
        1,
        arrayListOf(Intersection(Math.sqrt(2.0).toFloat(), floor))
      )
      val comps = xs[0].prepareHit(r, xs)
      val color = world.shadeHit(comps)

      assertEquals(Color(0.93642f, 0.68642f, 0.68642f), color)
    }
  }
})