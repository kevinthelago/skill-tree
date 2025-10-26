package com.skilltree.skilltree.pipeline

import com.skilltree.skilltree.ai.AIProvider
import com.skilltree.skilltree.dto.CreateDomainRequest
import com.skilltree.skilltree.dto.DomainResponse
import com.skilltree.skilltree.entity.*
import com.skilltree.skilltree.exception.AIProviderException
import com.skilltree.skilltree.repository.DomainRepository
import com.skilltree.skilltree.research.ResearchSource
import com.skilltree.skilltree.service.*
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class DomainGenerationPipeline(
    private val researchSources: List<ResearchSource>,
    private val aiProviders: List<AIProvider>,
    private val domainService: DomainService,
    private val categoryService: CategoryService,
    private val subcategoryService: SubcategoryService,
    private val skillService: SkillService,
    private val microskillService: MicroskillService,
    private val sourceService: SourceService,
    private val domainRepository: DomainRepository
) {

    private val logger = LoggerFactory.getLogger(DomainGenerationPipeline::class.java)

    /**
     * Generate a complete domain hierarchy with source tracking
     * @param domainTopic The topic to research and generate
     * @param aiAgentType The AI agent to use for generation
     * @param maxSources Maximum number of sources to research per level
     * @return The generated Domain entity with all relationships
     */
    suspend fun generateDomain(
        domainTopic: String,
        aiAgentType: AIAgentType,
        maxSources: Int = 10
    ): Domain = coroutineScope {
        logger.info("Starting domain generation for topic: $domainTopic using AI: $aiAgentType")

        // Step 1: Research the domain topic
        val sources = researchDomainTopic(domainTopic, maxSources)
        logger.info("Found ${sources.size} sources for domain research")

        // Step 2: Get AI provider
        val aiProvider = getAIProvider(aiAgentType)
        if (!aiProvider.isAvailable()) {
            throw AIProviderException("AI provider $aiAgentType is not available")
        }

        // Step 3: Generate domain structure using AI
        val domainPrompt = buildDomainPrompt(domainTopic, sources)
        val aiResponse = aiProvider.generateContent(domainPrompt, sources)

        // Step 4: Parse AI response and create domain
        val domain = createDomainFromAI(domainTopic, aiResponse, sources)
        logger.info("Successfully generated domain: ${domain.name}")

        return@coroutineScope domain
    }

    /**
     * Research a domain topic across multiple sources
     */
    private suspend fun researchDomainTopic(topic: String, maxSources: Int): List<Source> = coroutineScope {
        logger.info("Researching topic: $topic")

        val allSources = mutableListOf<Source>()

        // Search across all available research sources in parallel
        val searchJobs = researchSources.map { researchSource ->
            async {
                try {
                    researchSource.search(topic, maxSources / researchSources.size.coerceAtLeast(1))
                } catch (e: Exception) {
                    logger.error("Failed to search ${researchSource.sourceType}: ${e.message}")
                    emptyList()
                }
            }
        }

        val results = searchJobs.awaitAll()
        results.forEach { allSources.addAll(it) }

        // Save sources to database
        allSources.forEach { sourceService.createSource(it) }

        return@coroutineScope allSources.take(maxSources)
    }

    /**
     * Build a prompt for domain generation
     */
    private fun buildDomainPrompt(topic: String, sources: List<Source>): String {
        return """
            Generate a comprehensive skill tree structure for the domain: "$topic"

            Based on the following research sources, create a hierarchical structure with:
            1. Domain name and description
            2. 3-5 main categories
            3. For each category, 3-5 subcategories
            4. For each subcategory, 3-5 skills
            5. For each skill, 3-5 microskills

            Sources for reference:
            ${sources.take(5).joinToString("\n") { "- ${it.title}: ${it.summary?.take(200)}" }}

            Format your response as structured text that I can parse.
            Include clear sections for Domain, Categories, Subcategories, Skills, and Microskills.
        """.trimIndent()
    }

    /**
     * Create domain entity from AI response
     * NOTE: This is a simplified implementation
     * In production, you would parse the AI response more carefully
     */
    private fun createDomainFromAI(
        topic: String,
        aiResponse: String,
        sources: List<Source>
    ): Domain {
        // TODO: Implement proper parsing of AI response
        // For now, create a basic domain structure

        // Create domain using DTO
        val createRequest = CreateDomainRequest(
            name = topic,
            description = "AI-generated domain for $topic",
            prompt = aiResponse.take(1000)
        )

        val domainResponse = domainService.createDomain(createRequest)

        // Get the actual domain entity for linking sources
        val savedDomain = domainRepository.findById(domainResponse.id).orElseThrow()

        // Link sources to domain
        sources.forEach { source ->
            try {
                sourceService.linkSourceToDomain(
                    source = source,
                    domain = savedDomain,
                    relevanceScore = 0.8,
                    relevantExcerpt = source.summary?.take(500),
                    usedForGeneration = true
                )
            } catch (e: Exception) {
                logger.error("Failed to link source ${source.id} to domain: ${e.message}")
            }
        }

        logger.info("Created domain with ${sources.size} linked sources")

        return savedDomain
    }

    /**
     * Get AI provider by type
     */
    private fun getAIProvider(agentType: AIAgentType): AIProvider {
        return aiProviders.find { it.agentType == agentType }
            ?: throw AIProviderException("AI provider not found for type: $agentType")
    }
}

/**
 * Request data class for domain generation
 */
data class DomainGenerationRequest(
    val topic: String,
    val aiAgentType: AIAgentType,
    val maxSources: Int = 10,
    val generateCategories: Boolean = true,
    val generateSubcategories: Boolean = true,
    val generateSkills: Boolean = true,
    val generateMicroskills: Boolean = true
)

/**
 * Response data class for domain generation
 */
data class DomainGenerationResponse(
    val domainId: Long,
    val domainName: String,
    val sourcesUsed: Int,
    val categoriesCreated: Int = 0,
    val subcategoriesCreated: Int = 0,
    val skillsCreated: Int = 0,
    val microskillsCreated: Int = 0,
    val status: GenerationStatus = GenerationStatus.COMPLETED
)

enum class GenerationStatus {
    PENDING,
    IN_PROGRESS,
    COMPLETED,
    FAILED
}
