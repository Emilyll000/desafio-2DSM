package com.example.desafio2dsm

data class Destino(
    val id: String? = null,
    val nombre: String = "",
    val pais: String = "",
    val precio: Double = 0.0,
    val descripcion: String = "",
    val imagenUrl: String = ""
)