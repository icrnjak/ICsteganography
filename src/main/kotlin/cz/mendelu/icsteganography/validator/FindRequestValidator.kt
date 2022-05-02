package cz.mendelu.icsteganography.validator

import cz.mendelu.icsteganography.dto.FindRequest
import cz.mendelu.icsteganography.dto.HideRequest
import cz.mendelu.icsteganography.stego.GifImage
import org.springframework.stereotype.Component
import org.springframework.validation.Errors
import org.springframework.validation.Validator

@Component
class FindRequestValidator : Validator {

    override fun supports(clazz: Class<*>): Boolean {
        return FindRequest::class.java.isAssignableFrom(clazz)
    }

    override fun validate(target: Any, errors: Errors) {
        val findRequest = target as FindRequest

        if (findRequest.gif == null || findRequest.gif?.bytes == null || findRequest.gif?.bytes?.isEmpty() == true) {
            errors.rejectValue(FindRequest::gif.name, "", "GIF must be provided")
        }
        if (errors.hasErrors()) {
            return
        }

        val gifImage = GifImage(findRequest.gif!!.bytes)
        if (!gifImage.usesGlobalColorTable) {
            errors.rejectValue(HideRequest::gif.name, "", "This GIF does not use global color table, so it can not be used")
            return
        }
    }
}