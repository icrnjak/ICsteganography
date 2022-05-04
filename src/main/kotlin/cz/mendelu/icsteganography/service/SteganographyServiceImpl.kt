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
        val clearMask = 0xFE.toByte()

        println(textBytes.joinToString(separator = "") { "%02x".format(it) })
        println(textBytes.size)
        println("ITERATING...")

        var mask = 1
        var textIndex = 0
        var emptyBitCount = 0;
        val resultBytes = originalGif.bytes.copyOf().mapIndexed { i, byte ->
            if (i in GifUtil.GCT_START until GifUtil.calcGctEnd(originalGif.globalColorTableSize)) {
                // it is a color table byte, do the modification
                if (textIndex <= textBytes.lastIndex) {
                    val maskZeroes = log2(mask.toDouble()).toInt()
                    val textByte = textBytes[textIndex]
                    val maskedTextBit = textByte.toInt() and mask
                    val textBit = (maskedTextBit shr maskZeroes).toByte()
                    println("Inside %02x with mask $mask, value is $textBit".format(textByte))
                    val result = (byte and clearMask) or textBit
                    mask = if (mask == 128) {
                        textIndex++
                        1
                    } else {
                        mask shl 1
                    }
                    return@mapIndexed result
                } else {
                    // if we wrote the whole message, clear 8 incoming bits, so we know where message ends
                    if (emptyBitCount < 8) {
                        println("Writing empty bit")
                        ++emptyBitCount
                        byte and clearMask
                    } else {
                        byte
                    }
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
        kotlin.run {
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
                    println("Read byte: %02x".format(byte))
                    if (byte == 0) {
                        println("End of message, found byte 0")
                        // end of message, no need to read further
                        return@run
                    }

                    textByteList.add(byte.toByte())
                }
            }
        }

        val hiddenText = String(textByteList.toByteArray())
        println("Found byte count ${textByteList.toByteArray().size}")
        println("Found bytes in hex ${textByteList.toByteArray().joinToString(separator = "") { "%02x".format(it) }}")
        println("Found text $hiddenText")

        return FindResponse(hiddenText)
    }
}