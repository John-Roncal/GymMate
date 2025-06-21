package com.example.crud_producto.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.crud_producto.databinding.ActivityFormularioUsuarioBinding
import com.example.crud_producto.model.UsuarioRequest

class FormularioUsuarioActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFormularioUsuarioBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormularioUsuarioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnGenerar.setOnClickListener {
            val nombre = binding.etNombre.text.toString()
            val edad = binding.etEdad.text.toString().toIntOrNull()
            val peso = binding.etPeso.text.toString().toFloatOrNull()
            val talla = binding.etTalla.text.toString().toFloatOrNull()
            val genero = binding.spGenero.selectedItem.toString()
            val meta = binding.spMeta.selectedItem.toString()
            val diasGym = binding.etDias.text.toString().toIntOrNull()
            val nivel = binding.spNivel.selectedItem.toString()
            val observaciones = binding.etObservaciones.text.toString()

            if (nombre.isBlank() || edad == null || peso == null || talla == null ||
                genero.isBlank() || meta.isBlank() || diasGym == null || nivel.isBlank()) {
                Toast.makeText(this, "Completa todos los campos correctamente", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val usuario = UsuarioRequest(
                nombre = nombre,
                edad = edad,
                peso = peso,
                talla = talla,
                genero = genero,
                meta = meta,
                dias_gym = diasGym,
                nivel = nivel,
                observaciones = if (observaciones.isBlank()) null else observaciones
            )

            val intent = Intent(this, RutinaActivity::class.java)
            intent.putExtra("usuario", usuario)
            startActivity(intent)
        }
    }
}
