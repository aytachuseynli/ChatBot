package com.aytachuseynli.chatbot.domain.repository

import com.aytachuseynli.chatbot.domain.model.AiResponse
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for chat operations.
 * Defines the contract for sending messages to AI providers.
 */
interface ChatRepository {

    /**
     * Sends a message to all AI providers simultaneously and returns
     * a Flow that emits responses as they arrive.
     *
     * @param message The user's message to send to all AI providers
     * @return Flow emitting a list of AiResponse objects, updating as each provider responds
     */
    fun sendMessageToAllProviders(message: String): Flow<List<AiResponse>>

    /**
     * Sends a message to Mistral AI
     *
     * @param message The user's message
     * @return AiResponse from Mistral
     */
    suspend fun sendToMistral(message: String): AiResponse

    /**
     * Sends a message to NVIDIA
     *
     * @param message The user's message
     * @return AiResponse from NVIDIA
     */
    suspend fun sendToNvidia(message: String): AiResponse

    /**
     * Sends a message to Cohere
     *
     * @param message The user's message
     * @return AiResponse from Cohere
     */
    suspend fun sendToCohere(message: String): AiResponse
}
