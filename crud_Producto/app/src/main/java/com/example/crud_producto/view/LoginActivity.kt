package com.example.crud_producto.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.crud_producto.R
import com.example.crud_producto.viewmodel.LoginViewModel
import com.google.android.material.textfield.TextInputEditText

class LoginActivity : AppCompatActivity() {

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val etUsuario = findViewById<TextInputEditText>(R.id.etUsuario)
        val etPassword = findViewById<TextInputEditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val btnRegister = findViewById<Button>(R.id.btnGoToRegister)

        btnLogin.setOnClickListener {
            val user = etUsuario.text.toString()
            val pass = etPassword.text.toString()
            viewModel.login(user, pass)
        }

        btnRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        viewModel.loginExitoso.observe(this) { exito ->
            if (exito) {
                val intent = Intent(this, RutinaActivity::class.java)
                intent.putExtra("usuario_id", viewModel.usuarioId.value ?: -1)
                startActivity(intent)
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
