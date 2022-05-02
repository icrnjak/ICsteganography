package cz.mendelu.icsteganography.service

import cz.mendelu.icsteganography.dto.HideRequest
import cz.mendelu.icsteganography.dto.HideResponse
import cz.mendelu.icsteganography.stego.GifImage
import cz.mendelu.icsteganography.stego.GifStego
import org.springframework.stereotype.Service

@Service
class SteganographyServiceImpl : SteganographyService {

    override fun hide(hideRequest: HideRequest): HideResponse {
        if (hideRequest.gif?.bytes != null) {
            val originalGif = GifImage(hideRequest.gif!!.bytes)
            val gifStego = GifStego(originalGif, hideRequest.text!!)

            val resultGif = gifStego.hide()
            return HideResponse(originalGif, resultGif)
        }
        return HideResponse()
    }
}