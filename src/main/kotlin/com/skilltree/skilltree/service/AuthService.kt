package com.skilltree.skilltree.service

import com.skilltree.skilltree.dto.LoginRequest
import com.skilltree.skilltree.dto.LoginResponse
import com.skilltree.skilltree.dto.RegisterRequest
import com.skilltree.skilltree.dto.UserDto
import com.skilltree.skilltree.entity.User
import com.skilltree.skilltree.exception.DuplicateUserException
import com.skilltree.skilltree.exception.InvalidCredentialsException
import com.skilltree.skilltree.exception.UserNotFoundException
import com.skilltree.skilltree.repository.UserRepository
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import jakarta.servlet.http.HttpSession
import org.slf4j.LoggerFactory
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {
    private val logger = LoggerFactory.getLogger(AuthService::class.java)

    @Transactional
    fun register(request: RegisterRequest): UserDto {
        logger.info("Attempting to register user: ${request.username}")

        if (userRepository.existsByUsername(request.username)) {
            logger.error("Registration failed: Username already exists: ${request.username}")
            throw DuplicateUserException("Username '${request.username}' is already taken")
        }

        if (userRepository.existsByEmail(request.email)) {
            logger.error("Registration failed: Email already exists: ${request.email}")
            throw DuplicateUserException("Email '${request.email}' is already registered")
        }

        val user = User(
            username = request.username,
            email = request.email,
            password = passwordEncoder.encode(request.password),
            role = "USER"
        )

        val savedUser = userRepository.save(user)
        logger.info("User registered successfully: ${savedUser.username} with ID: ${savedUser.id}")

        return toUserDto(savedUser)
    }

    fun login(
        request: LoginRequest,
        session: HttpSession,
        httpRequest: HttpServletRequest,
        httpResponse: HttpServletResponse
    ): LoginResponse {
        logger.info("Login attempt for username: ${request.username}")

        val user = userRepository.findByUsername(request.username)
            .orElseThrow {
                logger.error("Login failed: User not found: ${request.username}")
                InvalidCredentialsException("Invalid username or password")
            }

        if (!passwordEncoder.matches(request.password, user.password)) {
            logger.error("Login failed: Invalid password for user: ${request.username}")
            throw InvalidCredentialsException("Invalid username or password")
        }

        // Store user in session
        session.setAttribute("userId", user.id)
        session.setAttribute("username", user.username)
        session.setAttribute("role", user.role)

        logger.info("User logged in successfully: ${user.username}")

        return LoginResponse(
            success = true,
            message = "Login successful",
            user = toUserDto(user)
        )
    }

    fun logout(session: HttpSession) {
        val username = session.getAttribute("username") as? String
        logger.info("Logging out user: $username")
        session.invalidate()
        logger.info("User logged out successfully: $username")
    }

    fun getCurrentUser(session: HttpSession): UserDto {
        val userId = session.getAttribute("userId") as? Long
            ?: throw InvalidCredentialsException("Not authenticated")

        val user = userRepository.findById(userId)
            .orElseThrow {
                logger.error("Current user not found with ID: $userId")
                UserNotFoundException("User not found")
            }

        return toUserDto(user)
    }

    private fun toUserDto(user: User): UserDto {
        return UserDto(
            id = user.id,
            username = user.username,
            email = user.email,
            role = user.role
        )
    }
}
