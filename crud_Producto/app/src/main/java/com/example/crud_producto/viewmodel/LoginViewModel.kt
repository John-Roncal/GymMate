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

    fun login(usuario: String, contraseña: String) {
        if (usuario.isBlank() || contraseña.isBlank()) {
            _mensaje.value = "Completa todos los campos"
            return
        }

        viewModelScope.launch {
            val result = repository.checkUser(usuario, contraseña)
            if (result) {
                _loginExitoso.value = true
            } else {
                _mensaje.value = "Credenciales inválidas"
            }
        }
    }

    fun registrar(usuario: String, contraseña: String) {
        if (usuario.isBlank() || contraseña.isBlank()) {
            _mensaje.value = "Completa todos los campos"
            return
        }

        viewModelScope.launch {
            val result = repository.registerUser(usuario, contraseña)
            if (result) {
                _registroExitoso.value = true
            } else {
                _mensaje.value = "Error al registrar"
            }
        }
    }

    fun limpiarMensajes() {
        _mensaje.value = ""
        _loginExitoso.value = false
        _registroExitoso.value = false
    }
}
