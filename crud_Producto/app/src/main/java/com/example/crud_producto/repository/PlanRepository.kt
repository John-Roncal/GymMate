package com.example.crud_producto.repository

import android.util.Log
import com.example.crud_producto.model.RespuestaIA
import com.example.crud_producto.model.UsuarioRequest
import com.example.crud_producto.network.RetrofitClient
import retrofit2.Response

class PlanRepository {
    suspend fun generarPlan(usuario: UsuarioRequest): Response<RespuestaIA> {
        return RetrofitClient.apiService.generarPlan(usuario)
    }

    suspend fun obtenerPlan(usuarioId: Int): Response<RespuestaIA> {
        return return RetrofitClient.apiService.obtenerPlanDesdeBD(usuarioId)
    }
}
