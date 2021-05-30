package com.antoine.flylist.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.antoine.flylist.R
import com.antoine.flylist.activities.MainActivity
import com.antoine.flylist.data.api.APIManager
import com.antoine.flylist.utils.Utils
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class ModalFragment : BottomSheetDialogFragment(), AdapterView.OnItemSelectedListener {

    private lateinit var prefs: SharedPreferences
    private lateinit var beginningDate: Date
    private lateinit var endingDate: Date

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_modal, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prefs = activity?.getSharedPreferences("FlyList", Context.MODE_PRIVATE)!!

        // Setup call picker
        val callPicker = view.findViewById<Spinner>(R.id.spinner)
        callPicker.onItemSelectedListener = this
        context?.let {
            ArrayAdapter.createFromResource(
                it,
                R.array.api_call_array,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                callPicker.adapter = adapter
            }
        }
        prefs.getInt("callPicker", -1).let {
            if (it != -1) callPicker.setSelection(it)
        }

        // Setup dates
        setupDateButtons()

        // Setup optional fields
        val icaoManda = view.findViewById<EditText>(R.id.mandatory_icao24)
        prefs.getString("icao24manda", "").apply {
            if (this != "") icaoManda.text = Editable.Factory.getInstance().newEditable(this)
        }
        val airportManda = view.findViewById<EditText>(R.id.mandatory_airport)
        prefs.getString("airportmanda", "").apply {
            if (this != "") airportManda.text = Editable.Factory.getInstance().newEditable(this)
        }
        when (prefs.getInt("callPicker", -1)) {
            1 -> icaoManda.visibility = View.VISIBLE
            2, 3 -> airportManda.visibility = View.VISIBLE
        }


        // Search button
        view.findViewById<Button>(R.id.searchButton).setOnClickListener {
            prefs.edit().putString("icao24manda", icaoManda.text.toString())
                .putString("airportmanda", airportManda.text.toString()).apply()
            (activity as MainActivity).viewModel.call = when (prefs.getInt("callPicker", -1)) {
                0 -> APIManager.FLIGHT_API.allFlights(
                    Utils.dateToEpoch(beginningDate), Utils.dateToEpoch(
                        endingDate
                    )
                )
                1 -> APIManager.FLIGHT_API.flightsByAircraft(
                    prefs.getString("icao24manda", "")!!,
                    Utils.dateToEpoch(beginningDate),
                    Utils.dateToEpoch(endingDate)
                )
                2 -> APIManager.FLIGHT_API.departuresByAirport(
                    prefs.getString(
                        "airportmanda",
                        ""
                    )!!, Utils.dateToEpoch(beginningDate), Utils.dateToEpoch(endingDate)
                )
                3 -> APIManager.FLIGHT_API.arrivalsByAirport(
                    prefs.getString("airportmanda", "")!!,
                    Utils.dateToEpoch(beginningDate),
                    Utils.dateToEpoch(endingDate)
                )
                else -> (activity as MainActivity).viewModel.call
            }
            dismiss()
        }
    }

    @Suppress("DEPRECATION")
    @SuppressLint("SetTextI18n")
    fun setupDateButtons() {
        val dateFormatter =
            SimpleDateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.SHORT)
        val beginningButton = requireView().findViewById<Button>(R.id.beginning_date_picker)
        beginningDate = Date().apply {
            this.year = prefs.getInt("year_b", this.year) % 1900
            this.month = prefs.getInt("month_b", this.month)
            this.date = prefs.getInt("day_b", this.date)
            this.hours = prefs.getInt("hours_b", this.hours)
            this.minutes = prefs.getInt("minutes_b", this.minutes)
        }
        beginningButton.text =
            getString(R.string.beginning_date) + " (" + dateFormatter.format(beginningDate) + ")"
        beginningButton.setOnClickListener {
            childFragmentManager.beginTransaction().add(DateTimePickerFragment(beginningDate.apply {
                this.year += 1900
            }, arrayOf("year_b", "month_b", "day_b", "hours_b", "minutes_b")), null)
                .addToBackStack(null).commit()
        }
        val endingButton = requireView().findViewById<Button>(R.id.ending_date_picker)
        endingDate = Date().apply {
            this.year = prefs.getInt("year_e", this.year) % 1900
            this.month = prefs.getInt("month_e", this.month)
            this.date = prefs.getInt("day_e", this.date)
            this.hours = prefs.getInt("hours_e", this.hours)
            this.minutes = prefs.getInt("minutes_e", this.minutes)
        }
        endingButton.text =
            getString(R.string.ending_date) + " (" + dateFormatter.format(endingDate) + ")"
        endingButton.setOnClickListener {
            childFragmentManager.beginTransaction().add(DateTimePickerFragment(endingDate.apply {
                this.year += 1900
            }, arrayOf("year_e", "month_e", "day_e", "hours_e", "minutes_e")), null)
                .addToBackStack(null).commit()
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if (parent != null) {
            prefs.edit().putInt("callPicker", position).apply()
            when (position) {
                0 -> {
                    this.view?.findViewById<EditText>(R.id.mandatory_icao24)?.visibility = View.GONE
                    this.view?.findViewById<EditText>(R.id.mandatory_airport)?.visibility =
                        View.GONE
                }
                1 -> {
                    this.view?.findViewById<EditText>(R.id.mandatory_icao24)?.visibility =
                        View.VISIBLE
                    this.view?.findViewById<EditText>(R.id.mandatory_airport)?.visibility =
                        View.GONE
                }
                2, 3 -> {
                    this.view?.findViewById<EditText>(R.id.mandatory_airport)?.visibility =
                        View.VISIBLE
                    this.view?.findViewById<EditText>(R.id.mandatory_icao24)?.visibility = View.GONE
                }
            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {}
}
