package com.skilltree.skilltree.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import java.time.LocalDateTime

data class CreateDomainRequest(
    @field:NotBlank(message = "Domain name is required")
    @field:Size(min = 3, max = 100, message = "Domain name must be between 3 and 100 characters")
    val name: String,

    @field:Size(max = 500, message = "Description must not exceed 500 characters")
    val description: String? = null,

    val prompt: String? = null
)

data class UpdateDomainRequest(
    @field:Size(min = 3, max = 100, message = "Domain name must be between 3 and 100 characters")
    val name: String? = null,

    @field:Size(max = 500, message = "Description must not exceed 500 characters")
    val description: String? = null,

    val prompt: String? = null
)

data class DomainResponse(
    val id: Long,
    val name: String,
    val description: String?,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)

data class DomainsResponse(
    val domains: List<DomainResponse>,
    val total: Int
)
