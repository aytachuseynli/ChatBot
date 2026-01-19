package com.aytachuseynli.chatbot.domain.model

/**
 * Domain model representing an AI response from any provider.
 * This is the UI model that abstracts away API-specific details.
 */
data class AiResponse(
    val modelName: String,
    val modelProvider: AiProvider,
    val content: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val responseTimeMs: Long? = null
)

/**
 * Enum representing supported AI providers
 */
enum class AiProvider(val displayName: String) {
    MISTRAL("Mistral AI"),
    NVIDIA("NVIDIA"),
    COHERE("Cohere")
}

/**
 * Represents a chat message in the conversation history
 */
data class ChatMessage(
    val id: String = System.currentTimeMillis().toString(),
    val content: String,
    val isFromUser: Boolean,
    val timestamp: Long = System.currentTimeMillis(),
    val aiResponses: List<AiResponse> = emptyList()
)
