package cz.mendelu.icsteganography.dto

import org.springframework.web.multipart.MultipartFile

data class FindRequest(
    var gif: MultipartFile? = null
)
