package com.weberapps.rayTracer

import java.io.*

class PPMGenerator(private val canvas: Canvas) {
    companion object {
        const val MAX_INTENSITY = 255
        const val MAX_LENGTH = 70
    }

    fun save(filename: String) {
        val file = File(filename)
        return file.printWriter().use { out -> generate(out) }
    }

    fun generate(output: Writer = StringWriter()): Writer {
        output.write("P3\n")
        output.write("${canvas.width} ${canvas.height}\n")
        output.write("$MAX_INTENSITY\n")

        for (y in 0..(canvas.height - 1)) {
            var line = ""
            for (x in 0..(canvas.width - 1)) {
                val pixel = canvas.getPixel(x, y)
                val pixelOutput = "${floatToInt(pixel.red)} ${floatToInt(pixel.green)} ${floatToInt(pixel.blue)} "

                if ((line.length + pixelOutput.length) > MAX_LENGTH) {
                    output.write(line.trimEnd() + "\n")
                    line = ""
                }
                line += pixelOutput
            }
            output.write("${line.trimEnd()}\n")
        }

        return output
    }

    private fun floatToInt(intensity: Float): Int {
        val i = if (intensity < 0f) {
            0f
        } else if (intensity > 1f) {
            1f
        } else {
            intensity
        }

        return (i* MAX_INTENSITY).toInt()
    }
}