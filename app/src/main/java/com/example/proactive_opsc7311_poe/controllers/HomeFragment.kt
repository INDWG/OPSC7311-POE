package com.example.proactive_opsc7311_poe.controllers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.proactive_opsc7311_poe.R

class HomeFragment : Fragment()
{

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View?
    {
        return inflater.inflate(R.layout.home_fragment, container, false)
    }
}