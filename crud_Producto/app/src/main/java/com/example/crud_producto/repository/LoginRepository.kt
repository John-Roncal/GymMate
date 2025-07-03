package com.example.crud_producto.repository

import android.util.Log
import com.example.crud_producto.model.ApiResponse
import com.example.crud_producto.model.LoginRequest
import com.example.crud_producto.model.RegisterRequest
import com.example.crud_producto.network.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LoginRepository {
    private val service = RetrofitClient.apiService

    suspend fun checkUser(username: String, password: String): ApiResponse? =
        withContext(Dispatchers.IO) {
            try {
                val response = service.login(LoginRequest(username, password))
                if (response.isSuccessful) {
                    return@withContext response.body()
                } else {
                    Log.e("ApiService", "Error en login: ${response.errorBody()?.string()}")
                    return@withContext null
                }
            } catch (e: Exception) {
                Log.e("ApiService", "Error en login: ${e.message}")
                return@withContext null
            }
        }

    suspend fun registerUser(username: String, password: String): ApiResponse? =
        withContext(Dispatchers.IO) {
            try {
                val response = service.register(RegisterRequest(username, password))
                if (response.isSuccessful) {
                    return@withContext response.body()
                } else {
                    Log.e("ApiService", "Error en register: ${response.errorBody()?.string()}")
                    return@withContext null
                }
            } catch (e: Exception) {
                Log.e("ApiService", "Error en register: ${e.message}")
                return@withContext null
            }
        }
}