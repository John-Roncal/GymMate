package com.example.crud_producto.network

import com.example.crud_producto.model.ApiResponse
import com.example.crud_producto.model.LoginRequest
import com.example.crud_producto.model.PreguntaRequest
import com.example.crud_producto.model.RegisterRequest
import com.example.crud_producto.model.RespuestaChat
import com.example.crud_producto.model.RespuestaIA
import com.example.crud_producto.model.UsuarioRequest
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @POST("/perfil")
    suspend fun generarPlan(@Body usuario: UsuarioRequest): Response<RespuestaIA>

    @POST("/login")
    suspend fun login(@Body request: LoginRequest): Response<ApiResponse>

    @POST("/register")
    suspend fun register(@Body request: RegisterRequest): Response<ApiResponse>

    @GET("/plan/{usuario_id}")
    suspend fun obtenerPlanDesdeBD(@Path("usuario_id") usuarioId: Int): Response<RespuestaIA>

    @POST("/chatbot")
    suspend fun consultarChatbot(@Body pregunta: PreguntaRequest): Response<RespuestaChat>
}