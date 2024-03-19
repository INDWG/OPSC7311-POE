package com.example.proactive_opsc7311_poe

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.content.Intent

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)

        // Post a delayed action to move to the home screen after 2 seconds
        Handler().postDelayed({
            // Intent to start the home screen activity
            val intent = Intent(this, LoginScreen::class.java)
            startActivity(intent)
            // Finish the current activity
            finish()
        }, 2000) // 2000 milliseconds = 2 seconds
    }
}