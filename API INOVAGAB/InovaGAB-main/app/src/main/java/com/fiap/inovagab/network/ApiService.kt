package com.fiap.inovagab.network

import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>

    @POST("api/auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<AuthResponse>

    @GET("api/strategies")
    suspend fun getStrategies(): Response<List<StrategyDto>>

    @POST("api/strategies")
    suspend fun createStrategy(@Body strategy: StrategyDto): Response<StrategyDto>

    @GET("api/ideas")
    suspend fun getIdeas(): Response<List<IdeaDto>>

    @POST("api/ideas")
    suspend fun createIdea(@Body idea: IdeaDto): Response<IdeaDto>

    @POST("api/ideas/{id}/review")
    suspend fun reviewIdea(@Path("id") id: String, @Body review: ReviewIdeaRequest): Response<IdeaDto>

    @GET("api/projects")
    suspend fun getProjects(): Response<List<ProjectDto>>

    @POST("api/projects")
    suspend fun createProject(@Body project: ProjectDto): Response<ProjectDto>
}