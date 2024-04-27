package com.example.proactive_opsc7311_poe.controllers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proactive_opsc7311_poe.R
import com.example.proactive_opsc7311_poe.models.Workout
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

class ViewWorkoutFragment : Fragment()
{

    private val db = Firebase.firestore

    private lateinit var workoutItem: Button

    private val workouts = mutableListOf<Workout>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View?
    {
        val view = inflater.inflate(R.layout.view_workout_fragment, container, false)

        val recyclerView = view?.findViewById<RecyclerView>(R.id.recyclerViewWorkouts)

        // Assuming you have an adapter class named 'WorkoutAdapter'
        val adapter = WorkoutAdapter(workouts)

        recyclerView?.adapter = adapter
        recyclerView?.layoutManager = LinearLayoutManager(context) // or GridLayoutManager(context, numberOfColumns)

        readData()

        return view
    }

    private fun readData() {
        val user = FirebaseAuth.getInstance().currentUser

        user?.let { currentUser ->
            val userId = currentUser.uid

            db.collection("users").whereEqualTo("uid", userId).get()
                .addOnSuccessListener { querySnapshot ->
                    if (!querySnapshot.isEmpty)
                    {
                        // There should be only one document with the given UID
                        val document = querySnapshot.documents[0]
                        val userData = document.data

                        val username = view?.findViewById<TextView>(R.id.username)

                        val firstname = userData?.get("firstname") as? String ?: ""

                        if (firstname.isNotEmpty())
                        {
                            username?.text = firstname
                        } else
                        {
                            username?.text = "User"
                        }

                        val userDocRef = document.reference

                        // Check if the "workouts" collection exists for the user
                        userDocRef.collection("workouts").get()
                            .addOnSuccessListener { workoutsSnapshot ->
                                for (workoutDocument in workoutsSnapshot.documents)
                                {
                                    val workoutId = workoutDocument.id
                                    val workoutProgress =
                                        workoutDocument.getLong("progress")?.toInt() ?: 0
                                    val totalExercises =
                                        workoutDocument.getLong("total-exercises")?.toInt() ?: 0

                                    // Check if the workout is completed, if so, skip adding it to the list
                                    if (workoutProgress != totalExercises)
                                    {
                                        val workoutName = workoutDocument.getString("name") ?: ""
                                        val workoutDescription =
                                            workoutDocument.getString("description") ?: ""
                                        val newWorkout = Workout(
                                            workoutName,
                                            workoutDescription,
                                            workoutProgress,
                                            totalExercises
                                        )
                                        workouts.add(newWorkout)
                                    }
                                }
                                // Update the RecyclerView with the retrieved workouts
                                updateRecyclerView(workouts)
                            }
                    }
                }
        }
    }

    private fun updateRecyclerView(workouts: List<Workout>) {
        val recyclerView = view?.findViewById<RecyclerView>(R.id.recyclerViewWorkouts)
        val adapter = WorkoutAdapter(workouts)
        recyclerView?.adapter = adapter
        recyclerView?.layoutManager = LinearLayoutManager(context)
    }
}