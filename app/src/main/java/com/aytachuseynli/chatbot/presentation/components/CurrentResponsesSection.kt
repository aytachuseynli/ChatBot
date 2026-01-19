package com.aytachuseynli.chatbot.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

/**
 * Section showing current responses while loading
 */
@Composable
fun CurrentResponsesSection(
    responses: List<com.aytachuseynli.chatbot.domain.model.AiResponse>
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        responses.forEach { response ->
            ResponseCard(response = response)
        }
    }
}
