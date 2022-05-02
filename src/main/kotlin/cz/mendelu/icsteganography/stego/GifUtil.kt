package cz.mendelu.icsteganography.stego

object GifUtil {
    val GCT_START: Int = 13

    fun calcGctEnd(gctSize: Int): Int {

        return GCT_START + gctSize * 3
    }
}