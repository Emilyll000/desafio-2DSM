package com.example.desafio2dsm

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    private lateinit var btnAgregar: Button
    private lateinit var recycler: RecyclerView
    private val lista = mutableListOf<Destino>()
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnAgregar = findViewById(R.id.btnAgregar)
        recycler = findViewById(R.id.recyclerDestinos)

        recycler.layoutManager = LinearLayoutManager(this)

        db = FirebaseFirestore.getInstance()

        btnAgregar.setOnClickListener {
            startActivity(Intent(this, AddDestinoActivity::class.java))
        }

        obtenerDatos()
    }

    private fun obtenerDatos() {
        db.collection("destinos")
            .get()
            .addOnSuccessListener { result ->
                lista.clear()
                for (doc in result) {
                    val destino = doc.toObject(Destino::class.java)
                    lista.add(destino)
                }
                recycler.adapter = DestinoAdapter(lista)
            }
    }
}