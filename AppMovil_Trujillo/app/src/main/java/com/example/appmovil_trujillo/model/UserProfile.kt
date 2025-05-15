package com.example.appmovil_trujillo.model

data class UserProfile(
    val username: String,
    val talla: Float,
    val peso: Float,
    val edad: Int,
    val genero: String,
    val meta: String,
    val diasSemana: Int,
    val nivel: String,
    val observaciones: String
)
