package com.example.crud_producto.adapter

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.crud_producto.R
import com.example.crud_producto.model.EjercicioDetalle
import com.example.crud_producto.model.RutinaDetalle

class RutinaAdapter(
    private var lista: List<RutinaDetalle>,
    private val onItemClick: (RutinaDetalle) -> Unit
) : RecyclerView.Adapter<RutinaAdapter.RutinaViewHolder>() {

    inner class RutinaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvNombre: TextView = itemView.findViewById(R.id.tvNombre)

        fun bind(item: RutinaDetalle) {
            tvNombre.text = item.nombre
            // Set click listener to trigger navigation
            itemView.setOnClickListener {
                onItemClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RutinaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_rutina, parent, false)
        return RutinaViewHolder(view)
    }

    override fun onBindViewHolder(holder: RutinaViewHolder, position: Int) {
        holder.bind(lista[position])
    }

    override fun getItemCount(): Int = lista.size

    fun actualizarLista(nuevaLista: List<RutinaDetalle>) {
        lista = nuevaLista
        notifyDataSetChanged()
    }

    inner class EjercicioAdapter(
        private val ejercicios: List<EjercicioDetalle>
    ) : RecyclerView.Adapter<EjercicioAdapter.EjercicioViewHolder>() {

        inner class EjercicioViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            private val tvEjercicio: TextView = view.findViewById(R.id.tvNombreEjercicio)

            fun bind(ejercicio: EjercicioDetalle) {
                tvEjercicio.text = ejercicio.nombre
                itemView.setOnClickListener {
                    val context = itemView.context
                    AlertDialog.Builder(context)
                        .setTitle(ejercicio.nombre)
                        .setMessage("Descripción: ${ejercicio.descripcion}\n\nRepeticiones: ${ejercicio.repeticiones}")
                        .setPositiveButton("Cerrar", null)
                        .show()
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EjercicioViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_ejercicio, parent, false)
            return EjercicioViewHolder(view)
        }

        override fun onBindViewHolder(holder: EjercicioViewHolder, position: Int) {
            holder.bind(ejercicios[position])
        }

        override fun getItemCount(): Int = ejercicios.size
    }
}
