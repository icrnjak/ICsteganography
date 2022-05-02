package cz.mendelu.icsteganography.stego

import java.nio.ByteBuffer
import java.util.Collections
import kotlin.experimental.and
import kotlin.math.pow

class GifImage(
    // Image bytes
    bytes: ByteArray
) {
    var bytes: ByteArray
        get() = field.copyOf()
        private set
    val size: Int = bytes.size
    val version: String = String(bytes.copyOfRange(0, 6))
    val width: Int = ByteBuffer.wrap(bytes.copyOfRange(6, 8).reversedArray()).short.toInt()
    val height: Int = ByteBuffer.wrap(bytes.copyOfRange(8, 10).reversedArray()).short.toInt()
    val usesGlobalColorTable: Boolean = (bytes[10] and 0x80.toByte()) == 0x80.toByte()
    val globalColorTableBytes: ByteArray
    val globalColorTable: List<ColorRGB>
    val globalColorTableSize: Int

    init {
        this.bytes = bytes
        if (usesGlobalColorTable) {
            globalColorTableSize = 2.0.pow((bytes[10] and 0x07.toByte()) + 1).toInt()
            globalColorTableBytes = bytes.copyOfRange(13, 13 + globalColorTableSize * 3)
            val globalColorTableTemp: ArrayList<ColorRGB> = ArrayList()
            for (i in globalColorTableBytes.indices step 3) {
                val color = ColorRGB(globalColorTableBytes[i].toUByte(), globalColorTableBytes[i + 1].toUByte(), globalColorTableBytes[i + 2].toUByte())
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