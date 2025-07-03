package com.example.crud_producto.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.crud_producto.R
import com.example.crud_producto.model.Mensaje

class ChatAdapter(private val mensajes: List<Mensaje>) : RecyclerView.Adapter<ChatAdapter.MensajeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MensajeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            if (viewType == 0) R.layout.item_mensaje_usuario else R.layout.item_mensaje_bot,
            parent,
            false
        )
        return MensajeViewHolder(view)
    }

    override fun onBindViewHolder(holder: MensajeViewHolder, position: Int) {
        holder.bind(mensajes[position])
    }

    override fun getItemViewType(position: Int): Int {
        return if (mensajes[position].esUsuario) 0 else 1
    }

    override fun getItemCount(): Int = mensajes.size

    class MensajeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val texto: TextView = itemView.findViewById(R.id.tvMensaje)
        fun bind(mensaje: Mensaje) {
            texto.text = mensaje.contenido
        }
    }
}