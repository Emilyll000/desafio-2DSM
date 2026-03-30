package com.example.desafio2dsm

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FirebaseFirestore

class AddDestinoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_destino)

        val etNombre = findViewById<TextInputEditText>(R.id.etNombre)
        val spPais = findViewById<Spinner>(R.id.spPais)
        val etPrecio = findViewById<TextInputEditText>(R.id.etPrecio)
        val etDescripcion = findViewById<TextInputEditText>(R.id.etDescripcion)
        val etImagenUrl = findViewById<TextInputEditText>(R.id.etImagenUrl)
        val btnGuardar = findViewById<Button>(R.id.btnGuardar)
        val progress = findViewById<ProgressBar>(R.id.progress)

        val paises = arrayOf(
            "Seleccione país",
            "El Salvador",
            "México",
            "España",
            "Estados Unidos",
            "Colombia",
            "Argentina"
        )

        val adapterSpinner = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            paises
        )

        spPais.adapter = adapterSpinner

        btnGuardar.setOnClickListener {

            val nombre = etNombre.text.toString().trim()
            val pais = spPais.selectedItem.toString()
            val precio = etPrecio.text.toString().toDoubleOrNull()
            val descripcion = etDescripcion.text.toString().trim()
            val imagenUrl = etImagenUrl.text.toString().trim()

            if (nombre.isEmpty()) {
                etNombre.error = "Campo obligatorio"
                return@setOnClickListener
            }

            if (pais == "Seleccione país") {
                Toast.makeText(this, "Seleccione un país", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (precio == null || precio <= 0) {
                etPrecio.error = "Precio inválido"
                return@setOnClickListener
            }

            if (descripcion.length < 20) {
                etDescripcion.error = "Mínimo 20 caracteres"
                return@setOnClickListener
            }

            if (imagenUrl.isEmpty()) {
                etImagenUrl.error = "Ingrese URL de imagen"
                return@setOnClickListener
            }

            progress.visibility = View.VISIBLE

            guardarEnFirebase(nombre, pais, precio, descripcion, imagenUrl, progress)
        }
    }

    private fun guardarEnFirebase(
        nombre: String,
        pais: String,
        precio: Double,
        descripcion: String,
        imagenUrl: String,
        progress: ProgressBar
    ) {
        val db = FirebaseFirestore.getInstance()

        val destino = hashMapOf(
            "nombre" to nombre,
            "pais" to pais,
            "precio" to precio,
            "descripcion" to descripcion,
            "imagenUrl" to imagenUrl
        )

        db.collection("destinos")
            .add(destino)
            .addOnSuccessListener {
                progress.visibility = View.GONE
                Toast.makeText(this, "Destino guardado", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener {
                progress.visibility = View.GONE
                Toast.makeText(this, "Error al guardar", Toast.LENGTH_SHORT).show()
            }
    }
}