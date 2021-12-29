package com.orbitalsonic.worldtime.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.orbitalsonic.worldtime.R
import com.orbitalsonic.worldtime.databinding.ItemWorldTimeBinding
import com.orbitalsonic.worldtime.datamodel.WorldTimeEntity
import com.orbitalsonic.worldtime.interfaces.OnTimeClickListener
import com.orbitalsonic.worldtime.utils.TimeDateUtils.conversionCheck
import com.orbitalsonic.worldtime.utils.TimeDateUtils.getCurrentTime
import com.orbitalsonic.worldtime.utils.TimeDateUtils.mDay
import com.orbitalsonic.worldtime.utils.TimeDateUtils.mHour
import com.orbitalsonic.worldtime.utils.TimeDateUtils.mMinute
import com.orbitalsonic.worldtime.utils.TimeDateUtils.mMonth
import com.orbitalsonic.worldtime.utils.TimeDateUtils.mYear

class AdapterWorldTime : ListAdapter<WorldTimeEntity, AdapterWorldTime.WorldTimeViewHolder>(
    DATA_COMPARATOR
) {

    var mListener: OnTimeClickListener? = null

    fun setOnItemClickListener(listener: OnTimeClickListener) {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorldTimeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ItemWorldTimeBinding=
            DataBindingUtil.inflate(inflater, R.layout.item_world_time, parent, false)
        return WorldTimeViewHolder(binding, mListener!!)

    }

    override fun onBindViewHolder(holderWorld: WorldTimeViewHolder, position: Int) {
        val currentItem = getItem(position)
        holderWorld.bind(currentItem)
    }


    class WorldTimeViewHolder(binding: ItemWorldTimeBinding, listener: OnTimeClickListener) :
        RecyclerView.ViewHolder(binding.root) {
        private val mBinding = binding

        init {

            mBinding.cardView.setOnClickListener {
                val mPosition = adapterPosition
                listener.onItemClick(mPosition)
            }

        }

        fun bind(mCurrentItem: WorldTimeEntity) {
            mBinding.wordTimeData = mCurrentItem

            if (conversionCheck) {
                mBinding.tvDate.visibility = View.VISIBLE
                mBinding.tvTime.visibility = View.VISIBLE
                mBinding.tcDate.visibility = View.GONE
                mBinding.tcTime.visibility = View.GONE
                mBinding.tvTime.text = getCurrentTime(
                    mYear,
                    mMonth,
                    mDay,
                    mHour,
                    mMinute,
                    mCurrentItem.timeZone,
                    "hh:mm a"
                )

                mBinding.tvDate.text = getCurrentTime(
                    mYear,
                    mMonth,
                    mDay,
                    mHour,
                    mMinute,
                    mCurrentItem.timeZone,
                    "EEE, MMM d"
                )

            } else {
                mBinding.tcDate.timeZone = mCurrentItem.timeZone
                mBinding.tcTime.timeZone = mCurrentItem.timeZone
                mBinding.tcDate.visibility = View.VISIBLE
                mBinding.tcTime.visibility = View.VISIBLE
                mBinding.tvTime.visibility = View.GONE
                mBinding.tvDate.visibility = View.GONE
                mBinding.tvTime.visibility = View.GONE
            }


        }

    }


    companion object {
        private val DATA_COMPARATOR = object : DiffUtil.ItemCallback<WorldTimeEntity>() {
            override fun areItemsTheSame(
                oldItem: WorldTimeEntity,
                newItem: WorldTimeEntity
            ): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(
                oldItem: WorldTimeEntity,
                newItem: WorldTimeEntity
            ): Boolean {
                return oldItem.cityName == newItem.cityName
            }
        }
    }

}