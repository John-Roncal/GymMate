package com.example.crud_producto.model

data class RutinaDetalle(
    val nombre: String,
    val descripcion: String,
    val ejercicios: List<EjercicioDetalle>
)