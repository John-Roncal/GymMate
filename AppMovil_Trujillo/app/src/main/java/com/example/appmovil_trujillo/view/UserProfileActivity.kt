package com.example.appmovil_trujillo.view

import android.R
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.appmovil_trujillo.api.ApiHelper
import com.example.appmovil_trujillo.databinding.ActivityUserProfileBinding
import com.example.appmovil_trujillo.model.UserProfile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserProfileBinding
    private lateinit var apiHelper: ApiHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        apiHelper = ApiHelper()

        // Configura los spinners
        val generos = arrayOf("Masculino", "Femenino", "Otro")
        val metas = arrayOf("Bajar de peso", "Ganar masa muscular", "Mantenerse")
        val niveles = arrayOf("Principiante", "Intermedio", "Avanzado")

        binding.spGenero.adapter = ArrayAdapter(this, R.layout.simple_spinner_dropdown_item, generos)
        binding.spMeta.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, metas)
        binding.spNivel.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, niveles)

        binding.btnGuardarPerfil.setOnClickListener {
            val perfil = UserProfile(
                username = intent.getStringExtra("USERNAME") ?: "",
                talla = binding.etTalla.text.toString().toFloatOrNull() ?: 0f,
                peso = binding.etPeso.text.toString().toFloatOrNull() ?: 0f,
                edad = binding.etEdad.text.toString().toIntOrNull() ?: 0,
                genero = binding.spGenero.selectedItem.toString(),
                meta = binding.spMeta.selectedItem.toString(),
                diasSemana = binding.etDiasSemana.text.toString().toIntOrNull() ?: 0,
                nivel = binding.spNivel.selectedItem.toString(),
                observaciones = binding.etObservaciones.text.toString()
            )

            CoroutineScope(Dispatchers.IO).launch {
                val result = apiHelper.guardarPerfil(perfil)
                withContext(Dispatchers.Main) {
                    if (result) {
                        Toast.makeText(this@UserProfileActivity, "Perfil guardado", Toast.LENGTH_SHORT).show()
                        finish() // o navegar a la pantalla principal
                    } else {
                        Toast.makeText(this@UserProfileActivity, "Error al guardar perfil", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}
