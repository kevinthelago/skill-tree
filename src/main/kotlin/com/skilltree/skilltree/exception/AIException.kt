package com.skilltree.skilltree.exception

class AIAgentNotFoundException(message: String) : RuntimeException(message)

class AIProviderException(message: String, cause: Throwable? = null) : RuntimeException(message, cause)

class InvalidAIConfigurationException(message: String) : RuntimeException(message)

class RateLimitExceededException(message: String) : RuntimeException(message)

class ResearchSourceException(message: String, cause: Throwable? = null) : RuntimeException(message, cause)

class InsufficientCreditsException(message: String) : RuntimeException(message)

class AIResponseParsingException(message: String, cause: Throwable? = null) : RuntimeException(message, cause)
