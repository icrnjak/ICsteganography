package cz.mendelu.icsteganography.service

import cz.mendelu.icsteganography.dto.FindRequest
import cz.mendelu.icsteganography.dto.FindResponse
import cz.mendelu.icsteganography.dto.HideRequest
import cz.mendelu.icsteganography.dto.HideResponse

interface SteganographyService {

    fun hide(hideRequest: HideRequest): HideResponse

    fun find(findRequest: FindRequest): FindResponse

}