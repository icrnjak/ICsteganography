package cz.mendelu.icsteganography.controller

import cz.mendelu.icsteganography.dto.HideRequest
import cz.mendelu.icsteganography.service.SteganographyService
import cz.mendelu.icsteganography.validator.HideRequestValidator
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.WebDataBinder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.InitBinder
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import java.util.*

@Controller
class HideController(
    private val steganographyService: SteganographyService,
    private val hideRequestValidator: HideRequestValidator
) {
    @InitBinder("hideRequest")
    fun initBinding(binder: WebDataBinder) {
        binder.validator = hideRequestValidator
    }

    @GetMapping
    fun showMainPage(model: Model): String {
        model.addAttribute("hideRequest", HideRequest())
        return "hide"
    }

    @PostMapping("/hide")
    fun hide(
        @Validated @ModelAttribute("hideRequest") hideRequest: HideRequest,
        bindingResult: BindingResult,
        model: Model
    ): String {
        if (bindingResult.hasErrors()) {
            return "hide"
        }

        val hideResponse = steganographyService.hide(hideRequest)
        model.addAttribute("hideResponse", hideResponse)

        hideResponse.original?.let {
            model.addAttribute("originalBase64", Base64.getEncoder().encodeToString(it.bytes))
        }
        hideResponse.result?.let {
            model.addAttribute("resultBase64", Base64.getEncoder().encodeToString(it.bytes))
        }
        return "hide"
    }
}