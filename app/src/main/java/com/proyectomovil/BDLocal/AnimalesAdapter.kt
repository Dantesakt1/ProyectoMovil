package com.proyectomovil.BDLocal

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.proyectomovil.R

class AnimalesAdapter(
    private var animales: List<AnimalApadrinado>,
    private val onClick: (AnimalApadrinado) -> Unit
) : RecyclerView.Adapter<AnimalesAdapter.AnimalViewHolder>() {

    inner class AnimalViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgAnimal: ImageView = view.findViewById(R.id.img_animal)
        val btnOpciones: ImageView = view.findViewById(R.id.btn_opciones) // Added this
        val txtNombre: TextView = view.findViewById(R.id.txt_nombre)
        val txtEspecie: TextView = view.findViewById(R.id.txt_especie)
        val badgeNuevo: TextView = view.findViewById(R.id.badge_nuevo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimalViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_animal, parent, false)
        return AnimalViewHolder(view)
    }

    override fun onBindViewHolder(holder: AnimalViewHolder, position: Int) {
        val animal = animales[position]

        holder.txtNombre.text = animal.nombre
        holder.txtEspecie.text = animal.especie
        holder.badgeNuevo.visibility = if (animal.tieneActualizacion) View.VISIBLE else View.GONE

        // Handle options button click
        holder.btnOpciones.setOnClickListener {
            onClick(animal)
        }

        // TODO: Load animal image using Glide or similar
        // Glide.with(holder.imgAnimal.context)
        //     .load(animal.imagen)
        //     .into(holder.imgAnimal)
    }

    override fun getItemCount(): Int = animales.size

    fun actualizarAnimales(nuevosAnimales: List<AnimalApadrinado>) {
        animales = nuevosAnimales
        notifyDataSetChanged()
    }
}