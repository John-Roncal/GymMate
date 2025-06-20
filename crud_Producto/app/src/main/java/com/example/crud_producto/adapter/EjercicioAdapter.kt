package com.example.crud_producto.adapter

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.crud_producto.R
import com.example.crud_producto.model.EjercicioDetalle

class EjercicioAdapter(
    private val context: Context,
    private var lista: List<EjercicioDetalle>
) : RecyclerView.Adapter<EjercicioAdapter.EjercicioViewHolder>() {

    inner class EjercicioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvNombre: TextView = itemView.findViewById(R.id.tvNombreEjercicio)
        private val tvRepeticiones: TextView = itemView.findViewById(R.id.tvRepeticionesEjercicio)

        fun bind(item: EjercicioDetalle) {
            tvNombre.text = item.nombre
            tvRepeticiones.text = item.repeticiones

            itemView.setOnClickListener {
                mostrarDialogoDetalle(item)
            }
        }

        private fun mostrarDialogoDetalle(ejercicio: EjercicioDetalle) {
            AlertDialog.Builder(context)
                .setTitle(ejercicio.nombre)
                .setMessage(
                    "Descripción:\n${ejercicio.descripcion}\n\nRepeticiones:\n${ejercicio.repeticiones}"
                )
                .setPositiveButton("Cerrar", null)
                .show()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EjercicioViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_ejercicio, parent, false)
        return EjercicioViewHolder(view)
    }

    override fun onBindViewHolder(holder: EjercicioViewHolder, position: Int) {
        holder.bind(lista[position])
    }

    override fun getItemCount(): Int = lista.size

    fun actualizarLista(nuevaLista: List<EjercicioDetalle>) {
        lista = nuevaLista
        notifyDataSetChanged()
    }
}
