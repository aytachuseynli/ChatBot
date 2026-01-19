package com.aytachuseynli.chatbot.presentation.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.aytachuseynli.chatbot.domain.model.AiProvider

/**
 * Returns a distinctive color for each AI provider
 */
@Composable
fun getProviderColor(provider: AiProvider): Color {
    return when (provider) {
        AiProvider.MISTRAL -> Color(0xFFFF7000) // Orange
        AiProvider.NVIDIA -> Color(0xFF76B900) // NVIDIA Green
        AiProvider.COHERE -> Color(0xFF6366F1) // Purple/Indigo
    }
}
