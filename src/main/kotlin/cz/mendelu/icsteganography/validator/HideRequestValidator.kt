package cz.mendelu.icsteganography.validator

import cz.mendelu.icsteganography.dto.HideRequest
import cz.mendelu.icsteganography.stego.GifImage
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import org.springframework.validation.Errors
import org.springframework.validation.Validator

@Component
class HideRequestValidator : Validator {

    override fun supports(clazz: Class<*>): Boolean {
        return HideRequest::class.java.isAssignableFrom(clazz)
    }

    override fun validate(target: Any, errors: Errors) {
        val hideRequest = target as HideRequest

        if (hideRequest.gif == null || hideRequest.gif?.bytes == null || hideRequest.gif?.bytes?.isEmpty() == true) {
            errors.rejectValue(HideRequest::gif.name, "", "GIF must be provided")
        }
        if (!StringUtils.hasText(hideRequest.text)) {
            errors.rejectValue(HideRequest::text.name, "", "Text must be provided")
        }
        if (errors.hasErrors()) {
            return
        }

        val gifImage = GifImage(hideRequest.gif!!.bytes)
        if (!gifImage.usesGlobalColorTable) {
            errors.rejectValue(HideRequest::gif.name, "", "This GIF does not use global color table, so it can not be used")
            return
        }


        val textBytesSize = hideRequest.text!!.toByteArray().size
        val maxHideBytes = gifImage.globalColorTableSize * 3 / 8 / 8
        if (textBytesSize > maxHideBytes) {
            errors.rejectValue(HideRequest::text.name, "", "This GIF can not store given text. Max is ${maxHideBytes}B, but text has ${textBytesSize}B")
            return
        }
    }
}