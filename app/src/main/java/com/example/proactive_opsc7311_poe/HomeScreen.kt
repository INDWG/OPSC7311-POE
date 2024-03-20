package com.example.proactive_opsc7311_poe

import android.os.Bundle
import android.util.DisplayMetrics
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.home_screen)

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
        val iconSizePercentage = 0.05 // Adjust this percentage as needed
        val iconSize = (screenHeight * iconSizePercentage).toInt()

        // Set the size of the icons in the BottomNavigationView
        bottomNavigationView.itemIconSize = iconSize
    }

    private fun getScreenHeight(): Int {
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.heightPixels
    }

}