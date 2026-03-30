package com.example.desafio2dsm

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class DestinoActivity : AppCompatActivity() {

    private lateinit var recycler: RecyclerView
    private lateinit var adapter: DestinoAdapter
    private val lista = mutableListOf<Destino>()
    private lateinit var db: FirebaseFirestore
    private var listener: ListenerRegistration? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_destino)

        recycler = findViewById(R.id.recyclerDestinos)
        recycler.layoutManager = LinearLayoutManager(this)

        adapter = DestinoAdapter(lista)
        recycler.adapter = adapter

        db = FirebaseFirestore.getInstance()

        listener = db.collection("destinos")
            .addSnapshotListener { snapshots, e ->
                if (e != null) return@addSnapshotListener

                lista.clear()
                for (doc in snapshots!!) {
                    val destino = doc.toObject(Destino::class.java)
                    destino.id = doc.id
                    lista.add(destino)
                }
                adapter.notifyDataSetChanged()
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        listener?.remove()
    }
}