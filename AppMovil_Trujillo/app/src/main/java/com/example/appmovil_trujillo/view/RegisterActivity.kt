package com.example.appmovil_trujillo.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.appmovil_trujillo.R
import com.example.appmovil_trujillo.api.ApiHelper
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnRegister: Button
    private val apiHelper = ApiHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        etUsername = findViewById(R.id.etUsername)
        etPassword = findViewById(R.id.etPassword)
        btnRegister = findViewById(R.id.btnRegister)

        btnRegister.setOnClickListener {
            val user = etUsername.text.toString().trim()
            val pass = etPassword.text.toString().trim()
            if (user.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Rellena todos los campos", Toast.LENGTH_SHORT).show()
            } else {
                registerUser(user, pass)
            }
        }
    }

    private fun registerUser(username: String, password: String) {
        lifecycleScope.launch {
            val success = apiHelper.registerUser(username, password)
            if (success) {
                Toast.makeText(this@RegisterActivity, "Registro exitoso", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@RegisterActivity, UserProfileActivity::class.java)
                intent.putExtra("USERNAME", username) // si lo necesitas para asociar el perfil
                startActivity(intent)
                finish()

            } else {
                Toast.makeText(this@RegisterActivity, "Error al registrar", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
