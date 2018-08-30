package com.weberapps.rayTracer

import java.io.File

class PPMGenerator(private val canvas: Canvas) {
    companion object {
        const val MAX_INTENSITY = 255
        const val MAX_LENGTH = 70
    }

    fun save(filename: String): Unit {
        val content = generate()
        val file = File(filename)
        return file.printWriter().use { out -> out.write(content) }
    }

    fun generate(): String {
        var output = """
            P3
            ${canvas.width} ${canvas.height}
            ${MAX_INTENSITY}

        """.trimIndent()

        for (y in 0..(canvas.height - 1)) {
            var lineLength = 0
            for (x in 0..(canvas.width - 1)) {
                val pixel = canvas.getPixel(x, y)
                val pixelOutput = "${floatToInt(pixel.red)} ${floatToInt(pixel.green)} ${floatToInt(pixel.blue)} "
                if ((lineLength + pixelOutput.length) > MAX_LENGTH) {
                    output = output.trimEnd() + "\n"
                    lineLength = 0
                }
                output += pixelOutput
                lineLength += pixelOutput.length
            }
            output = output.trimEnd() + "\n"
        }

        return output
    }

    private fun floatToInt(intensity: Float): Int {
        var intensity = if (intensity < 0f) 0f else intensity
        intensity = if (intensity > 1f) 1f else intensity

        return (intensity * MAX_INTENSITY).toInt()
    }
}