package com.example.desafio2dsm

import android.app.AlertDialog
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore

class DestinoAdapter(private val lista: MutableList<Destino>) :
    RecyclerView.Adapter<DestinoAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNombre: TextView = view.findViewById(R.id.tvNombre)
        val tvPrecio: TextView = view.findViewById(R.id.tvPrecio)
        val tvDescripcion: TextView = view.findViewById(R.id.tvDescripcion)
        val imgDestino: ImageView = view.findViewById(R.id.imgDestino)
        val btnEditar: Button = view.findViewById(R.id.btnEditar)
        val btnEliminar: Button = view.findViewById(R.id.btnEliminar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_destino, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = lista.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val destino = lista[position]
        val context = holder.itemView.context

        holder.tvNombre.text = destino.nombre
        holder.tvPrecio.text = "$${destino.precio}"
        holder.tvDescripcion.text = destino.descripcion

        Glide.with(context)
            .load(destino.imagenUrl)
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.placeholder)
            .into(holder.imgDestino)

        holder.btnEliminar.setOnClickListener {

            val db = FirebaseFirestore.getInstance()

            AlertDialog.Builder(context)
                .setTitle("Eliminar destino")
                .setMessage("¿Seguro que deseas eliminar este destino?")
                .setPositiveButton("Sí") { _, _ ->

                    db.collection("destinos")
                        .document(destino.id)
                        .delete()
                        .addOnSuccessListener {

                            lista.removeAt(position)
                            notifyItemRemoved(position)

                            Toast.makeText(context, "Destino eliminado", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                        }
                }
                .setNegativeButton("Cancelar", null)
                .show()
        }

        holder.btnEditar.setOnClickListener {

            val intent = Intent(context, EditDestinoActivity::class.java)

            intent.putExtra("id", destino.id)
            intent.putExtra("nombre", destino.nombre)
            intent.putExtra("pais", destino.pais)
            intent.putExtra("precio", destino.precio)
            intent.putExtra("descripcion", destino.descripcion)
            intent.putExtra("imagenUrl", destino.imagenUrl)

            context.startActivity(intent)
        }
    }
}