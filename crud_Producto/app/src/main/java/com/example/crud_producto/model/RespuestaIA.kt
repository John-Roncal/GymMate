package com.example.crud_producto.model

data class RespuestaIA(
    val rutina: List<RutinaDetalle>,
    val nutricion: String
)