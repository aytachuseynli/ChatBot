package com.aytachuseynli.chatbot.di

import com.aytachuseynli.chatbot.data.remote.CohereApi
import com.aytachuseynli.chatbot.data.remote.MistralApi
import com.aytachuseynli.chatbot.data.remote.NvidiaApi
import com.aytachuseynli.chatbot.data.repository.ChatRepositoryImpl
import com.aytachuseynli.chatbot.domain.repository.ChatRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

/**
 * Hilt module providing network-related dependencies.
 * Creates separate Retrofit instances for each AI provider.
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    /**
     * Provides OkHttpClient with logging interceptor for debugging
     */
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()
    }

    /**
     * Provides Retrofit instance for Mistral API
     */
    @Provides
    @Singleton
    @Named("MistralRetrofit")
    fun provideMistralRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(MistralApi.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    /**
     * Provides Retrofit instance for NVIDIA API
     */
    @Provides
    @Singleton
    @Named("NvidiaRetrofit")
    fun provideNvidiaRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(NvidiaApi.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    /**
     * Provides Retrofit instance for Cohere API
     */
    @Provides
    @Singleton
    @Named("CohereRetrofit")
    fun provideCohereRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(CohereApi.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    /**
     * Provides Mistral API interface
     */
    @Provides
    @Singleton
    fun provideMistralApi(@Named("MistralRetrofit") retrofit: Retrofit): MistralApi {
        return retrofit.create(MistralApi::class.java)
    }

    /**
     * Provides NVIDIA API interface
     */
    @Provides
    @Singleton
    fun provideNvidiaApi(@Named("NvidiaRetrofit") retrofit: Retrofit): NvidiaApi {
        return retrofit.create(NvidiaApi::class.java)
    }

    /**
     * Provides Cohere API interface
     */
    @Provides
    @Singleton
    fun provideCohereApi(@Named("CohereRetrofit") retrofit: Retrofit): CohereApi {
        return retrofit.create(CohereApi::class.java)
    }

    /**
     * Provides ChatRepository implementation
     */
    @Provides
    @Singleton
    fun provideChatRepository(
        mistralApi: MistralApi,
        nvidiaApi: NvidiaApi,
        cohereApi: CohereApi
    ): ChatRepository {
        return ChatRepositoryImpl(mistralApi, nvidiaApi, cohereApi)
    }
}
