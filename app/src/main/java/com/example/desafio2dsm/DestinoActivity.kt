package com.example.desafio2dsm

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class DestinoActivity : AppCompatActivity() {

    private lateinit var rvDestinos: RecyclerView
    private lateinit var progress: ProgressBar

    private val lista = mutableListOf<Destino>()
    private val adapter by lazy { DestinoAdapter(lista) }

    private val db: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_destino)

        rvDestinos = findViewById(R.id.rvDestinos)
        progress = findViewById(R.id.progress)

        rvDestinos.layoutManager = LinearLayoutManager(this)
        rvDestinos.setHasFixedSize(true)
        rvDestinos.adapter = adapter

        cargarDestinos()
    }

    private fun cargarDestinos() {
        progress.visibility = View.VISIBLE

        // ⚠️ NO usar argumentos con nombre aquí. Es Java API.
        db.collection("destinos")
            .get()
            .addOnSuccessListener { result ->
                lista.clear()
                for (document in result) {
                    // Opción 1: mapeo manual (seguro si los tipos varían)
                    val destino = Destino(
                        id = document.id,
                        nombre = document.getString("nombre") ?: "",
                        pais = document.getString("pais") ?: "",
                        precio = document.getDouble("precio") ?: 0.0,
                        descripcion = document.getString("descripcion") ?: "",
                        imagenUrl = document.getString("imagenUrl") ?: ""
                    )
                    lista.add(destino)

                    // --- Opción 2 (alternativa): usar toObject y luego copiar el id ---
                    // val d = document.toObject(Destino::class.java).copy(id = document.id)
                    // lista.add(d)
                }
                adapter.notifyDataSetChanged()
                progress.visibility = View.GONE

                if (lista.isEmpty()) {
                    Toast.makeText(this, getString(R.string.sin_datos), Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                progress.visibility = View.GONE
                Toast.makeText(
                    this,
                    getString(R.string.error_cargar, e.localizedMessage ?: ""),
                    Toast.LENGTH_LONG
                ).show()
            }
    }
}