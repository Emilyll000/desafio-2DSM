package com.example.desafio2dsm

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class AddDestinoActivity : AppCompatActivity() {

    private lateinit var etNombre: EditText
    private lateinit var etPais: EditText
    private lateinit var etPrecio: EditText
    private lateinit var etDescripcion: EditText
    private lateinit var etImagen: EditText
    private lateinit var btnGuardar: Button

    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_destino)

        etNombre = findViewById(R.id.etNombre)
        etPais = findViewById(R.id.etPais)
        etPrecio = findViewById(R.id.etPrecio)
        etDescripcion = findViewById(R.id.etDescripcion)
        etImagen = findViewById(R.id.etImagen)
        btnGuardar = findViewById(R.id.btnGuardar)

        db = FirebaseFirestore.getInstance()

        btnGuardar.setOnClickListener {
            guardarDestino()
        }
    }

    private fun guardarDestino() {

        val nombre = etNombre.text.toString()
        val pais = etPais.text.toString()
        val precio = etPrecio.text.toString()
        val descripcion = etDescripcion.text.toString()
        val imagen = etImagen.text.toString()

        if (nombre.isEmpty() || pais.isEmpty() || precio.isEmpty() || descripcion.isEmpty() || imagen.isEmpty()) {
            Toast.makeText(this, "No dejar campos vacíos", Toast.LENGTH_SHORT).show()
            return
        }

        if (descripcion.length < 20) {
            Toast.makeText(this, "Descripción muy corta", Toast.LENGTH_SHORT).show()
            return
        }

        val precioDouble = precio.toDoubleOrNull()
        if (precioDouble == null || precioDouble <= 0) {
            Toast.makeText(this, "Precio inválido", Toast.LENGTH_SHORT).show()
            return
        }

        val destino = hashMapOf(
            "nombre" to nombre,
            "pais" to pais,
            "precio" to precioDouble,
            "descripcion" to descripcion,
            "imagenUrl" to imagen
        )

        db.collection("destinos")
            .add(destino)
            .addOnSuccessListener {
                Toast.makeText(this, "Destino guardado", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error al guardar", Toast.LENGTH_SHORT).show()
            }
    }
}