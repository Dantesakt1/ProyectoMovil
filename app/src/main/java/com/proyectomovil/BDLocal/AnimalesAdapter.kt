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

    class AnimalViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgAnimal: ImageView = view.findViewById(R.id.img_animal)
        val txtNombre: TextView = view.findViewById(R.id.txt_nombre)
        val txtEspecie: TextView = view.findViewById(R.id.txt_especie)
        val badgeNuevo: TextView = view.findViewById(R.id.badge_nuevo)
        val btnOpciones: ImageView = view.findViewById(R.id.btn_opciones)
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

        // Mostrar badge si tiene actualización
        holder.badgeNuevo.visibility = if (animal.tieneActualizacion) View.VISIBLE else View.GONE

        // TODO: Cargar imagen con Glide o Picasso
        // Glide.with(holder.itemView).load(animal.imagen).into(holder.imgAnimal)

        holder.itemView.setOnClickListener {
            onClick(animal)
        }

        holder.btnOpciones.setOnClickListener {
            // Mostrar opciones (compartir, detalles, etc.)
        }
    }

    override fun getItemCount() = animales.size

    fun actualizarAnimales(nuevosAnimales: List<AnimalApadrinado>) {
        animales = nuevosAnimales
        notifyDataSetChanged()
    }
}