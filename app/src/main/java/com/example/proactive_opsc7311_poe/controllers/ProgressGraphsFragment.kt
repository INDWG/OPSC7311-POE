package com.example.proactive_opsc7311_poe.controllers

import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.proactive_opsc7311_poe.R
import com.example.proactive_opsc7311_poe.models.Exercise
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.Calendar
import java.util.Date

class ProgressGraphsFragment : Fragment() {

    private lateinit var dateRange: TextView
    private lateinit var dateRangeSelector: Button
    private lateinit var barChart: BarChart
    private lateinit var totalHoursTextView: TextView

    private val db = Firebase.firestore

    private val exercises = mutableListOf<Exercise>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.progress_graphs_fragment, container, false)

        barChart = view.findViewById(R.id.barChart)
        totalHoursTextView = view.findViewById(R.id.totalHours)

        dateRange = view.findViewById(R.id.dateRange)
        dateRangeSelector = view.findViewById(R.id.btnSelectDateRange)

        dateRangeSelector.setOnClickListener {
            showDateRangePickerDialog(this)
        }

        retrieveUsername()

        return view
    }

    private fun retrieveUsername() {
        val user = FirebaseAuth.getInstance().currentUser
        user?.let { currentUser ->
            val userId = currentUser.uid

            db.collection("users").whereEqualTo("uid", userId).get()
                .addOnSuccessListener { querySnapshot ->
                    if (!querySnapshot.isEmpty) {
                        // There should be only one document with the given UID
                        val document = querySnapshot.documents[0]
                        val userData = document.data

                        val username = view?.findViewById<TextView>(R.id.username)

                        val firstname = userData?.get("firstname") as? String ?: ""

                        if (firstname.isNotEmpty()) {
                            username?.text = firstname
                        } else {
                            username?.text = "User"
                        }
                    }
                }
        }
    }

    fun showDateRangePickerDialog(fragment: Fragment) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        // First, pick the start date
        val startDatePicker =
            DatePickerDialog(requireContext(), { _, startYear, startMonth, startDay ->
                // Create a Calendar instance for the start date
                val startCal = Calendar.getInstance()
                startCal.set(startYear, startMonth, startDay, 0, 0, 0)
                startCal.set(Calendar.MILLISECOND, 0)
                val startDate = startCal.time
                val startDateView = "$startDay/${startMonth + 1}/$startYear"

                // Then, pick the end date
                val endDatePicker =
                    DatePickerDialog(requireContext(), { _, endYear, endMonth, endDay ->
                        // Create a Calendar instance for the end date
                        val endCal = Calendar.getInstance()
                        endCal.set(endYear, endMonth, endDay, 23, 59, 59)
                        endCal.set(Calendar.MILLISECOND, 999)
                        val endDate = endCal.time
                        val endDateView = "$endDay/${endMonth + 1}/$endYear"
                        // Handle the date chosen by the user
                        dateRange.text = "$startDateView - $endDateView"

                        // Set up the BarChart
                        setupBarChart(barChart, startDate, endDate)
                    }, year, month, day)

                // Set the minimum date for the end date picker to be the selected start date
                val startCalendar = Calendar.getInstance()
                startCalendar.set(startYear, startMonth, startDay)
                endDatePicker.datePicker.minDate = startCalendar.timeInMillis

                endDatePicker.setTitle("Select Second Date") // Set custom title for the end date picker dialog
                endDatePicker.show()
            }, year, month, day)

        startDatePicker.setTitle("Select Start Date")
        startDatePicker.show()
    }

    private fun setupBarChart(barChart: BarChart, startDate: Date, endDate: Date) {
        val differenceInMillis = endDate.time - startDate.time
        val daysDifference = (differenceInMillis / (1000 * 60 * 60 * 24)).toInt()

        val minGoalEntries = mutableListOf<BarEntry>()
        val dailyTimeEntries = mutableListOf<BarEntry>()
        val maxGoalEntries = mutableListOf<BarEntry>()
        var totalLoggedTime = 0f

        fun processDayData(dayIndex: Int) {
            if (dayIndex > daysDifference) {
                // All days processed, update chart
                val minGoalDataSet = BarDataSet(minGoalEntries, "Min Goal").apply {
                    color = Color.parseColor("#222BFF")
                    valueTextColor = Color.TRANSPARENT
                }

                val dailyTimeDataSet = BarDataSet(dailyTimeEntries, "Daily Time").apply {
                    color = Color.parseColor("#9E00FF")
                    valueTextColor = Color.TRANSPARENT
                }

                val maxGoalDataSet = BarDataSet(maxGoalEntries, "Max Goal").apply {
                    color = Color.parseColor("#FF2929")
                    valueTextColor = Color.TRANSPARENT
                }

                val data = BarData(minGoalDataSet, dailyTimeDataSet, maxGoalDataSet)
                barChart.data = data

                data.barWidth = 0.2f
                val groupSpace = 0.1f
                val barSpace = (1f - (data.barWidth * 3)) / 4

                barChart.groupBars(0f, groupSpace, barSpace)
                barChart.axisLeft.isEnabled = false
                barChart.axisRight.isEnabled = false
                barChart.xAxis.textColor = Color.WHITE
                barChart.legend.isEnabled = false
                barChart.description.isEnabled = false
                barChart.setScaleEnabled(false)
                barChart.setPinchZoom(false)
                barChart.setDrawGridBackground(false)
                barChart.isDragEnabled = true

                val xAxis: XAxis = barChart.xAxis
                xAxis.position = XAxis.XAxisPosition.BOTTOM
                xAxis.setDrawGridLines(false)
                xAxis.textColor = Color.WHITE
                xAxis.granularity = 1f
                xAxis.axisMinimum = 0f
                xAxis.axisMaximum = daysDifference.toFloat() + 1
                xAxis.setCenterAxisLabels(true)
                xAxis.valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        val calendar = Calendar.getInstance()
                        calendar.time = startDate
                        calendar.add(Calendar.DAY_OF_MONTH, value.toInt())
                        return calendar.get(Calendar.DAY_OF_MONTH).toString()
                    }
                }

                barChart.setVisibleXRangeMaximum(3f)
                barChart.invalidate()

                // Update the total logged time in the TextView
                totalHoursTextView.text = String.format("%.2f", totalLoggedTime)

                return
            }

            val calendar = Calendar.getInstance()
            calendar.time = startDate
            calendar.add(Calendar.DAY_OF_MONTH, dayIndex)
            val date = calendar.time

            totalDailyMinGoal(date) { minGoal ->
                minGoalEntries.add(BarEntry(dayIndex.toFloat(), minGoal))
                totalDailyTime(date) { dailyTime ->
                    dailyTimeEntries.add(BarEntry(dayIndex.toFloat(), dailyTime))
                    totalLoggedTime += dailyTime  // Accumulate the logged time
                    totalDailyMaxGoal(date) { maxGoal ->
                        maxGoalEntries.add(BarEntry(dayIndex.toFloat(), maxGoal))
                        processDayData(dayIndex + 1)  // Process the next day
                    }
                }
            }
        }

        processDayData(0)  // Start processing from the first day
    }

    // Calculate total daily minimum goal time
    private fun totalDailyMinGoal(date: Date, callback: (Float) -> Unit) {
        val startTimestamp = Timestamp(date)
        val endTimestamp = Timestamp(Date(date.time + 24 * 60 * 60 * 1000 - 1))
        readExercisesData(startTimestamp.toDate(), endTimestamp.toDate()) { exercises ->
            val totalTime = exercises.sumOf { it.min ?: 0.00 }.toFloat()

            Log.w("checking", totalTime.toString())

            callback(totalTime / 60f)
        }
    }

    private fun totalDailyTime(date: Date, callback: (Float) -> Unit) {
        val startTimestamp = Timestamp(date)
        val endTimestamp = Timestamp(Date(date.time + 24 * 60 * 60 * 1000 - 1))
        readExercisesData(startTimestamp.toDate(), endTimestamp.toDate()) { exercises ->
            val totalTime = exercises.sumOf { it.loggedTime ?: 0.00 }.toFloat()

            Log.w("checking", totalTime.toString())

            callback(totalTime / 60f)
        }
    }

    private fun totalDailyMaxGoal(date: Date, callback: (Float) -> Unit) {
        val startTimestamp = Timestamp(date)
        val endTimestamp = Timestamp(Date(date.time + 24 * 60 * 60 * 1000 - 1))
        readExercisesData(startTimestamp.toDate(), endTimestamp.toDate()) { exercises ->
            val totalTime = exercises.sumOf { it.max ?: 0.00 }.toFloat()

            Log.w("checking", totalTime.toString())

            callback(totalTime / 60f)
        }
    }

    fun timestampToDate(timestamp: Timestamp): com.google.type.Date {
        val calendar = Calendar.getInstance()
        calendar.time = timestamp.toDate()
        return com.google.type.Date.newBuilder().setYear(calendar.get(Calendar.YEAR))
            .setMonth(calendar.get(Calendar.MONTH) + 1) // Calendar.MONTH is zero-based
            .setDay(calendar.get(Calendar.DAY_OF_MONTH)).build()
    }

    private fun readExercisesData(startDate: Date, endDate: Date, callback: (List<Exercise>) -> Unit) {
        val user = FirebaseAuth.getInstance().currentUser

        user?.let { currentUser ->
            val userId = currentUser.uid

            db.collection("users").whereEqualTo("uid", userId).get()
                .addOnSuccessListener { querySnapshot ->
                    if (!querySnapshot.isEmpty) {
                        val document = querySnapshot.documents[0]
                        val userDocRef = document.reference

                        userDocRef.collection("workouts").get()
                            .addOnSuccessListener { workoutsSnapshot ->
                                val exerciseList = mutableListOf<Exercise>()
                                val tasks = mutableListOf<Task<QuerySnapshot>>()

                                for (workoutDocument in workoutsSnapshot.documents) {
                                    val workoutDocRef = workoutDocument.reference

                                    val task = workoutDocRef.collection("exercises")
                                        .whereGreaterThanOrEqualTo("date", Timestamp(startDate))
                                        .whereLessThan("date", Timestamp(endDate)).get()
                                        .addOnSuccessListener { exercisesSnapshot ->
                                            for (exerciseDocument in exercisesSnapshot.documents) {
                                                val exerciseID = exerciseDocument.getString("exerciseID") ?: ""
                                                val exerciseName = exerciseDocument.getString("name") ?: ""
                                                val exerciseDescription = exerciseDocument.getString("description") ?: ""
                                                val exerciseImage = exerciseDocument.getString("image") ?: ""
                                                val exerciseTimestamp = exerciseDocument.getTimestamp("date")
                                                val exerciseDate = exerciseTimestamp?.let { timestampToDate(it) }
                                                    ?: com.google.type.Date.getDefaultInstance()
                                                val exerciseStartTime = exerciseDocument.getTimestamp("startTime") ?: Timestamp.now()
                                                val exerciseEndTime = exerciseDocument.getTimestamp("endTime") ?: Timestamp.now()
                                                val exerciseCategory = exerciseDocument.getString("category") ?: ""
                                                val exerciseMin = exerciseDocument.getLong("min")?.toDouble() ?: 0.0
                                                val exerciseMax = exerciseDocument.getLong("max")?.toDouble() ?: 0.0
                                                val exerciseLoggedTime =
                                                    exerciseDocument.getLong("loggedTime")?.toDouble()
                                                        ?: 0.00

                                                val newExercise = Exercise(
                                                    exerciseID,
                                                    exerciseName,
                                                    exerciseDescription,
                                                    exerciseImage,
                                                    exerciseDate,
                                                    exerciseStartTime,
                                                    exerciseEndTime,
                                                    exerciseCategory,
                                                    exerciseMin,
                                                    exerciseMax
                                                )

                                                if (exerciseList.none { it.exerciseID == newExercise.exerciseID }) {
                                                    newExercise.loggedTime = exerciseLoggedTime
                                                    exerciseList.add(newExercise)
                                                }
                                            }
                                        }

                                    tasks.add(task)
                                }

                                Tasks.whenAllComplete(tasks).addOnCompleteListener {
                                    callback(exerciseList)
                                }
                            }
                    } else {
                        callback(emptyList())
                    }
                }
        }
    }
}
