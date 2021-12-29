package com.orbitalsonic.worldtime.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.orbitalsonic.worldtime.MainApplication
import com.orbitalsonic.worldtime.R
import com.orbitalsonic.worldtime.adapters.AdapterTimeZone
import com.orbitalsonic.worldtime.databinding.ActivityTimeZoneBinding
import com.orbitalsonic.worldtime.datamodel.TimeZoneEntity
import com.orbitalsonic.worldtime.datamodel.WorldTimeEntity
import com.orbitalsonic.worldtime.interfaces.OnTimeClickListener
import com.orbitalsonic.worldtime.utils.ConversionsUtils.fromObjectToStringZone
import com.orbitalsonic.worldtime.viewmodel.WorldTimeViewModel
import java.util.*
import kotlin.collections.ArrayList

class TimeZoneActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTimeZoneBinding

    private val worldTimeViewModel: WorldTimeViewModel by viewModels {
        WorldTimeViewModel.WorldTimeViewModelFactory((application as MainApplication).repository)
    }

    private lateinit var mAdapter: AdapterTimeZone
    private lateinit var timeZoneEntity: TimeZoneEntity
    private lateinit var worldTimeEntity: WorldTimeEntity
    var mZoneList: List<TimeZoneEntity> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_time_zone)


        initViews()
        createRecyclerView()

        worldTimeViewModel.allTimeZoneList.observe( this) { timeZoneList ->
            timeZoneList.let {
                mAdapter.submitList(it)
                mZoneList = it
                Log.i("information", it.size.toString())

            }
        }

    }

    private fun initViews() {

        binding.etSearchItem.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable) {
                textFilter(s.toString())
            }
        })

        binding.clearText.setOnClickListener {
            binding.etSearchItem.setText("")
        }
    }

    private fun createRecyclerView() {
        mAdapter = AdapterTimeZone()
        binding.recyclerview.adapter = mAdapter
        binding.recyclerview.layoutManager = LinearLayoutManager(this)

        mAdapter.setOnItemClickListener(object : OnTimeClickListener {
            override fun onItemClick(position: Int) {
                timeZoneEntity = mAdapter.currentList[position]
                saveTimeZone()
            }

        })

    }

    private fun textFilter(text: String) {
        val filteredList: ArrayList<TimeZoneEntity> = ArrayList()
        for (item in mZoneList) {
            if (item.searchName.lowercase(Locale.ROOT).contains(text.lowercase(Locale.ROOT))) {
                filteredList.add(item)
            }
        }
        mAdapter.submitList(filteredList)
    }

    private fun saveTimeZone() {

        worldTimeEntity = WorldTimeEntity(
            countryName = timeZoneEntity.countryName,
            cityName = timeZoneEntity.cityName,
            timeZone = timeZoneEntity.timeZone
        )

        val replyIntent = Intent()
        replyIntent.putExtra(
            "TimeZoneData",
            fromObjectToStringZone(worldTimeEntity)
        )
        setResult(Activity.RESULT_OK, replyIntent)
        finish()
    }

}