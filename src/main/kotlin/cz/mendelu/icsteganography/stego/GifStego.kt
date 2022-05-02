package cz.mendelu.icsteganography.stego

import kotlin.experimental.and
import kotlin.experimental.or
import kotlin.math.pow

class GifStego(
    val gifImage: GifImage,
    val text: String
) {

    fun canHide(): Boolean {
        return true
    }

    fun hide(): GifImage {
        return GifImage(gifImage.bytes)
    }

}