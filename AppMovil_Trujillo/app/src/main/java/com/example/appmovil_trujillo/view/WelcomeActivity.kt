package com.example.appmovil_trujillo.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.appmovil_trujillo.databinding.ActivityWelcomeBinding

class WelcomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = intent.getStringExtra("USERNAME") ?: "Usuario"
        binding.tvWelcome.text = "Bienvenido, $username a la AppMovil_Trujillo"
    }
}