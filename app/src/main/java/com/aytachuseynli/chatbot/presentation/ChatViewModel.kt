package com.aytachuseynli.chatbot.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aytachuseynli.chatbot.domain.model.AiModelConfig
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

    fun onInputChange(message: String) {
        _uiState.update { it.copy(inputMessage = message) }
    }

    fun toggleTheme() {
        _uiState.update { it.copy(isDarkTheme = !it.isDarkTheme) }
    }

    fun sendMessage() {
        val message = _uiState.value.inputMessage.trim()
        if (message.isBlank()) return

        viewModelScope.launch {
            val userMessage = ChatMessage(content = message, isFromUser = true)

            _uiState.update {
                it.copy(
                    inputMessage = "",
                    isLoading = true,
                    chatHistory = it.chatHistory + userMessage
                )
            }

            chatRepository.sendMessageToAllProviders(message)
                .catch { e ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            currentResponses = AiModelConfig.models.map { model ->
                                model.toErrorResponse(e.message ?: "Unknown error")
                            }
                        )
                    }
                }
                .collect { responses ->
                    val allDone = responses.none { it.isLoading }

                    _uiState.update { state ->
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

    fun clearHistory() {
        _uiState.update {
            it.copy(
                chatHistory = emptyList(),
                currentResponses = AiModelConfig.getDefaultResponses()
            )
        }
    }
}
