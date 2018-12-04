import com.weberapps.ray.tracer.intersection.Intersection
import com.weberapps.ray.tracer.intersection.Intersections
import com.weberapps.ray.tracer.shape.*
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.junit.jupiter.api.Assertions.*

object CSGShapeSpec: Spek({
  describe("when initializing") {
    it("should be able to Union two shapes") {
      val s1 = Sphere()
      val s2 = Cube()
      val c = CSGUnion(s1, s2)

      assertEquals(s1, c.left)
      assertEquals(s2, c.right)
      assertEquals(c, s1.parent)
      assertEquals(c, s2.parent)
    }

    it("should be able to Intersect two shapes") {
      val s1 = Sphere()
      val s2 = Cube()
      val c = CSGIntersect(s1, s2)

      assertEquals(s1, c.left)
      assertEquals(s2, c.right)
      assertEquals(c, s1.parent)
      assertEquals(c, s2.parent)
    }

    it("should be able to Difference two shapes") {
      val s1 = Sphere()
      val s2 = Cube()
      val c = CSGDifference(s1, s2)

      assertEquals(s1, c.left)
      assertEquals(s2, c.right)
      assertEquals(c, s1.parent)
      assertEquals(c, s2.parent)
    }
  }

  val truthTable = arrayListOf(
    Triple( true,  true,  true),
    Triple( true,  true, false),
    Triple( true, false,  true),
    Triple( true, false, false),
    Triple(false,  true,  true),
    Triple(false,  true, false),
    Triple(false, false,  true),
    Triple(false, false, false)
  )
  describe("when evaluating the Union operator") {
    it("should implement the input/output table combinations") {
      val expectations = arrayListOf(
        false,
        true,
        false,
        true,
        false,
        false,
        true,
        true
      )
      val c = CSGUnion(Sphere(), Sphere())

      for ((index, expectation) in expectations.withIndex()) {
        val facts = truthTable[index]
        assertEquals(expectation, c.intersectionAllowed(facts.first, facts.second, facts.third))
      }
    }
  }

  describe("when evaluating the Intersection operator") {
    it("should implement the input/output table combinations") {
      val expectations = arrayListOf(
        true,
        false,
        true,
        false,
        true,
        true,
        false,
        false
      )
      val c = CSGIntersect(Sphere(), Sphere())

      for ((index, expectation) in expectations.withIndex()) {
        val facts = truthTable[index]
        assertEquals(expectation, c.intersectionAllowed(facts.first, facts.second, facts.third))
      }
    }
  }

  describe("when evaluating the Difference operator") {
    it("should implement the input/output table combinations") {
      val expectations = arrayListOf(
        false,
        true,
        false,
        true,
        true,
        true,
        false,
        false
      )
      val c = CSGDifference(Sphere(), Sphere())

      for ((index, expectation) in expectations.withIndex()) {
        val facts = truthTable[index]
        assertEquals(expectation, c.intersectionAllowed(facts.first, facts.second, facts.third))
      }
    }
  }

  describe("when checking if a CSGOperation includes another operation") {
    val union = CSGUnion(Sphere(), Cylinder())
    val intersection = CSGIntersect(Sphere(), Cylinder())
    val difference = CSGDifference(Cube(), Cone())

    it("should be true when comparing to itself") {
      assertTrue(union.includes(union))
      assertFalse(union.includes(intersection))
      assertFalse(union.includes(difference))

      assertFalse(intersection.includes(union))
      assertTrue(intersection.includes(intersection))
      assertFalse(intersection.includes(difference))

      assertFalse(difference.includes(union))
      assertFalse(difference.includes(intersection))
      assertTrue(difference.includes(difference))
    }

    it("should be true when a child operation is included") {
      val parent = CSGUnion(union, intersection)
      val grandParent = CSGIntersect(parent, difference)

      assertFalse(parent.includes(grandParent))
      assertTrue(parent.includes(parent))
      assertTrue(parent.includes(union))
      assertTrue(parent.includes(intersection))
      assertFalse(parent.includes(difference))

      assertTrue(grandParent.includes(grandParent))
      assertTrue(grandParent.includes(parent))
      assertTrue(grandParent.includes(union))
      assertTrue(grandParent.includes(intersection))
      assertTrue(grandParent.includes(difference))
    }
  }

  describe("when filtering intersections") {
    it("should ignore intersections that aren't allowed") {
      val s1 = Sphere()
      val s2 = Cube()

      val c1 = CSGUnion(s1, s2)
      val c2 = CSGIntersect(s1, s2)
      val c3 = CSGDifference(s1, s2)

      val xs = Intersections(
        Intersection(1f, s1),
        Intersection(2f, s2),
        Intersection(3f, s1),
        Intersection(4f, s2)
      )

      val result1 = c1.filterIntersections(xs)
      assertEquals(2, result1.size)
      assertEquals(xs[0], result1[0])
      assertEquals(xs[3], result1[1])

      val result2 = c2.filterIntersections(xs)
      assertEquals(2, result2.size)
      assertEquals(xs[1], result2[0])
      assertEquals(xs[2], result2[1])

      val result3 = c3.filterIntersections(xs)
      assertEquals(2, result3.size)
      assertEquals(xs[0], result3[0])
      assertEquals(xs[1], result3[1])
    }
  }
})