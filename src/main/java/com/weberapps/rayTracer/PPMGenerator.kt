package com.weberapps.rayTracer

class PPMGenerator(val canvas: Canvas) {
    fun generate(): String {
        val max = 255
        var output = """
            P3
            ${canvas.width} ${canvas.height}
            ${max}

        """.trimIndent()
        for (y in 0..(canvas.height - 1)) {
            for (x in 0..(canvas.width - 1)) {
                val pixel = canvas.getPixel(x, y)
                output += "${(max * pixel.red).toInt()} ${(max * pixel.green).toInt()} ${(max * pixel.blue).toInt()}"
                if (x < canvas.width - 1) output += " "
            }
            if (y < canvas.height - 1) output += "\n"
        }

        return output
    }
}