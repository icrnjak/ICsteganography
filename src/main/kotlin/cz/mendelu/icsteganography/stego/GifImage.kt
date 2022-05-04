package cz.mendelu.icsteganography.stego

import java.nio.ByteBuffer
import java.util.Collections
import kotlin.experimental.and
import kotlin.math.pow

class GifImage(
    // Image bytes
    bytes: ByteArray
) {
    companion object {
        fun isGif(bytes: ByteArray): Boolean {
            return GifUtil.GIF_SIGNATURE == String(bytes.copyOfRange(0, 3))
        }
    }

    var bytes: ByteArray
        get() = field.copyOf()
        private set
    val size: Int = bytes.size
    val gifSignature: String = String(bytes.copyOfRange(0, 3))
    val version: String = String(bytes.copyOfRange(3, 6))
    val width: Int = ByteBuffer.wrap(bytes.copyOfRange(6, 8).reversedArray()).short.toInt()
    val height: Int = ByteBuffer.wrap(bytes.copyOfRange(8, 10).reversedArray()).short.toInt()
    val usesGlobalColorTable: Boolean = (bytes[10] and 0x80.toByte()) == 0x80.toByte()
    val globalColorTableBytes: ByteArray
    val globalColorTable: List<ColorRGB>
    val globalColorTableSize: Int

    init {
        this.bytes = bytes
        if (usesGlobalColorTable) {
            // power is the value of last 3 bits plus 1
            val colorTableSizePower = (bytes[10] and 0x07.toByte()) + 1
            globalColorTableSize = 2.0.pow(colorTableSizePower).toInt()
            globalColorTableBytes = bytes.copyOfRange(GifUtil.GCT_START, GifUtil.calcGctEnd(globalColorTableSize))
            val globalColorTableTemp: ArrayList<ColorRGB> = ArrayList()
            for (i in globalColorTableBytes.indices step 3) {
                val r = globalColorTableBytes[i]
                val g = globalColorTableBytes[i + 1]
                val b = globalColorTableBytes[i + 2]
                val color = ColorRGB(r,g,b)
                globalColorTableTemp.add(color)
            }
            globalColorTable = Collections.unmodifiableList(globalColorTableTemp)
        } else {
            globalColorTableSize = 0
            globalColorTableBytes = ByteArray(0)
            globalColorTable = listOf()
        }
    }

    override fun toString(): String {
        return "GifImage(size=$size, version='$version', width=$width, height=$height, usesGlobalColorTable=$usesGlobalColorTable, globalColorTable=$globalColorTable, globalColorTableSize=$globalColorTableSize)"
    }

}