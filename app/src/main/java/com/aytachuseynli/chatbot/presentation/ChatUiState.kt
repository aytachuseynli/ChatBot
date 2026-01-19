package com.aytachuseynli.chatbot.presentation

import com.aytachuseynli.chatbot.domain.model.AiProvider
import com.aytachuseynli.chatbot.domain.model.AiResponse
import com.aytachuseynli.chatbot.domain.model.ChatMessage

/**
 * UI State for the Chat screen
 */
data class ChatUiState(
    val inputMessage: String = "",
    val isLoading: Boolean = false,
    val chatHistory: List<ChatMessage> = emptyList(),
    val currentResponses: List<AiResponse> = listOf(
        AiResponse(modelName = "mistral-small", modelProvider = AiProvider.MISTRAL),
        AiResponse(modelName = "llama-3.1-8b", modelProvider = AiProvider.NVIDIA),
        AiResponse(modelName = "command-r7b", modelProvider = AiProvider.COHERE)
    ),
    val isDarkTheme: Boolean = false
)