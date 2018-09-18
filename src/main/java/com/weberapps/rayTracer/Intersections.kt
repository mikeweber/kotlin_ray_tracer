package com.weberapps.rayTracer

class Intersections(override var size: Int = 0, private val elements: ArrayList<Intersection> = arrayListOf()) : Collection<Intersection> {
    fun add(intersections: Intersections): Intersections {
        for (i in intersections) { add(i) }

        return this
    }

    fun add(intersection: Intersection): Intersections {
        size++
        elements.add(intersection)
        elements.sort()

        return this
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

    fun hit(includeTransparentMaterial: Boolean = true): Intersection? {
        var closest: Intersection? = null
        elements.forEach {
            if (it.t >= 0 && it.t < (closest?.t ?: Float.POSITIVE_INFINITY)) {
                closest = it
            }
        }
        return closest
    }
}
