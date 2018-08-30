package com.weberapps.rayTracer

fun drawClock() {
    val c = Canvas(100, 100, Color(1f, 0f, 0f))
    val p = Point(0f, 0f, 1f)
    val white = Color(1f, 1f, 1f)
    val black = Color(0f, 0f, 0f)

    for (i in 0..11) {
        val r = Transformation.rotateY(i * TAU / 12)
        val s = Transformation.scale(30f, 0f, 30f)
        val t = Transformation.translation(50f, 0f, 50f)
        val rotatedPoint = t * s * r * p
        val color = if (i == 0) black else white
        for (x in -2..2) {
            for (z in -2..2) {
                c.setPixel(rotatedPoint.x.toInt() + x, 100 - (rotatedPoint.z.toInt() + z), color)
            }
        }
    }
    PPMGenerator(c).save("/Users/mweber/clock.ppm")
}