package cz.mendelu.icsteganography.dto

import cz.mendelu.icsteganography.stego.GifImage

data class HideResponse(
    var original: GifImage? = null,
    var result: GifImage? = null
)
