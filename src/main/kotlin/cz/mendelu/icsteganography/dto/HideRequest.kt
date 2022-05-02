package cz.mendelu.icsteganography.dto

import org.springframework.web.multipart.MultipartFile

data class HideRequest(
    var gif: MultipartFile? = null,
    var text: String? = null
)
