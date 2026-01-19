package com.aytachuseynli.chatbot.domain.model

/**
 * Centralized configuration for AI models.
 * Single source of truth for model names and providers.
 */
object AiModelConfig {

    val models = listOf(
        ModelInfo("mistral-large-latest", AiProvider.MISTRAL),
        ModelInfo("llama-3.1-8b", AiProvider.NVIDIA),
        ModelInfo("command-r7b", AiProvider.COHERE)
    )

    fun getDefaultResponses(): List<AiResponse> = models.map { it.toEmptyResponse() }

    fun getLoadingResponses(): List<AiResponse> = models.map { it.toLoadingResponse() }
}

data class ModelInfo(
    val name: String,
    val provider: AiProvider
) {
    fun toEmptyResponse() = AiResponse(modelName = name, modelProvider = provider)

    fun toLoadingResponse() = AiResponse(modelName = name, modelProvider = provider, isLoading = true)

    fun toErrorResponse(error: String) = AiResponse(
        modelName = name,
        modelProvider = provider,
        isLoading = false,
        errorMessage = error
    )

    fun toSuccessResponse(content: String, responseTimeMs: Long) = AiResponse(
        modelName = name,
        modelProvider = provider,
        content = content,
        isLoading = false,
        responseTimeMs = responseTimeMs
    )
}
