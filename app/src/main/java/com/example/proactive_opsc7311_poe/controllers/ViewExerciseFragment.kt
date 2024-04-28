package com.example.proactive_opsc7311_poe.controllers

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.proactive_opsc7311_poe.R
import com.example.proactive_opsc7311_poe.models.Exercise
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import com.google.type.DateTime
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ViewExerciseFragment : Fragment()
{
    private lateinit var username: TextView
    private lateinit var exerciseName: TextView
    private lateinit var progressPhotoView: ImageView
    private lateinit var date: TextView
    private lateinit var exerciseDate: TextView
    private lateinit var doneBackground: LinearLayout
    private lateinit var doneImage: ImageView
    private lateinit var done: TextView
    private lateinit var time: TextView
    private lateinit var category: TextView
    private lateinit var categoryDescription: TextView
    private lateinit var stats: TextView
    private lateinit var statsDescription: TextView

    private val db = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View?
    {
        val view = inflater.inflate(R.layout.view_exercise_fragment, container, false)

        username = view.findViewById(R.id.username)
        exerciseName = view.findViewById(R.id.exerciseName)
        progressPhotoView = view.findViewById(R.id.progressPhotoView)
        date = view.findViewById(R.id.date)
        exerciseDate = view.findViewById(R.id.exerciseDate)
        doneBackground = view.findViewById(R.id.doneBackground)
        doneImage = view.findViewById(R.id.doneImage)
        done = view.findViewById(R.id.done)
        time = view.findViewById(R.id.time)
        category = view.findViewById(R.id.category)
        categoryDescription = view.findViewById(R.id.categoryDescription)
        stats = view.findViewById(R.id.stats)
        statsDescription = view.findViewById(R.id.statsDescription)

        progressPhotoView.clipToOutline = true

        // Dummy data for DateTime, replace with actual DateTime objects as needed
        val dummyDate = DateTime.newBuilder().setYear(2024).setMonth(4).setDay(28).build()
        val dummyStartTime = DateTime.newBuilder().setHours(9).setMinutes(0).setSeconds(0).build()
        val dummyEndTime = DateTime.newBuilder().setHours(10).setMinutes(0).setSeconds(0).build()

        // Creating a new Exercise object with dummy data
        val newExercise = Exercise(
            "1500m Warmup", // name
            "Warming up for 1500m.", // description
            "image_url", // image
            dummyDate, // date
            dummyStartTime, // startTime
            dummyEndTime, // endTime
            "Cardio", // category
            5, // min
            10 // max
        )

        // Assuming you have appropriate setters and getters in your Exercise class
        newExercise.setLoggedTime(15); // loggedTime in minutes

        populateComponents(newExercise)

        readData()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun populateComponents(exercise: Exercise)
    {
        val dateFormat = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.getDefault())

        // Convert com.google.type.DateTime to java.util.Date
        val calendar = Calendar.getInstance()
        calendar.set(
            exercise.date.year, exercise.date.month - 1, // Calendar.MONTH is zero-based
            exercise.date.day, exercise.date.hours, exercise.date.minutes, exercise.date.seconds
        )
        val date = calendar.time

        exerciseName.text = exercise.name
        exerciseDate.text = dateFormat.format(date)

        if (exercise.loggedTime < exercise.max && exercise.loggedTime > exercise.min)
        {
            exercise.isGoalsMet = true
            statsDescription.text = "Daily Goals were met. Well Done!"
        } else
        {
            exercise.isGoalsMet = false
            statsDescription.text = "Daily Goals have not been met."
        }

        // Check if the loggedTime is not null and not empty
        if (exercise.loggedTime != null && exercise.loggedTime > 0)
        {
            // Set the background tint to green and the icon to done_true
            doneBackground.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#2AD300"))
            doneImage.setImageResource(R.drawable.done_true)
            time.text = "${exercise.loggedTime} mins"
        } else
        {
            // Set the background tint to proactive red and the icon to done_false
            doneBackground.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor("#FF0000")) // proactive red color code
            doneImage.setImageResource(R.drawable.done_false)
            time.text = "0 mins"
            statsDescription.text = "Daily Goals have not been met yet."
        }

        categoryDescription.text = exercise.category
    }


    private fun readData()
    {
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

                    }
                }
        }
    }

}