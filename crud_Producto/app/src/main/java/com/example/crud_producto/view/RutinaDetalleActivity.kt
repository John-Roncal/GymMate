package com.example.crud_producto.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.crud_producto.adapter.EjercicioAdapter
import com.example.crud_producto.databinding.ActivityRutinaDetalleBinding
import com.example.crud_producto.model.EjercicioDetalle
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class RutinaDetalleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRutinaDetalleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRutinaDetalleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtener la lista de ejercicios pasada como JSON
        val ejerciciosJson = intent.getStringExtra("ejercicios") ?: "[]"
        val tipoLista = object : TypeToken<List<EjercicioDetalle>>() {}.type
        val ejercicios: List<EjercicioDetalle> = Gson().fromJson(ejerciciosJson, tipoLista)

        // Configurar el RecyclerView con EjercicioAdapter
        val adapter = EjercicioAdapter(this, ejercicios)
        binding.recyclerEjercicios.layoutManager = LinearLayoutManager(this)
        binding.recyclerEjercicios.adapter = adapter
    }
}