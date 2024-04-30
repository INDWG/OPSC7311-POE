package com.example.proactive_opsc7311_poe.controllers
import java.util.Calendar
import android.app.DatePickerDialog
import android.widget.Toast
import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.proactive_opsc7311_poe.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainScreen : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.main_screen)

        setRelativeSizes()

        //set to this on startup
        navigateToFragment(HomeFragment())

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        // Set listener to handle navigation item clicks
        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId)
            {
                R.id.home ->
                {
                    navigateToFragment(HomeFragment())
                    true
                }

                R.id.add ->
                {
                    navigateToFragment(ViewWorkoutFragment())
                    true
                }

                R.id.progress ->
                {
                    navigateToFragment(ViewExerciseFragment())
                    true
                }

                R.id.view ->
                {
                    navigateToFragment(ActivityViewerFragment())
                    true
                }

                R.id.account ->
                {
                    navigateToFragment(AccountFragment())
                    true
                }

                else -> false
            }
        }
    }

    private fun getScreenHeight(): Int
    {
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.heightPixels
    }

    private fun setRelativeSizes()
    {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        // Calculate the height based on a percentage of the screen height
        val screenHeight = getScreenHeight()
        val heightPercentage = 0.15 // Adjust this percentage as needed
        val height = (screenHeight * heightPercentage).toInt()

        // Set the height of the BottomNavigationView
        val layoutParams = bottomNavigationView.layoutParams
        layoutParams.height = height
        bottomNavigationView.layoutParams = layoutParams

        // Calculate the icon size based on a percentage of the screen height
        val iconSizePercentage = 0.03 // Adjust this percentage as needed
        val iconSize = (screenHeight * iconSizePercentage).toInt()

        // Set the size of the icons in the BottomNavigationView
        bottomNavigationView.itemIconSize = iconSize
    }

    private fun navigateToFragment(fragment: Fragment)
    {
        // Replace the current fragment with the new fragment
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment)
            .commit()
    }

    private fun navigateToActivity(activityClass: Class<*>)
    {
        // Start the new activity
        val intent = Intent(this, activityClass)
        startActivity(intent)
    }

    //Activity Viewer Fragment back button
    fun btnBackClicked(view: View)
    {
        navigateToFragment(ViewWorkoutFragment())
    }

    // Method to show DatePicker in Activity Viewer Fragment
    fun showDateRangePickerDialog(view: View) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        // First, pick the start date
        val startDatePicker = DatePickerDialog(this, { _, startYear, startMonth, startDay ->
            val startDate = "$startDay/${startMonth + 1}/$startYear"

            // Then, pick the end date
            val endDatePicker = DatePickerDialog(this, { _, endYear, endMonth, endDay ->
                val endDate = "$endDay/${endMonth + 1}/$endYear"
                // Handle the date chosen by the user
                Toast.makeText(this, "Date range selected: $startDate - $endDate", Toast.LENGTH_LONG).show()
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
}