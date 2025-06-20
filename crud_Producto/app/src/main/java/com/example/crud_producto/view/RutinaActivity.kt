package com.example.crud_producto.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.crud_producto.databinding.ActivityRutinaBinding
import com.example.crud_producto.model.UsuarioRequest
import com.example.crud_producto.adapter.RutinaAdapter
import com.example.crud_producto.viewmodel.PlanViewModel
import com.google.gson.Gson

class RutinaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRutinaBinding
    private val viewModel: PlanViewModel by viewModels()
    private lateinit var adapter: RutinaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRutinaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val usuario = intent.getParcelableExtra<UsuarioRequest>("usuario")
        if (usuario == null) {
            Toast.makeText(this, "Datos de usuario no encontrados", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        setupRecyclerView()
        observarViewModel()
        viewModel.generarPlan(usuario)
    }

    private fun setupRecyclerView() {
        adapter = RutinaAdapter(emptyList(), onItemClick = { rutina ->
            val intent = Intent(this, RutinaDetalleActivity::class.java)
            val ejerciciosJson = Gson().toJson(rutina.ejercicios)
            intent.putExtra("ejercicios", ejerciciosJson)
            startActivity(intent)
        })
        binding.rvRutina.layoutManager = LinearLayoutManager(this)
        binding.rvRutina.adapter = adapter
    }

    private fun observarViewModel() {
        viewModel.respuesta.observe(this) { plan ->
            binding.tvNutricion.text = plan.nutricion
            adapter.actualizarLista(plan.rutina)
        }

        viewModel.error.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        }
    }
}
