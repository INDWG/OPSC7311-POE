package com.example.proactive_opsc7311_poe

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class RegisterScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.register_screen)
    }

    fun btnLoginClicked(view: View) {
        // Define the destination activity class
        val destinationActivity = LoginScreen::class.java

        // Create an Intent to start the destination activity
        val intent = Intent(this, destinationActivity)

        // Start the destination activity
        startActivity(intent)
    }

    //perform more logic when backend is implemented
    fun btnRegisterClicked(view: View) {
        // Define the destination activity class
        val destinationActivity = HomeScreen::class.java

        // Create an Intent to start the destination activity
        val intent = Intent(this, destinationActivity)

        // Start the destination activity
        startActivity(intent)
    }
}