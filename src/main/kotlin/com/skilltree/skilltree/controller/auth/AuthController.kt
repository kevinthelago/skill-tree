package com.skilltree.skilltree.controller.auth

import com.skilltree.skilltree.dto.*
import com.skilltree.skilltree.service.AuthService
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import jakarta.servlet.http.HttpSession
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authService: AuthService
) {
    private val logger = LoggerFactory.getLogger(AuthController::class.java)

    @PostMapping("/register")
    fun register(@Valid @RequestBody request: RegisterRequest): ResponseEntity<UserDto> {
        logger.info("POST /api/auth/register - username: ${request.username}")
        val user = authService.register(request)
        logger.info("POST /api/auth/register - User registered successfully")
        return ResponseEntity.ok(user)
    }

    @PostMapping("/login")
    fun login(
        @Valid @RequestBody request: LoginRequest,
        session: HttpSession,
        httpRequest: HttpServletRequest,
        httpResponse: HttpServletResponse
    ): ResponseEntity<LoginResponse> {
        logger.info("POST /api/auth/login - username: ${request.username}")
        val response = authService.login(request, session, httpRequest, httpResponse)
        logger.info("POST /api/auth/login - Login successful")
        return ResponseEntity.ok(response)
    }

    @PostMapping("/logout")
    fun logout(session: HttpSession): ResponseEntity<MessageResponse> {
        logger.info("POST /api/auth/logout")
        authService.logout(session)
        logger.info("POST /api/auth/logout - Logout successful")
        return ResponseEntity.ok(MessageResponse("Logout successful"))
    }

    @GetMapping("/me")
    fun getCurrentUser(session: HttpSession): ResponseEntity<UserDto> {
        logger.info("GET /api/auth/me")
        val user = authService.getCurrentUser(session)
        logger.info("GET /api/auth/me - Retrieved current user")
        return ResponseEntity.ok(user)
    }
}
