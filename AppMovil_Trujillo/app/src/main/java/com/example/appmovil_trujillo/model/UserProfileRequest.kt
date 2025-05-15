package com.example.appmovil_trujillo.model

data class UserProfileRequest(
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