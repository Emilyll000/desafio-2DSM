package com.example.desafio2dsm

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FirebaseFirestore

class EditDestinoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_destino)

        val id = intent.getStringExtra("id")!!

        val etNombre = findViewById<TextInputEditText>(R.id.etNombre)
        val spPais = findViewById<Spinner>(R.id.spPais)
        val etPrecio = findViewById<TextInputEditText>(R.id.etPrecio)
        val etDescripcion = findViewById<TextInputEditText>(R.id.etDescripcion)
        val btnGuardar = findViewById<Button>(R.id.btnGuardar)
        val progress = findViewById<ProgressBar>(R.id.progress)

        val paises = arrayOf(
            "Seleccione país",
            "El Salvador",
            "México",
            "España",
            "Estados Unidos"
        )

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, paises)
        spPais.adapter = adapter

        etNombre.setText(intent.getStringExtra("nombre"))
        etPrecio.setText(intent.getDoubleExtra("precio", 0.0).toString())
        etDescripcion.setText(intent.getStringExtra("descripcion"))

        btnGuardar.setOnClickListener {

            val nombre = etNombre.text.toString()
            val pais = spPais.selectedItem.toString()
            val precio = etPrecio.text.toString().toDoubleOrNull()
            val descripcion = etDescripcion.text.toString()

            if (nombre.isEmpty() || precio == null || descripcion.length < 5) {
                Toast.makeText(this, "Completa correctamente", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            progress.visibility = View.VISIBLE

            val db = FirebaseFirestore.getInstance()

            val datos = hashMapOf(
                "nombre" to nombre,
                "pais" to pais,
                "precio" to precio,
                "descripcion" to descripcion
            )

            db.collection("destinos")
                .document(id)
                .update(datos as Map<String, Any>)
                .addOnSuccessListener {
                    progress.visibility = View.GONE
                    Toast.makeText(this, "Actualizado", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener {
                    progress.visibility = View.GONE
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
                }
        }
    }
}