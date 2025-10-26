package com.skilltree.skilltree.controller.ui

import org.springframework.security.core.Authentication
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class UIController {

    @GetMapping("/")
    fun root(authentication: Authentication?): String {
        // If user is authenticated, redirect to private app at /app/
        // Otherwise, serve public app from root
        return if (authentication?.isAuthenticated == true) {
            "redirect:/app/"
        } else {
            "forward:/public/index.html"
        }
    }

    @GetMapping("/login")
    fun login(): String {
        return "forward:/public/login.html"
    }

    @GetMapping("/signup")
    fun signup(): String {
        return "forward:/public/signup.html"
    }

    @GetMapping("/app", "/app/")
    fun privateApp(): String {
        return "forward:/private/index.html"
    }

    @GetMapping("/admin", "/admin/")
    fun adminApp(): String {
        return "forward:/admin/index.html"
    }
}
