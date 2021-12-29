package com.orbitalsonic.worldtime.utils

import android.annotation.SuppressLint
import android.util.Log
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.Period
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


object TimeDateUtils {


    var mYear:Int = 0
    var mMonth:Int = 0
    var mDay:Int = 0
    var mHour:Int = 0
    var mMinute:Int = 0
    var conversionCheck = false

    fun toDisplayString(timeHundreds: Int): String {
        var mDummy=timeHundreds

        val formatterArrayMillis = arrayOfNulls<String>(2)
        val formattedSeconds: String
        val formattedMinutes: String

        val hundreds: Int = timeHundreds % 100
        val milliSecStr = hundreds.toString()
        formatterArrayMillis[0] = "0$milliSecStr"
        formatterArrayMillis[1] = milliSecStr

        val seconds: Int = 100.let { mDummy /= it; mDummy } % 60
        val minutes: Int = 60.let { mDummy /= it; mDummy } % 60

        //format output
        formattedSeconds = (seconds / 10).toString() +
                (seconds % 10).toString()
        formattedMinutes = (minutes / 10).toString() +
                (minutes % 10).toString()

        val millSecDigitsCnt = milliSecStr.length

        return formattedMinutes + ":" +
                formattedSeconds + "." +
                formatterArrayMillis[millSecDigitsCnt - 1]

    }


    /**
    zone = Europe/London
    formatter = dd:MM:yy
     ***/

    fun getCurrentTime(
        mYear: Int,
        mMonth: Int,
        mDay: Int,
        mHour: Int,
        mMinute: Int,
        zone: String,
        formatter: String
    ):String{
        val dt = DateTime(mYear, mMonth, mDay, mHour, mMinute)
        val dtCity = dt.withZone(DateTimeZone.forID(zone))

        return dtCity.toString(formatter)
    }


    @SuppressLint("SimpleDateFormat")
    fun getCurrentDate(formatter: String): String {
        val dateFormat = SimpleDateFormat(formatter)
        val today: Date = Calendar.getInstance().time
        return dateFormat.format(today)
    }

    fun getDate(mYear: Int, mMonth: Int, mDay: Int, formater: String):String{

        val mDate = "$mMonth/$mDay/$mYear"

        val fmt: DateTimeFormatter = DateTimeFormat.forPattern("MM/dd/yyyy")
        val jodaDate: DateTime = fmt.parseDateTime(mDate)

        val finalDate = DateTimeFormat.forPattern(formater)

        return finalDate.print(jodaDate)

    }

    fun getTime(mHour: Int, mMinute: Int, formater: String):String{

        val mDate = "$mHour:$mMinute"

        val fmt: DateTimeFormatter = DateTimeFormat.forPattern("HH:mm")
        val jodaDate: DateTime = fmt.parseDateTime(mDate)

        val finalDate = DateTimeFormat.forPattern(formater)

        return finalDate.print(jodaDate)

    }

    fun getDateDifference(fromDate: String, toDate: String, formater: String):String{

        val fmt: DateTimeFormatter = DateTimeFormat.forPattern(formater)
        val mDate1: DateTime = fmt.parseDateTime(fromDate)
        val mDate2: DateTime = fmt.parseDateTime(toDate)
        val period = Period(mDate1, mDate2)
        val mDays:Int = period.days + (period.weeks*7) + 1



        return "Year: ${period.years}\nMonth: ${period.months}\nDay: $mDays"
    }


     @SuppressLint("SimpleDateFormat")
     fun dateChanger(mdDate: String, mTime: String): String {
        var formatter = SimpleDateFormat("dd-MMM-yyyy HH:mm")
        var date: Date? = null
        try {
            date = formatter.parse("$mdDate $mTime")
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        formatter = SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'")
        return formatter.format(date!!)
    }
}
