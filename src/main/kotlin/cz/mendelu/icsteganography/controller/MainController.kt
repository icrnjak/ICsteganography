package cz.mendelu.icsteganography.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class MainController {

    @GetMapping
    fun showMainPage(): String {
        return "main"
    }
}