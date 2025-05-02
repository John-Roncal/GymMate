package com.example.appmovil_trujillo

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.appmovil_trujillo.api.ApiHelper
import com.example.appmovil_trujillo.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var apiHelper: ApiHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        apiHelper = ApiHelper()

        // Configurar botón de inicio de sesión
        binding.btnLogin.setOnClickListener {
            val username = binding.etUsuario.text.toString()
            val password = binding.etPassword.text.toString()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Verificar credenciales en un hilo secundario
            CoroutineScope(Dispatchers.IO).launch {
                val isValidUser = apiHelper.checkUser(username, password)

                withContext(Dispatchers.Main) {
                    if (isValidUser) {
                        // Guardar preferencia de recordar usuario si el checkbox está marcado
                        if (binding.cbRecordarme.isChecked) {
                            val sharedPref = getSharedPreferences("login_prefs", MODE_PRIVATE)
                            with(sharedPref.edit()) {
                                putString("username", username)
                                apply()
                            }
                        }

                        // Ir a la pantalla de bienvenida
                        val intent = Intent(this@MainActivity, WelcomeActivity::class.java)
                        intent.putExtra("USERNAME", username)
                        startActivity(intent)
                    } else {
                        Toast.makeText(
                            this@MainActivity,
                            "Credenciales incorrectas",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

        // Configurar botones de redes sociales
        binding.btnFacebook.setOnClickListener {
            openWebUrl("https://www.facebook.com")
        }

        binding.btnInstagram.setOnClickListener {
            openWebUrl("https://www.instagram.com")
        }

        binding.btnX.setOnClickListener {
            openWebUrl("https://www.x.com")
        }

        // Verificar si hay un usuario guardado
        checkRememberedUser()
    }

    private fun checkRememberedUser() {
        val sharedPref = getSharedPreferences("login_prefs", MODE_PRIVATE)
        val savedUsername = sharedPref.getString("username", null)

        if (savedUsername != null) {
            binding.etUsuario.setText(savedUsername)
            binding.cbRecordarme.isChecked = true
        }
    }

    private fun openWebUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }
}