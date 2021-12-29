package com.orbitalsonic.worldtime.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.orbitalsonic.worldtime.R
import com.orbitalsonic.worldtime.databinding.ItemTimeZoneBinding
import com.orbitalsonic.worldtime.datamodel.TimeZoneEntity
import com.orbitalsonic.worldtime.interfaces.OnTimeClickListener


class AdapterTimeZone : ListAdapter<TimeZoneEntity, AdapterTimeZone.TimeZoneViewHolder>(DATA_COMPARATOR) {

    var mListener: OnTimeClickListener? = null

    fun setOnItemClickListener(listener: OnTimeClickListener) {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeZoneViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ItemTimeZoneBinding =
            DataBindingUtil.inflate(inflater, R.layout.item_time_zone, parent, false)
        return TimeZoneViewHolder(binding, mListener!!)

    }

    override fun onBindViewHolder(holder: TimeZoneViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }


    class TimeZoneViewHolder(
        binding: ItemTimeZoneBinding,
        listener: OnTimeClickListener
    ) :
        RecyclerView.ViewHolder(binding.root) {
        private val mBinding = binding


        init {

            mBinding.cardView.setOnClickListener {
                val mPosition = adapterPosition
                listener.onItemClick(mPosition)
            }

        }

        fun bind(mCurrentItem: TimeZoneEntity) {
            mBinding.timeZoneData = mCurrentItem
            mBinding.tcDate.timeZone = mCurrentItem.timeZone
            mBinding.tcTime.timeZone = mCurrentItem.timeZone

        }

    }


    companion object {
        private val DATA_COMPARATOR = object : DiffUtil.ItemCallback<TimeZoneEntity>() {
            override fun areItemsTheSame(
                oldItem: TimeZoneEntity,
                newItem: TimeZoneEntity
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: TimeZoneEntity,
                newItem: TimeZoneEntity
            ): Boolean {
                return oldItem.cityName == newItem.cityName
            }
        }
    }

}