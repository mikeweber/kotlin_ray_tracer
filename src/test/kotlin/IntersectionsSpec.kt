import com.weberapps.rayTracer.*
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue

object IntersectionsSpec : Spek({
  val sphere = Sphere()

  context("Aggregating intersections") {
    it("should store intersections") {
      val i1 = Intersection(1f, sphere)
      val i2 = Intersection(2f, sphere)
      var intersections = Intersections()
      intersections = intersections.add(i1)
      intersections = intersections.add(i2)
      assertEquals(2, intersections.size)
      assertEquals(1f, intersections[0].t)
      assertEquals(2f, intersections[1].t)
    }
  }

  context("getting hits") {
    it("should return nothing when there are no intersections") {
      val intersections = Intersections()
      assertEquals(null, intersections.hit())
    }

    it("should return nothing when there are no positive intersections") {
      val i1 = Intersection(-1f, sphere)
      val intersections = Intersections().add(i1)
      assertEquals(null, intersections.hit())
    }

    it("should return the first intersection") {
      val i1 = Intersection(1f, sphere)
      val intersections = Intersections().add(i1)
      assertEquals(i1, intersections.hit())
    }

    it("should return the first non-negative intersection") {
      val i1 = Intersection(-1f, sphere)
      val i2 = Intersection(10f, sphere)
      val i3 = Intersection(1f, sphere)
      val intersections = Intersections().add(i1).add(i2).add(i3)
      assertEquals(i3, intersections.hit())
    }
  }
})