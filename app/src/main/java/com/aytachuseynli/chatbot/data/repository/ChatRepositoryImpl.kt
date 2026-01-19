package com.aytachuseynli.chatbot.data.repository

import com.aytachuseynli.chatbot.BuildConfig
import com.aytachuseynli.chatbot.data.models.*
import com.aytachuseynli.chatbot.data.remote.CohereApi
import com.aytachuseynli.chatbot.data.remote.MistralApi
import com.aytachuseynli.chatbot.data.remote.NvidiaApi
import com.aytachuseynli.chatbot.data.util.toUserFriendlyMessage
import com.aytachuseynli.chatbot.domain.model.AiModelConfig
import com.aytachuseynli.chatbot.domain.model.AiResponse
import com.aytachuseynli.chatbot.domain.model.ModelInfo
import com.aytachuseynli.chatbot.domain.repository.ChatRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatRepositoryImpl @Inject constructor(
    private val mistralApi: MistralApi,
    private val nvidiaApi: NvidiaApi,
    private val cohereApi: CohereApi
) : ChatRepository {

    private val mistralModel = AiModelConfig.models[0]
    private val nvidiaModel = AiModelConfig.models[1]
    private val cohereModel = AiModelConfig.models[2]

    override fun sendMessageToAllProviders(message: String): Flow<List<AiResponse>> = flow {
        val responses = AiModelConfig.getLoadingResponses().toMutableList()
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
        return executeApiCall(mistralModel, BuildConfig.MISTRAL_API_KEY) {
            val request = MistralRequest(
                messages = listOf(MistralMessage(role = "user", content = message))
            )
            mistralApi.chatCompletion("Bearer $it", request)
                .choices?.firstOrNull()?.message?.content
        }
    }

    override suspend fun sendToNvidia(message: String): AiResponse {
        return executeApiCall(nvidiaModel, BuildConfig.NVIDIA_API_KEY) {
            val request = NvidiaRequest(
                messages = listOf(NvidiaMessage(role = "user", content = message))
            )
            nvidiaApi.chatCompletion("Bearer $it", request)
                .choices?.firstOrNull()?.message?.content
        }
    }

    override suspend fun sendToCohere(message: String): AiResponse {
        return executeApiCall(cohereModel, BuildConfig.COHERE_API_KEY) {
            val request = CohereRequest(
                messages = listOf(CohereMessage(role = "user", content = message))
            )
            cohereApi.chat("Bearer $it", request)
                .message?.content?.firstOrNull()?.text
        }
    }

    private suspend inline fun executeApiCall(
        model: ModelInfo,
        apiKey: String,
        crossinline apiCall: suspend (String) -> String?
    ): AiResponse {
        if (apiKey.isBlank()) {
            return model.toErrorResponse("${model.provider.displayName} API key not configured")
        }

        val startTime = System.currentTimeMillis()

        return runCatching { apiCall(apiKey) }
            .fold(
                onSuccess = { content ->
                    model.toSuccessResponse(
                        content = content ?: "No response",
                        responseTimeMs = System.currentTimeMillis() - startTime
                    )
                },
                onFailure = { exception ->
                    model.toErrorResponse(exception.toUserFriendlyMessage())
                }
            )
    }
}
