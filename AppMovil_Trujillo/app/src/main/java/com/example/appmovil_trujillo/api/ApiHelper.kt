package com.example.appmovil_trujillo.api

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

// Data models for request/response

data class LoginRequest(val username: String, val password: String)

data class RegisterRequest(val username: String, val password: String)

data class ApiResponse(val success: Boolean, val detail: String)

// Retrofit API definition
interface ApiService {
    @POST("/login")
    suspend fun login(@Body request: LoginRequest): Response<ApiResponse>

    @POST("/register")
    suspend fun register(@Body request: RegisterRequest): Response<ApiResponse>
}

// Singleton Retrofit client
object ApiClient {
    private const val BASE_URL = "http://10.0.2.2:8000" // Usa 10.0.2.2 para emulador Android

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}

// Helper para interactuar con la API
class ApiHelper {
    private val service = ApiClient.apiService

    /**
     * Verifica usuario y contraseña contra el endpoint /login
     * @return true si credenciales válidas
     */
    suspend fun checkUser(username: String, password: String): Boolean =
        withContext(Dispatchers.IO) {
            try {
                val response = service.login(LoginRequest(username, password))
                if (response.isSuccessful) {
                    response.body()?.success == true
                } else {
                    Log.e("ApiHelper", "Login failed: HTTP ${response.code()}")
                    false
                }
            } catch (e: Exception) {
                Log.e("ApiHelper", "Error en login: ${e.message}")
                false
            }
        }

    /**
     * Registra nuevo usuario usando el endpoint /register
     * @return true si registro exitoso
     */
    suspend fun registerUser(username: String, password: String): Boolean =
        withContext(Dispatchers.IO) {
            try {
                val response = service.register(RegisterRequest(username, password))
                if (response.isSuccessful) {
                    response.body()?.success == true
                } else {
                    Log.e("ApiHelper", "Register failed: HTTP ${response.code()}")
                    false
                }
            } catch (e: Exception) {
                Log.e("ApiHelper", "Error en register: ${e.message}")
                false
            }
        }
}
