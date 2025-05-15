package com.example.appmovil_trujillo.api

import android.util.Log
import com.example.appmovil_trujillo.model.LoginRequest
import com.example.appmovil_trujillo.model.RegisterRequest
import com.example.appmovil_trujillo.model.UserProfileRequest
import com.example.appmovil_trujillo.model.ApiResponse
import com.example.appmovil_trujillo.model.UserProfile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

// Retrofit API definition
interface ApiService {
    @POST("/login")
    suspend fun login(@Body request: LoginRequest): Response<ApiResponse>

    @POST("/register")
    suspend fun register(@Body request: RegisterRequest): Response<ApiResponse>

    @POST("/perfil")
    suspend fun guardarPerfil(@Body request: UserProfileRequest): Response<ApiResponse>
}

// Singleton Retrofit client
object ApiClient {
    private const val BASE_URL = "http://10.0.2.2:8000/"

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
     */
    suspend fun checkUser(username: String, password: String): Boolean =
        withContext(Dispatchers.IO) {
            try {
                val response = service.login(LoginRequest(username, password))
                response.isSuccessful && response.body()?.success == true
            } catch (e: Exception) {
                Log.e("ApiHelper", "Error en login: ${e.message}")
                false
            }
        }

    /**
     * Registra nuevo usuario usando el endpoint /register
     */
    suspend fun registerUser(username: String, password: String): Boolean =
        withContext(Dispatchers.IO) {
            try {
                val response = service.register(RegisterRequest(username, password))
                response.isSuccessful && response.body()?.success == true
            } catch (e: Exception) {
                Log.e("ApiHelper", "Error en register: ${e.message}")
                false
            }
        }

    /**
     * Guarda perfil de usuario usando el endpoint /perfil
     */
    suspend fun guardarPerfil(perfil: UserProfile): Boolean =
        withContext(Dispatchers.IO) {
            try {
                val request = UserProfileRequest(
                    username = perfil.username,
                    talla = perfil.talla,
                    peso = perfil.peso,
                    edad = perfil.edad,
                    genero = perfil.genero,
                    meta = perfil.meta,
                    diasSemana = perfil.diasSemana,
                    nivel = perfil.nivel,
                    observaciones = perfil.observaciones
                )
                val response = service.guardarPerfil(request)
                response.isSuccessful && response.body()?.success == true
            } catch (e: Exception) {
                Log.e("ApiHelper", "Error en guardarPerfil: ${e.message}")
                false
            }
        }
}
