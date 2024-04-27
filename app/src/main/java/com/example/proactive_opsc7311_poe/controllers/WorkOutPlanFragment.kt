package com.example.proactive_opsc7311_poe.controllers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.proactive_opsc7311_poe.R
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

class WorkOutPlanFragment : Fragment()
{

    private val db = Firebase.firestore

    private lateinit var helpButton: Button

    private lateinit var backButton: ImageButton


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View?
    {
        val view = inflater.inflate(R.layout.workout_plan_fragment, container, false)

        readData()

        return view
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        helpButton =
            view.findViewById(R.id.btnHelp)
        helpButton.setOnClickListener {
            btnHelpClicked(this)
        }

        backButton =
            view.findViewById(R.id.btnBack)
        backButton.setOnClickListener {
            btnBackClicked(this)
        }
    }

    fun btnHelpClicked(fragment: Fragment)
    {
        //navigateToFragment(Help())
    }

    fun btnBackClicked(fragment: Fragment)
    {
        navigateToFragment(ViewWorkoutFragment())
    }
    private fun navigateToFragment(fragment: Fragment) {
        // Replace the current fragment with the new fragment
        parentFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment)
            .commit()
    }


}
