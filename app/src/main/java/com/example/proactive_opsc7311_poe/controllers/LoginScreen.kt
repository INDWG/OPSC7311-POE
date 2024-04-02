package com.example.proactive_opsc7311_poe.controllers

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.proactive_opsc7311_poe.R
import com.google.firebase.auth.FirebaseAuth

class LoginScreen : AppCompatActivity()
{
    private lateinit var auth: FirebaseAuth
    private lateinit var email: EditText
    private lateinit var password: EditText

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_screen)
        auth = FirebaseAuth.getInstance()

        // Initialize EditText fields
        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
    }

    fun btnSignUpClicked(view: View)
    {
        // Define the destination activity class
        val destinationActivity = RegisterScreen::class.java

        // Create an Intent to start the destination activity
        val intent = Intent(this, destinationActivity)

        // Start the destination activity
        startActivity(intent)
    }

    //perform more logic when backend is implemented
    fun btnLoginClicked(view: View)
    {
        val email = email.text.toString()
        val password = password.text.toString()

        if (email.isNotEmpty() && password.isNotEmpty())
        {
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
                    if (task.isSuccessful)
                    {
                        Toast.makeText(
                            baseContext, "Login successful.", Toast.LENGTH_SHORT
                        ).show()

                        // Sign in success, navigate to MainScreen
                        val intent = Intent(this, MainScreen::class.java)

                        startActivity(intent)
                        finish() // Finish the current activity to prevent back navigation
                    } else
                    {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(
                            baseContext, "Authentication failed.", Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        } else
        {
            // If email or password is empty, show a toast message
            Toast.makeText(
                baseContext, "Please enter email and password.", Toast.LENGTH_SHORT
            ).show()
        }
    }
}