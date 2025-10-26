package com.skilltree.skilltree.controller

import com.skilltree.skilltree.dto.UserProfileDTO
import com.skilltree.skilltree.dto.UserProgressTreeDTO
import com.skilltree.skilltree.entity.User
import com.skilltree.skilltree.service.ProgressService
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import com.skilltree.skilltree.repository.UserRepository

@RestController
@RequestMapping("/api/progress")
class ProgressController(
    private val progressService: ProgressService,
    private val userRepository: UserRepository
) {

    @GetMapping("/me")
    fun getMyProgress(@AuthenticationPrincipal userDetails: UserDetails): ResponseEntity<UserProgressTreeDTO> {
        val user = userRepository.findByUsername(userDetails.username)
            .orElseThrow { RuntimeException("User not found") }

        val progressTree = progressService.getUserProgressTree(user)
        return ResponseEntity.ok(progressTree)
    }

    @GetMapping("/profile")
    fun getMyProfile(@AuthenticationPrincipal userDetails: UserDetails): ResponseEntity<UserProfileDTO> {
        val user = userRepository.findByUsername(userDetails.username)
            .orElseThrow { RuntimeException("User not found") }

        val profile = progressService.getUserProfile(user)
        return ResponseEntity.ok(profile)
    }
}
