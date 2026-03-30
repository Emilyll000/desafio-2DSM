package com.example.desafio2dsm

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.desafio2dsm.R

class DestinoAdapter(private val lista: MutableList<Destino>) :
    RecyclerView.Adapter<DestinoAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivImagen: ImageView = view.findViewById(R.id.ivImagen)
        val tvNombre: TextView  = view.findViewById(R.id.tvNombre)
        val tvPrecio: TextView  = view.findViewById(R.id.tvPrecio)
        val tvDescripcion: TextView = view.findViewById(R.id.tvDescripcion)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_destino, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val destino = lista[position]

        holder.tvNombre.text = destino.nombre
        holder.tvPrecio.text =
            holder.itemView.context.getString(R.string.precio_formato, destino.precio)
        holder.tvDescripcion.text = destino.descripcion

        Glide.with(holder.itemView.context)
            .load(destino.imagenUrl)
            .placeholder(R.drawable.placeholder)
            .into(holder.ivImagen)

        holder.itemView.setOnLongClickListener {
            Toast.makeText(
                holder.itemView.context,
                holder.itemView.context.getString(R.string.eliminar_label, destino.nombre),
                Toast.LENGTH_SHORT
            ).show()
            true
        }
    }

    override fun getItemCount(): Int = lista.size
}