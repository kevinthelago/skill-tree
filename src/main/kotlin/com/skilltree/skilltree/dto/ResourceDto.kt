package com.skilltree.skilltree.dto

import com.skilltree.skilltree.entity.ResourceType
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.time.LocalDateTime

data class CreateResourceRequest(
    @field:NotBlank(message = "Resource title is required")
    @field:Size(min = 3, max = 200, message = "Resource title must be between 3 and 200 characters")
    val title: String,

    @field:NotNull(message = "Resource type is required")
    val resourceType: ResourceType,

    @field:NotBlank(message = "Content is required")
    val content: String,

    @field:Size(max = 500, message = "URL must not exceed 500 characters")
    val url: String? = null,

    val displayOrder: Int = 0,

    val metadata: String? = null
)

data class UpdateResourceRequest(
    @field:Size(min = 3, max = 200, message = "Resource title must be between 3 and 200 characters")
    val title: String? = null,

    val content: String? = null,

    @field:Size(max = 500, message = "URL must not exceed 500 characters")
    val url: String? = null,

    val displayOrder: Int? = null,

    val metadata: String? = null
)

data class ResourceResponse(
    val id: Long,
    val title: String,
    val resourceType: ResourceType,
    val content: String,
    val url: String?,
    val filePath: String?,
    val fileName: String?,
    val fileSize: Long?,
    val mimeType: String?,
    val displayOrder: Int,
    val metadata: String?,
    val downloadUrl: String?,
    val createdAt: LocalDateTime
)

data class ResourcesResponse(
    val resources: List<ResourceResponse>,
    val total: Int
)

data class UpdateResourceOrderRequest(
    val displayOrder: Int
)
