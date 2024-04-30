package com.example.proactive_opsc7311_poe.controllers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.proactive_opsc7311_poe.R
import com.example.proactive_opsc7311_poe.models.Exercise

class ExerciseAdapter(
    private val exercises: List<Exercise>,
    private val exerciseClickListener: OnExerciseClickListener,
    private val logTimeClickListener: OnLogTimeClickListener
) : RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_exercise, parent, false)
        return ExerciseViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        val exercise = exercises[position]
        holder.bind(exercise)
    }

    override fun getItemCount() = exercises.size

    inner class ExerciseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.textViewExerciseTitle)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.textViewExerciseDescription)
        private val imageView: ImageView = itemView.findViewById(R.id.progressImage)
        private val logTimeButton: Button = itemView.findViewById(R.id.btnLogTime)

        init {
            itemView.setOnClickListener {
                val exerciseID = exercises[adapterPosition].exerciseID
                exerciseClickListener.onExerciseClicked(exerciseID)
            }
            logTimeButton.setOnClickListener {
                val exerciseID = exercises[adapterPosition].exerciseID
                logTimeClickListener.onLogTimeClicked(exerciseID)
            }
        }

        fun bind(exercise: Exercise) {
            nameTextView.text = exercise.name
            descriptionTextView.text = exercise.description
            // Bind other views as needed
        }
    }
}

interface OnExerciseClickListener {
    fun onExerciseClicked(exerciseID: String)
}

interface OnLogTimeClickListener {
    fun onLogTimeClicked(exerciseID: String)
}