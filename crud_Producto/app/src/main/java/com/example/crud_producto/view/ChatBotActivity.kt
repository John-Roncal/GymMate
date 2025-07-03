package com.example.crud_producto.view

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.crud_producto.R
import com.example.crud_producto.databinding.ActivityChatBotBinding
import com.example.crud_producto.model.Mensaje
import com.example.crud_producto.adapter.ChatAdapter
import com.example.crud_producto.model.PreguntaRequest
import com.example.crud_producto.network.RetrofitClient
import kotlinx.coroutines.launch

class ChatBotActivity : BaseActivity() {

    private lateinit var binding: ActivityChatBotBinding
    private val listaMensajes = mutableListOf<Mensaje>()
    private lateinit var adapter: ChatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBotBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            title = "GymMate"
            setDisplayHomeAsUpEnabled(true)
        }
        adapter = ChatAdapter(listaMensajes)
        binding.recyclerMensajes.layoutManager = LinearLayoutManager(this)
        binding.recyclerMensajes.adapter = adapter

        binding.btnEnviar.setOnClickListener {
            val texto = binding.etMensaje.text.toString()
            if (texto.isNotBlank()) {
                enviarMensaje(texto)
                binding.etMensaje.text?.clear()
            }
        }
    }

    private fun enviarMensaje(pregunta: String) {
        listaMensajes.add(Mensaje(pregunta, true))
        adapter.notifyItemInserted(listaMensajes.size - 1)
        binding.recyclerMensajes.scrollToPosition(listaMensajes.size - 1)

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.consultarChatbot(PreguntaRequest(pregunta))
                if (response.isSuccessful && response.body() != null) {
                    val respuestaBot = response.body()!!.respuesta
                    listaMensajes.add(Mensaje(respuestaBot, false))
                } else {
                    listaMensajes.add(Mensaje("No pude responder esa pregunta.", false))
                }
            } catch (e: Exception) {
                listaMensajes.add(Mensaje("Error al contactar al bot: ${e.message}", false))
            }
            adapter.notifyItemInserted(listaMensajes.size - 1)
            binding.recyclerMensajes.scrollToPosition(listaMensajes.size - 1)
        }
    }

}