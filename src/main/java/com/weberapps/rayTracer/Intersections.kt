package com.weberapps.rayTracer

class Intersections(override val size: Int = 0, private val elements: Array<Intersection> = arrayOf()) : Collection<Intersection> {
    fun add(intersection: Intersection): Intersections {
        return Intersections(size + 1, elements + arrayListOf(intersection))
    }

    operator fun get(index: Int): Intersection {
        return elements[index]
    }

    override fun isEmpty(): Boolean {
        return size == 0
    }

    override fun iterator(): Iterator<Intersection> {
        return elements.iterator()
    }

    override fun containsAll(elements: Collection<Intersection>): Boolean {
        elements.forEach { if (!contains(it)) return false }

        return true
    }

    override fun contains(element: Intersection): Boolean {
        this.forEach { if (it == element) return true }

        return false
    }

    fun hit(): Intersection? {
        var closest: Intersection? = null
        elements.forEach {
            if (it.t >= 0 && it.t < (closest?.t ?: Float.POSITIVE_INFINITY)) {
                closest = it
            }
        }
        return closest
    }
}
