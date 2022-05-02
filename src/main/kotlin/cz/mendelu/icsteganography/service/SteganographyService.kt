package cz.mendelu.icsteganography.service

import cz.mendelu.icsteganography.dto.HideRequest
import cz.mendelu.icsteganography.dto.HideResponse

interface SteganographyService {

    fun hide(hideRequest: HideRequest): HideResponse

}