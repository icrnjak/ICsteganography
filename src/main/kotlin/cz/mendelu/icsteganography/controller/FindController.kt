package cz.mendelu.icsteganography.controller

import cz.mendelu.icsteganography.dto.FindRequest
import cz.mendelu.icsteganography.service.SteganographyService
import cz.mendelu.icsteganography.validator.FindRequestValidator
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.WebDataBinder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.InitBinder
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/find")
class FindController(
    private val steganographyService: SteganographyService,
    private val findRequestValidator: FindRequestValidator
) {
    @InitBinder("findRequest")
    fun initBinding(binder: WebDataBinder) {
        binder.validator = findRequestValidator
    }

    @GetMapping
    fun showMainPage(model: Model): String {
        model.addAttribute("findRequest", FindRequest())
        return "find"
    }

    @PostMapping
    fun hide(
        @Validated @ModelAttribute("findRequest") findRequest: FindRequest,
        bindingResult: BindingResult,
        model: Model
    ): String {
        if (bindingResult.hasErrors()) {
            return "find"
        }

        val findResponse = steganographyService.find(findRequest)
        model.addAttribute("findResponse", findResponse)

        return "find"
    }
}