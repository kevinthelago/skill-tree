package com.skilltree.skilltree.controller.generation

import com.skilltree.skilltree.entity.AIAgentType
import com.skilltree.skilltree.pipeline.DomainGenerationPipeline
import com.skilltree.skilltree.pipeline.DomainGenerationRequest
import com.skilltree.skilltree.pipeline.DomainGenerationResponse
import com.skilltree.skilltree.pipeline.GenerationStatus
import jakarta.validation.Valid
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import kotlinx.coroutines.runBlocking
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/generation")
class GenerationController(
    private val generationPipeline: DomainGenerationPipeline
) {

    @PostMapping("/domain")
    fun generateDomain(@Valid @RequestBody request: GenerateDomainRequestDto): ResponseEntity<DomainGenerationResponse> {
        return try {
            // Run the generation pipeline
            val domain = runBlocking {
                generationPipeline.generateDomain(
                    domainTopic = request.topic,
                    aiAgentType = request.aiAgentType,
                    maxSources = request.maxSources
                )
            }

            // Return response
            val response = DomainGenerationResponse(
                domainId = domain.id,
                domainName = domain.name,
                sourcesUsed = domain.sources.size,
                categoriesCreated = domain.categories.size,
                status = GenerationStatus.COMPLETED
            )

            ResponseEntity.ok(response)
        } catch (e: Exception) {
            val errorResponse = DomainGenerationResponse(
                domainId = 0,
                domainName = request.topic,
                sourcesUsed = 0,
                status = GenerationStatus.FAILED
            )
            ResponseEntity.status(500).body(errorResponse)
        }
    }
}

/**
 * DTO for domain generation request
 */
data class GenerateDomainRequestDto(
    @field:NotBlank(message = "Topic is required")
    val topic: String,

    val aiAgentType: AIAgentType = AIAgentType.GEMINI,

    @field:Min(1, message = "Max sources must be at least 1")
    @field:Max(50, message = "Max sources cannot exceed 50")
    val maxSources: Int = 10
)
