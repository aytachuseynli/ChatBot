package com.aytachuseynli.chatbot.presentation

import com.aytachuseynli.chatbot.domain.model.AiModelConfig
import com.aytachuseynli.chatbot.domain.model.AiResponse
import com.aytachuseynli.chatbot.domain.model.ChatMessage

/**
 * UI State for the Chat screen
 */
data class ChatUiState(
    val inputMessage: String = "",
    val isLoading: Boolean = false,
    val chatHistory: List<ChatMessage> = emptyList(),
    val currentResponses: List<AiResponse> = AiModelConfig.getDefaultResponses(),
    val isDarkTheme: Boolean = false
)
