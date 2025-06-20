package com.example.crud_producto.repository

import android.util.Log
import com.example.crud_producto.model.LoginRequest
import com.example.crud_producto.model.RegisterRequest
import com.example.crud_producto.network.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LoginRepository {
    private val service = RetrofitClient.apiService

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
}