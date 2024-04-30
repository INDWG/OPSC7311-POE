package com.example.proactive_opsc7311_poe.controllers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.proactive_opsc7311_poe.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ActivityViewerFragment : Fragment() {

    // Reference to Firebase Firestore
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_viewer_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize the Spinner
        val spinnerWorkoutsExercises = view.findViewById<Spinner>(R.id.spinnerWorkoutsExercises)

        // Populate the Spinner with options
        val workoutOrExerciseOptions = arrayOf("Workout", "Exercise")
        val adapter = ArrayAdapter(requireContext(), R.layout.spinner_item_layout, workoutOrExerciseOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerWorkoutsExercises.adapter = adapter

        // Call the method to retrieve user data
        retrieveUserName(view)
    }

    private fun retrieveUserName(view: View) {
        val user = FirebaseAuth.getInstance().currentUser
        user?.let { currentUser ->
            val userId = currentUser.uid
            db.collection("users").whereEqualTo("uid", userId).get()
                .addOnSuccessListener { querySnapshot ->
                    if (!querySnapshot.isEmpty) {
                        val document = querySnapshot.documents[0]
                        val userData = document.data
                        val firstname = userData?.get("firstname") as? String ?: "User"
                        val usernameTextView = view.findViewById<TextView>(R.id.username)
                        if (firstname.isNotEmpty()) {
                            usernameTextView.text = firstname
                        } else {
                            usernameTextView.text = "User"
                        }
                    } else {
                        // Log or show no user data found
                        Toast.makeText(context, "No user data found.", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { exception ->
                    // Handle any errors here, possibly with a Toast
                    Toast.makeText(context, "Failed to retrieve user data: ${exception.message}", Toast.LENGTH_LONG).show()
                }
        }
    }

}
