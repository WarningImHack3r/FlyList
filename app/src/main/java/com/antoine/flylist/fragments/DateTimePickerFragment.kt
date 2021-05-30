package com.antoine.flylist.fragments

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.format.DateFormat
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import java.util.*

@Suppress("DEPRECATION")
class DateTimePickerFragment(private val initialDate: Date, private val keys: Array<String>) :
    DialogFragment(), DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private lateinit var prefs: SharedPreferences

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        prefs = requireActivity().getSharedPreferences("FlyList", Context.MODE_PRIVATE)
        return DatePickerDialog(
            requireActivity(),
            this,
            initialDate.year,
            initialDate.month,
            initialDate.date
        )
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        prefs.edit().putInt(keys[0], year).putInt(keys[1], month).putInt(keys[2], dayOfMonth)
            .apply()
        TimePickerDialog(
            activity,
            this,
            initialDate.hours,
            initialDate.minutes,
            DateFormat.is24HourFormat(activity)
        ).show()
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        prefs.edit().putInt(keys[3], hourOfDay).putInt(keys[4], minute).apply()
        if (parentFragment != null) (parentFragment as ModalFragment).setupDateButtons()
    }
}
