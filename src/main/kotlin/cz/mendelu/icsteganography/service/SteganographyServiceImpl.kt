package cz.mendelu.icsteganography.service

import cz.mendelu.icsteganography.dto.FindRequest
import cz.mendelu.icsteganography.dto.FindResponse
import cz.mendelu.icsteganography.dto.HideRequest
import cz.mendelu.icsteganography.dto.HideResponse
import cz.mendelu.icsteganography.stego.GifImage
import cz.mendelu.icsteganography.stego.GifUtil
import org.springframework.stereotype.Service
import kotlin.experimental.and
import kotlin.experimental.or
import kotlin.math.log2

@Service
class SteganographyServiceImpl : SteganographyService {

    override fun hide(hideRequest: HideRequest): HideResponse {
        val originalGif = GifImage(hideRequest.gif!!.bytes)
        val text = hideRequest.text!!

        val textBytes = text.toByteArray()
        val lsbCount = 1
        val clearMask = 0xFE.toByte()

        println(textBytes.joinToString(separator = "") { "%02x".format(it) })
        println(textBytes.size)
        println("ITERATING...")

        var mask = 1
        var textIndex = 0
        val resultBytes = originalGif.bytes.copyOf().mapIndexed { i, byte ->
            if (i in GifUtil.GCT_START until GifUtil.calcGctEnd(originalGif.globalColorTableSize)) {
                // it is a color table byte, do the modification
                if (textIndex <= textBytes.lastIndex) {
                    val bitPosition = log2(mask.toDouble()).toInt()
                    val bit = ((textBytes[textIndex] and mask.toByte()).toInt() shr bitPosition).toByte()
                    println("Inside %02x with mask $mask, value is $bit".format(textBytes[textIndex]))
                    val result = (byte and clearMask) or bit
                    mask = if (mask == 128) {
                        textIndex++
                        1
                    } else {
                        mask shl 1
                    }
                    return@mapIndexed result
                } else {
                    // if we wrote the whole message, just clear incoming bits
                    byte and clearMask
                }
            } else {
                // it is not a color table byte, so do not modify it
                byte
            }
        }.toByteArray()
        val resultGif = GifImage(resultBytes)

        return HideResponse(originalGif, resultGif)
    }

    override fun find(findRequest: FindRequest): FindResponse {
        val gifImage = GifImage(findRequest.gif!!.bytes)

        val textByteList = ArrayList<Byte>()
        // bits of each byte are reversed, so we use stack
        val stack = ArrayDeque<Byte>()
        gifImage.globalColorTableBytes.forEach {
            val bit = it and 1
            stack.addLast(bit)

            if (stack.size == 8) {
                var byte = 0
                for (i in 0 until 8) {
                    // turn those 8 bits from a stack into a byte
                    byte = byte shl 1
                    byte = byte or stack.removeLast().toInt()
                }
                if (byte == 0) {
                    // end of message, no need to read further
                    return@forEach
                }

                textByteList.add(byte.toByte())
            }
        }

        val hiddenText = String(textByteList.toByteArray())
        println("Found byte count ${textByteList.toByteArray().size}")
        println("Found bytes ${textByteList.toByteArray().joinToString(separator = ""){"%02x".format(it)}}")
        println("Found text $hiddenText")

        return FindResponse(hiddenText)
    }
}