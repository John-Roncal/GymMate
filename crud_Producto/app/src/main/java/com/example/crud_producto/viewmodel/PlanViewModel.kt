package com.example.crud_producto.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.crud_producto.model.RespuestaIA
import com.example.crud_producto.model.UsuarioRequest
import com.example.crud_producto.repository.PlanRepository
import kotlinx.coroutines.launch

class PlanViewModel : ViewModel() {
    private val repository = PlanRepository()

    private val _respuesta = MutableLiveData<RespuestaIA?>()
    val respuesta: LiveData<RespuestaIA?> = _respuesta

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun generarPlan(usuario: UsuarioRequest) {
        viewModelScope.launch {
            try {
                val response = repository.generarPlan(usuario)
                if (response.isSuccessful) {
                    response.body()?.let {
                        _respuesta.postValue(it)
                    } ?: _error.postValue("Respuesta vacía del servidor")
                } else {
                    _error.postValue("Error: ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun cargarPlanGuardado(usuarioId: Int) {
        viewModelScope.launch {
            try {
                val response = repository.obtenerPlan(usuarioId)
                if (response.isSuccessful) {
                    _respuesta.postValue(response.body())
                } else {
                    _error.postValue("Error al obtener plan guardado")
                }
            } catch (e: Exception) {
                _error.postValue("Error: ${e.message}")
            }
        }
    }
}
