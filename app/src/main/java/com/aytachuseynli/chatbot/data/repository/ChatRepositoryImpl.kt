package com.aytachuseynli.chatbot.data.repository

import com.aytachuseynli.chatbot.BuildConfig
import com.aytachuseynli.chatbot.data.models.*
import com.aytachuseynli.chatbot.data.remote.CohereApi
import com.aytachuseynli.chatbot.data.remote.MistralApi
import com.aytachuseynli.chatbot.data.remote.NvidiaApi
import com.aytachuseynli.chatbot.domain.model.AiProvider
import com.aytachuseynli.chatbot.domain.model.AiResponse
import com.aytachuseynli.chatbot.domain.repository.ChatRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatRepositoryImpl @Inject constructor(
    private val mistralApi: MistralApi,
    private val nvidiaApi: NvidiaApi,
    private val cohereApi: CohereApi
) : ChatRepository {

    private companion object {
        const val MISTRAL_MODEL = "mistral-small"
        const val NVIDIA_MODEL = "llama-3.1-8b"
        const val COHERE_MODEL = "command-r7b"
    }

    override fun sendMessageToAllProviders(message: String): Flow<List<AiResponse>> = flow {
        val responses = mutableListOf(
            createLoadingResponse(MISTRAL_MODEL, AiProvider.MISTRAL),
            createLoadingResponse(NVIDIA_MODEL, AiProvider.NVIDIA),
            createLoadingResponse(COHERE_MODEL, AiProvider.COHERE)
        )
        emit(responses.toList())

        coroutineScope {
            val deferredResults = listOf(
                async { sendToMistral(message) },
                async { sendToNvidia(message) },
                async { sendToCohere(message) }
            )

            deferredResults.forEachIndexed { index, deferred ->
                responses[index] = deferred.await()
                emit(responses.toList())
            }
        }
    }

    override suspend fun sendToMistral(message: String): AiResponse {
        return executeApiCall(
            modelName = MISTRAL_MODEL,
            provider = AiProvider.MISTRAL,
            apiKey = BuildConfig.MISTRAL_API_KEY
        ) {
            val request = MistralRequest(
                messages = listOf(MistralMessage(role = "user", content = message))
            )
            mistralApi.chatCompletion("Bearer $it", request)
                .choices?.firstOrNull()?.message?.content
        }
    }

    override suspend fun sendToNvidia(message: String): AiResponse {
        return executeApiCall(
            modelName = NVIDIA_MODEL,
            provider = AiProvider.NVIDIA,
            apiKey = BuildConfig.NVIDIA_API_KEY
        ) {
            val request = NvidiaRequest(
                messages = listOf(NvidiaMessage(role = "user", content = message))
            )
            nvidiaApi.chatCompletion("Bearer $it", request)
                .choices?.firstOrNull()?.message?.content
        }
    }

    override suspend fun sendToCohere(message: String): AiResponse {
        return executeApiCall(
            modelName = COHERE_MODEL,
            provider = AiProvider.COHERE,
            apiKey = BuildConfig.COHERE_API_KEY
        ) {
            val request = CohereRequest(
                messages = listOf(CohereMessage(role = "user", content = message))
            )
            cohereApi.chat("Bearer $it", request)
                .message?.content?.firstOrNull()?.text
        }
    }

    private fun createLoadingResponse(modelName: String, provider: AiProvider) =
        AiResponse(modelName = modelName, modelProvider = provider, isLoading = true)

    private fun createErrorResponse(modelName: String, provider: AiProvider, error: String) =
        AiResponse(modelName = modelName, modelProvider = provider, isLoading = false, errorMessage = error)

    private fun createSuccessResponse(
        modelName: String,
        provider: AiProvider,
        content: String,
        responseTime: Long
    ) = AiResponse(
        modelName = modelName,
        modelProvider = provider,
        content = content,
        isLoading = false,
        responseTimeMs = responseTime
    )

    private suspend inline fun executeApiCall(
        modelName: String,
        provider: AiProvider,
        apiKey: String,
        crossinline apiCall: suspend (String) -> String?
    ): AiResponse {
        if (apiKey.isBlank()) {
            return createErrorResponse(modelName, provider, "${provider.displayName} API key not configured")
        }

        val startTime = System.currentTimeMillis()

        return runCatching { apiCall(apiKey) }
            .fold(
                onSuccess = { content ->
                    createSuccessResponse(
                        modelName = modelName,
                        provider = provider,
                        content = content ?: "No response",
                        responseTime = System.currentTimeMillis() - startTime
                    )
                },
                onFailure = { exception ->
                    createErrorResponse(modelName, provider, exception.toUserFriendlyMessage())
                }
            )
    }

    private fun Throwable.toUserFriendlyMessage(): String = when (this) {
        is HttpException -> when (code()) {
            401 -> "Authentication failed. Check API key."
            403 -> "Access forbidden. Check API permissions."
            404 -> "API endpoint not found. Model may be deprecated."
            429 -> "Rate limit exceeded. Please wait."
            in 500..599 -> "Server error. Try again later."
            else -> "HTTP error: ${code()}"
        }
        is SocketTimeoutException -> "Request timed out."
        is UnknownHostException -> "No internet connection."
        is IOException -> "Network error. Check your connection."
        else -> message ?: "Unknown error occurred"
    }
}
