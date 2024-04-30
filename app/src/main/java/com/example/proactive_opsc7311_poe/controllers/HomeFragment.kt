package com.example.proactive_opsc7311_poe.controllers

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proactive_opsc7311_poe.R
import com.example.proactive_opsc7311_poe.models.Workout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HomeFragment : Fragment(), OnWorkoutClickListener
{
    private val db = FirebaseFirestore.getInstance()
    private val workouts: MutableList<Workout> = ArrayList()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        val view = inflater.inflate(R.layout.home_fragment, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewWorkouts)
        val adapter = WorkoutAdapter(workouts, this)
        recyclerView.setAdapter(adapter)
        recyclerView.setLayoutManager(LinearLayoutManager(context))
        val dateProgress = view.findViewById<TextView>(R.id.date_progress)
        dateProgress.text = "Today's Progress - " + currentDate
        readData(adapter)
        return view
    }

    private val currentDate: String
        private get()
        {
            val dateFormat = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.getDefault())
            return dateFormat.format(Date())
        }

    private fun readData(adapter: WorkoutAdapter)
    {
        val auth = FirebaseAuth.getInstance()
        if (auth.currentUser != null)
        {
            val userId = auth.currentUser!!.uid
            db.collection("users").document(userId).collection("workouts").get()
                .addOnSuccessListener { queryDocumentSnapshots: QuerySnapshot ->
                    workouts.clear()
                    for (snapshot in queryDocumentSnapshots.getDocuments())
                    {
                        val workout = snapshot.toObject(Workout::class.java)
                        if (workout != null)
                        {
                            workouts.add(workout)
                        }
                    }
                    adapter.notifyDataSetChanged()
                }.addOnFailureListener { e: Exception? ->
                    Log.e(
                        "HomeFragment", "Error reading workouts", e
                    )
                }
        }
    }

    override fun onWorkoutClicked(workoutID: String)
    {
        Log.d("HomeFragment", "Workout clicked: $workoutID")
        // Add navigation or action handling here
    }
}
