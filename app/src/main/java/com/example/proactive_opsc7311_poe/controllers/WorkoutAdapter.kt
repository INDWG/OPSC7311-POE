package com.example.proactive_opsc7311_poe.controllers

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.proactive_opsc7311_poe.R
import com.example.proactive_opsc7311_poe.models.Workout

class WorkoutAdapter(private val workouts: List<Workout>) : RecyclerView.Adapter<WorkoutAdapter.WorkoutViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_workout, parent, false)
        return WorkoutViewHolder(view)
    }

    override fun onBindViewHolder(holder: WorkoutViewHolder, position: Int) {
        val workout = workouts[position]
        holder.bind(workout)
    }

    override fun getItemCount() = workouts.size

    inner class WorkoutViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.textViewWorkoutTitle)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.textViewWorkoutDescription)
        private val progressBar: ProgressBar = itemView.findViewById(R.id.progressBarWorkout)

        init {
            itemView.setOnClickListener {
                val workoutName = workouts[adapterPosition].name
                workoutItemClicked(itemView, workoutName)
            }
        }

        private fun workoutItemClicked(view: View, workoutName: String) {
            // Display the message dialog
            AlertDialog.Builder(view.context)
                .setTitle("Workout Clicked")
                .setMessage("You clicked on: $workoutName")
                .setPositiveButton("OK") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }

        fun bind(workout: Workout) {
            titleTextView.text = workout.getName()
            descriptionTextView.text = workout.getDescription()
            // Calculate the progress as a percentage
            val progressPercentage = (workout.getProgress().toFloat() / workout.totalExercises) * 100
            progressBar.progress = progressPercentage.toInt()
            progressBar.isEnabled = false
        }
    }
}
