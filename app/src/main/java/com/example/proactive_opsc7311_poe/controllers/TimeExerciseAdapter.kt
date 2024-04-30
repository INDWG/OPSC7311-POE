package com.example.proactive_opsc7311_poe.controllers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.proactive_opsc7311_poe.R
import com.example.proactive_opsc7311_poe.models.Exercise

class TimeExerciseAdapter(private val exercises: List<Exercise>) : RecyclerView.Adapter<TimeExerciseAdapter.WorkoutViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_date_exercise, parent, false)
        return WorkoutViewHolder(view)
    }

    override fun onBindViewHolder(holder: WorkoutViewHolder, position: Int) {
        val exercise = exercises[position]
        holder.bind(exercise)
    }

    override fun getItemCount() = exercises.size

    inner class WorkoutViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.textViewExerciseTitle)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.textViewExerciseDescription)
        private val progressImage: ImageView = itemView.findViewById(R.id.progressImage)

        fun bind(exercise: Exercise) {
            titleTextView.text = exercise.getName()
            descriptionTextView.text = exercise.getDescription()
        }
    }
}