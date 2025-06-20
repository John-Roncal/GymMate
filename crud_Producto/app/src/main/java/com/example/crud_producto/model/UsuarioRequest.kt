package com.example.crud_producto.model
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UsuarioRequest(
    val nombre: String,
    val edad: Int,
    val peso: Float,
    val talla: Float,
    val genero: String,
    val meta: String,
    val dias_gym: Int,
    val nivel: String,
    val observaciones: String?
) : Parcelable
