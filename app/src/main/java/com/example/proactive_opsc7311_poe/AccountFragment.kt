package com.example.proactive_opsc7311_poe

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import java.util.Calendar

class AccountFragment : Fragment() {

    private lateinit var datePickerDialog: DatePickerDialog
    private lateinit var dateButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.account_fragment, container, false)
        dateButton = view.findViewById(R.id.datePickerButton)
        initDatePicker()
        dateButton.text = todaysDate
        return view
    }



    private val todaysDate: String
        get() {
            val cal = Calendar.getInstance()
            val year = cal.get(Calendar.YEAR)
            var month = cal.get(Calendar.MONTH)
            month += 1
            val day = cal.get(Calendar.DAY_OF_MONTH)
            return makeDateString(day, month, year)
        }

    private fun initDatePicker() {
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, day ->
            val newMonth = month + 1
            val date = makeDateString(day, newMonth, year)
            dateButton.text = date
        }

        val cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)

        val style = AlertDialog.THEME_HOLO_DARK

        datePickerDialog = DatePickerDialog(requireContext(), style, dateSetListener, year, month, day)
    }

    private fun makeDateString(day: Int, month: Int, year: Int): String {
        val formattedDay = if (day < 10) "0$day" else day.toString()
        val formattedMonth = if (month < 10) "0$month" else month.toString()
        return "$formattedDay/$formattedMonth/$year"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dateButton.setOnClickListener {
            openDateClicker()
        }
    }

    private fun openDateClicker() {
        datePickerDialog.show()
    }
}