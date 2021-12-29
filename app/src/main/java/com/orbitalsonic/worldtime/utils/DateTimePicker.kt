package com.orbitalsonic.worldtime.utils

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.text.format.DateFormat
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import com.orbitalsonic.worldtime.interfaces.OnDateTimeListener
import com.orbitalsonic.worldtime.utils.TimeDateUtils.mDay
import com.orbitalsonic.worldtime.utils.TimeDateUtils.mHour
import com.orbitalsonic.worldtime.utils.TimeDateUtils.mMinute
import com.orbitalsonic.worldtime.utils.TimeDateUtils.mMonth
import com.orbitalsonic.worldtime.utils.TimeDateUtils.mYear
import java.util.*

class TimePickerFragment(context: Context) : DialogFragment(), TimePickerDialog.OnTimeSetListener {

    private val mContext = context
    private var mListener: OnDateTimeListener? = null

    fun onClickListener(onDateTimeListener: OnDateTimeListener) {
        mListener = onDateTimeListener
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the current time as the default values for the picker
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)

        // Create a new instance of TimePickerDialog and return it
        return TimePickerDialog(mContext,this, hour, minute, DateFormat.is24HourFormat(activity))
    }

    override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {

        mHour = hourOfDay
        mMinute = minute
        mListener?.onItemClick()
        // Do something with the time chosen by the user
    }

}

class DatePickerFragment(context: Context) : DialogFragment(), DatePickerDialog.OnDateSetListener {

    private val mContext = context
    private var mListener: OnDateTimeListener? = null

    fun onClickListener(onDateTimeListener: OnDateTimeListener) {
        mListener = onDateTimeListener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the current date as the default date in the picker
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        // Create a new instance of DatePickerDialog and return it
        return DatePickerDialog(mContext,this, year, month, day)
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        mYear = year
        mMonth = month+1
        mDay = day
        mListener?.onItemClick()
        // Do something with the date chosen by the user
    }
}