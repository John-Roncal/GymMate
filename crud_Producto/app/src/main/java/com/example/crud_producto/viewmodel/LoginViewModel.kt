package com.example.crud_producto.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.LiveData
import com.example.crud_producto.repository.LoginRepository
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    private val repository = LoginRepository()

    private val _loginExitoso = MutableLiveData<Boolean>()
    val loginExitoso: LiveData<Boolean> = _loginExitoso

    private val _registroExitoso = MutableLiveData<Boolean>()
    val registroExitoso: LiveData<Boolean> = _registroExitoso

    private val _mensaje = MutableLiveData<String>()
    val mensaje: LiveData<String> = _mensaje

    private val _usuarioId = MutableLiveData<Int>()
    val usuarioId: LiveData<Int> get() = _usuarioId

    fun login(usuario: String, contraseña: String) {
        if (usuario.isBlank() || contraseña.isBlank()) {
            _mensaje.value = "Completa todos los campos"
            return
        }

        viewModelScope.launch {
            val result = repository.checkUser(usuario, contraseña)
            if (result != null && result.success) {
                _usuarioId.value = result.usuario_id
                _loginExitoso.value = true
            } else {
                _mensaje.value = "Credenciales inválidas"
            }
        }
    }

    fun registrar(username: String, password: String) {
        if (username.isBlank() || password.isBlank()) {
            _mensaje.value = "Completa todos los campos"
            return
        }

        viewModelScope.launch {
            val response = repository.registerUser(username, password)
            if (response != null && response.success) {
                _usuarioId.value = response.usuario_id
                _registroExitoso.value = true
            } else {
                _mensaje.value = "Error al registrar usuario"
                _registroExitoso.value = false
            }
        }
    }

    fun limpiarMensajes() {
        _mensaje.value = ""
        _loginExitoso.value = false
        _registroExitoso.value = false
    }
}
