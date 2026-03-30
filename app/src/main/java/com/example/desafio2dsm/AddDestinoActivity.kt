package com.example.desafio2dsm

import android.os.Bundle
import android.util.Patterns
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class AddDestinoActivity : AppCompatActivity() {

    private lateinit var etNombre: EditText
    private lateinit var etPrecio: EditText
    private lateinit var etDescripcion: EditText
    private lateinit var etImagen: EditText
    private lateinit var spinnerPais: Spinner
    private lateinit var btnGuardar: Button

    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_destino)

        etNombre = findViewById(R.id.etNombre)
        etPrecio = findViewById(R.id.etPrecio)
        etDescripcion = findViewById(R.id.etDescripcion)
        etImagen = findViewById(R.id.etImagen)
        spinnerPais = findViewById(R.id.spinnerPais)
        btnGuardar = findViewById(R.id.btnGuardar)

        db = FirebaseFirestore.getInstance()

        val paises = listOf("El Salvador", "Guatemala", "Honduras", "Nicaragua", "Costa Rica")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, paises)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerPais.adapter = adapter

        btnGuardar.setOnClickListener { guardarDestino() }
    }

    private fun guardarDestino() {
        val nombre = etNombre.text.toString().trim()
        val pais = spinnerPais.selectedItem.toString()
        val precio = etPrecio.text.toString().trim()
        val descripcion = etDescripcion.text.toString().trim()
        val imagen = etImagen.text.toString().trim()

        if (nombre.isEmpty()) { etNombre.error = "No puede estar vacío"; return }
        if (descripcion.isEmpty()) { etDescripcion.error = "No puede estar vacío"; return }
        if (descripcion.length < 20) { etDescripcion.error = "Descripción muy corta"; return }
        val precioDouble = precio.toDoubleOrNull()
        if (precioDouble == null || precioDouble <= 0) { etPrecio.error = "Precio inválido"; return }
        if (imagen.isEmpty()) { etImagen.error = "Debe ingresar la URL"; return }
        if (!Patterns.WEB_URL.matcher(imagen).matches()) { etImagen.error = "URL inválida"; return }

        val destino = hashMapOf(
            "nombre" to nombre,
            "pais" to pais,
            "precio" to precioDouble,
            "descripcion" to descripcion,
            "imagenUrl" to imagen
        )

        db.collection("destinos")
            .add(destino)
            .addOnSuccessListener { Toast.makeText(this, "Destino guardado", Toast.LENGTH_SHORT).show(); finish() }
            .addOnFailureListener { Toast.makeText(this, "Error al guardar", Toast.LENGTH_SHORT).show() }
    }
}