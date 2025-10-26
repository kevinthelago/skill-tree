package com.skilltree.skilltree.ai

import com.skilltree.skilltree.entity.AIAgentType
import com.skilltree.skilltree.entity.Source

/**
 * Interface for AI provider implementations
 * Each implementation handles communication with a specific AI service
 */
interface AIProvider {
    /**
     * The type of AI agent this provider handles
     */
    val agentType: AIAgentType

    /**
     * Generate content based on a prompt and optional sources
     * @param prompt The prompt to send to the AI
     * @param sources Optional list of sources to provide as context
     * @param maxTokens Maximum tokens to generate
     * @param temperature Temperature parameter for generation
     * @return Generated text response
     */
    suspend fun generateContent(
        prompt: String,
        sources: List<Source> = emptyList(),
        maxTokens: Int = 4096,
        temperature: Double = 0.7
    ): String

    /**
     * Analyze sources and extract structured information
     * @param sources List of sources to analyze
     * @param extractionPrompt Prompt describing what information to extract
     * @return Extracted information as structured text
     */
    suspend fun analyzeSources(
        sources: List<Source>,
        extractionPrompt: String
    ): String

    /**
     * Check if this provider is properly configured and ready to use
     * @return true if the provider is ready
     */
    suspend fun isAvailable(): Boolean
}

/**
 * Data class representing an AI generation request
 */
data class AIGenerationRequest(
    val prompt: String,
    val sources: List<Source> = emptyList(),
    val maxTokens: Int = 4096,
    val temperature: Double = 0.7,
    val systemPrompt: String? = null
)

/**
 * Data class representing an AI generation response
 */
data class AIGenerationResponse(
    val content: String,
    val tokensUsed: Int? = null,
    val model: String? = null
)
