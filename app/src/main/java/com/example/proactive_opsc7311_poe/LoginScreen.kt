package com.example.proactive_opsc7311_poe

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class LoginScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_screen)
    }
    fun btnSignUpClicked(view: View) {
        // Define the destination activity class
        val destinationActivity = RegisterScreen::class.java

        // Create an Intent to start the destination activity
        val intent = Intent(this, destinationActivity)

        // Start the destination activity
        startActivity(intent)
    }

    //perform more logic when backend is implemented
    fun btnLoginClicked(view: View) {
        // Define the destination activity class
        val destinationActivity = MainScreen::class.java

        // Create an Intent to start the destination activity
        val intent = Intent(this, destinationActivity)

        // Start the destination activity
        startActivity(intent)
    }
}