package com.example.desafio2dsm

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class DestinoActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: DestinoAdapter
    private val lista = mutableListOf<Destino>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_destino)

        recyclerView = findViewById(R.id.recyclerDestinos)
        val btnAgregar = findViewById<Button>(R.id.btnAgregar)

        adapter = DestinoAdapter(lista)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        btnAgregar.setOnClickListener {
            startActivity(Intent(this, AddDestinoActivity::class.java))
        }

        cargarDatos()
    }

    override fun onResume() {
        super.onResume()
        cargarDatos() //
    }

    private fun cargarDatos() {
        val db = FirebaseFirestore.getInstance()

        db.collection("destinos")
            .get()
            .addOnSuccessListener { result ->

                lista.clear()

                for (doc in result) {
                    val destino = doc.toObject(Destino::class.java)

                    destino.id = doc.id

                    lista.add(destino)
                }

                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }
}