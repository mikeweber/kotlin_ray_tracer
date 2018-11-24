import com.weberapps.ray.tracer.TestShape
import com.weberapps.ray.tracer.constants.TAU
import com.weberapps.ray.tracer.math.*
import com.weberapps.ray.tracer.shape.Group
import com.weberapps.ray.tracer.shape.Sphere
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.it
import org.junit.jupiter.api.Assertions.assertEquals

object GroupSpec: Spek({
  context("creating a group") {
    it("should be empty by default") {
      val group = Group()
      assertEquals(0, group.shapes.size)
    }

    it("should be able to add a shape to the group") {
      val group = Group()
      val shape = TestShape()
      group.add(shape)
      assertEquals(1, group.shapes.size)
      assertEquals(group, shape.parent)
    }
  }

  context("intersecting rays") {
    it("should return an empty list when the group is empty") {
      val group = Group()
      val ray = Ray(Point(0f, 0f, 0f), Vector(0f, 0f, 1f))
      assertEquals(0, group.localIntersect(ray).size)
    }

    it("should return intersections when a shape in the group is hit") {
      var group = Group()
      val s1 = Sphere()
      val s2 = Sphere(
        transform = Transformation.translation(0f, 0f, -3f)
      )
      val s3 = Sphere(
        transform = Transformation.translation(5f, 0f, 0f)
      )
      group = group.add(s1).add(s2).add(s3)
      val r = Ray(Point(0f, 0f, -5f), Vector(0f, 0f, 1f))
      val xs = group.localIntersect(r)
      assertEquals(4, xs.size)
      assertEquals(s2, xs[0].shape)
      assertEquals(s2, xs[1].shape)
      assertEquals(s1, xs[2].shape)
      assertEquals(s1, xs[3].shape)
    }

    it("should factor in the transformation of the group, as well as the child shape") {
      val group = Group(transform = Transformation.scale(2f, 2f, 2f))
      val sphere = Sphere(transform = Transformation.translation(5f, 0f, 0f))
      group.add(sphere)

      val ray = Ray(Point(10f, 0f, -10f), Vector(0f, 0f, 1f))
      val xs = group.intersect(ray)
      assertEquals(2, xs.size)
    }
  }

  context("worldToObjectSpace") {
    it("should recursively convert a point from world space to a child shape's object space") {
      val g1 = Group(transform = Transformation.rotateY(TAU / 4))
      val g2 = Group(transform = Transformation.scale(2f, 2f, 2f))
      g1.add(g2)
      val sphere = Sphere(transform = Transformation.translation(5f, 0f, 0f))
      g2.add(sphere)

      val p = sphere.worldToObject(Point(-2f, 0f, -10f))
      assertEquals(Point(0f, 0f, -1f), p)
    }
  }

  context("objectToWorldSpace") {
    it("should convert vectors from object space to world space") {
      val g1 = Group(transform = Transformation.rotateY(TAU / 4))
      val g2 = Group(transform = Transformation.scale(1f, 2f, 3f))
      g1.add(g2)
      val sphere = Sphere(transform = Transformation.translation(5f, 0f, 0f))
      g2.add(sphere)
      val n = sphere.normalToWorld(Vector(floatRoot(3f) / 3f, floatRoot(3f) / 3f, floatRoot(3f) / 3f))
      assertEquals(Vector(0.28571f, 0.42857f, -0.85714f), n)
    }
  }

  context("determining normal") {
    it("should find the normal on a child object") {
      val g1 = Group(transform = Transformation.rotateY(TAU / 4))
      val g2 = Group(transform = Transformation.scale(1f, 2f, 3f))
      g1.add(g2)
      val sphere = Sphere(transform = Transformation.translation(5f, 0f, 0f))
      g2.add(sphere)

      val n = sphere.normal(Point(1.7321f, 1.1547f, -5.5774f))
      assertEquals(Vector(0.2857f, 0.42854f, -0.85716f), n)
    }
  }
})