package com.aytachuseynli.chatbot.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aytachuseynli.chatbot.domain.model.AiProvider
import com.aytachuseynli.chatbot.domain.model.AiResponse
import com.aytachuseynli.chatbot.domain.model.ChatMessage
import com.aytachuseynli.chatbot.domain.repository.ChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for managing chat state and interactions with AI providers.
 */
@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    /**
     * Updates the input message as the user types
     */
    fun onInputChange(message: String) {
        _uiState.update { it.copy(inputMessage = message) }
    }

    /**
     * Toggles the app theme between dark and light mode
     */
    fun toggleTheme() {
        _uiState.update { it.copy(isDarkTheme = !it.isDarkTheme) }
    }

    /**
     * Sends the current input message to all AI providers
     */
    fun sendMessage() {
        val message = _uiState.value.inputMessage.trim()
        if (message.isBlank()) return

        viewModelScope.launch {
            // Add user message to history
            val userMessage = ChatMessage(
                content = message,
                isFromUser = true
            )

            _uiState.update {
                it.copy(
                    inputMessage = "",
                    isLoading = true,
                    chatHistory = it.chatHistory + userMessage
                )
            }

            // Collect responses as they arrive
            chatRepository.sendMessageToAllProviders(message)
                .catch { e ->
                    // Handle flow collection errors
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            currentResponses = listOf(
                                AiResponse(
                                    modelName = "mistral-small",
                                    modelProvider = AiProvider.MISTRAL,
                                    errorMessage = e.message
                                ),
                                AiResponse(
                                    modelName = "llama-3.1-8b",
                                    modelProvider = AiProvider.NVIDIA,
                                    errorMessage = e.message
                                ),
                                AiResponse(
                                    modelName = "command-r7b",
                                    modelProvider = AiProvider.COHERE,
                                    errorMessage = e.message
                                )
                            )
                        )
                    }
                }
                .collect { responses ->
                    val allDone = responses.none { it.isLoading }

                    _uiState.update { state ->
                        // If all responses are done, add them to chat history
                        if (allDone) {
                            val aiMessage = ChatMessage(
                                content = message,
                                isFromUser = false,
                                aiResponses = responses
                            )
                            state.copy(
                                currentResponses = responses,
                                isLoading = false,
                                chatHistory = state.chatHistory + aiMessage
                            )
                        } else {
                            state.copy(currentResponses = responses)
                        }
                    }
                }
        }
    }

    /**
     * Clears the chat history
     */
    fun clearHistory() {
        _uiState.update {
            it.copy(
                chatHistory = emptyList(),
                currentResponses = listOf(
                    AiResponse(modelName = "mistral-small", modelProvider = AiProvider.MISTRAL),
                    AiResponse(modelName = "llama-3.1-8b", modelProvider = AiProvider.NVIDIA),
                    AiResponse(modelName = "command-r7b", modelProvider = AiProvider.COHERE)
                )
            )
        }
    }
}
