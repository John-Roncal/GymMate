package com.example.crud_producto.view

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.crud_producto.R
import com.example.crud_producto.viewmodel.LoginViewModel

class RegisterActivity : AppCompatActivity() {

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val etUsuario = findViewById<EditText>(R.id.etUsername)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnRegister = findViewById<Button>(R.id.btnRegister)

        btnRegister.setOnClickListener {
            val user = etUsuario.text.toString()
            val pass = etPassword.text.toString()
            viewModel.registrar(user, pass)
        }

        viewModel.registroExitoso.observe(this) { exito ->
            if (exito) {
                Toast.makeText(this, "Registro exitoso. Ahora ingresa tus datos para crear tu rutina.", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, FormularioUsuarioActivity::class.java))
                finish()
            }
        }

        viewModel.mensaje.observe(this) { mensaje ->
            if (mensaje.isNotEmpty()) {
                Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
                viewModel.limpiarMensajes()
            }
        }
    }
}
