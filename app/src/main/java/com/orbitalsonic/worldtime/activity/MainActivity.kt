package com.orbitalsonic.worldtime.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.orbitalsonic.worldtime.MainApplication
import com.orbitalsonic.worldtime.R
import com.orbitalsonic.worldtime.adapters.AdapterWorldTime
import com.orbitalsonic.worldtime.databinding.ActivityMainBinding
import com.orbitalsonic.worldtime.databinding.DialogDateTimeBinding
import com.orbitalsonic.worldtime.datamodel.WorldTimeEntity
import com.orbitalsonic.worldtime.interfaces.OnDateTimeListener
import com.orbitalsonic.worldtime.interfaces.OnTimeClickListener
import com.orbitalsonic.worldtime.utils.ConversionsUtils.fromStringToObjectZone
import com.orbitalsonic.worldtime.utils.DatePickerFragment
import com.orbitalsonic.worldtime.utils.ScreenUtils
import com.orbitalsonic.worldtime.utils.TimeDateUtils.conversionCheck
import com.orbitalsonic.worldtime.utils.TimeDateUtils.getCurrentTime
import com.orbitalsonic.worldtime.utils.TimeDateUtils.mDay
import com.orbitalsonic.worldtime.utils.TimeDateUtils.mHour
import com.orbitalsonic.worldtime.utils.TimeDateUtils.mMinute
import com.orbitalsonic.worldtime.utils.TimeDateUtils.mMonth
import com.orbitalsonic.worldtime.utils.TimeDateUtils.mYear
import com.orbitalsonic.worldtime.utils.TimePickerFragment
import com.orbitalsonic.worldtime.viewmodel.WorldTimeViewModel
import org.joda.time.DateTime

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val paint = Paint()

    private val worldTimeViewModel: WorldTimeViewModel  by viewModels {
        WorldTimeViewModel.WorldTimeViewModelFactory((application as MainApplication).repository)
    }

    private lateinit var mAdapter: AdapterWorldTime
    private lateinit var worldTimeEntity: WorldTimeEntity
    private var zoneList: List<WorldTimeEntity> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)


        iniViews()
        createRecyclerView()

        worldTimeViewModel.allWorldTimeList.observe( this) { worldTimeList ->
            worldTimeList.let {
                mAdapter.submitList(it)
                zoneList = it
                if (it.isNotEmpty()){
                    binding.clockEmptyText.visibility = View.GONE
                }else{
                    binding.clockEmptyText.visibility = View.VISIBLE
                }

            }
        }
    }

    private fun iniViews() {

        binding.addTimeZone.setOnClickListener {
            openActivityForResult(Intent(this,TimeZoneActivity::class.java))
        }

    }

    private fun createRecyclerView() {
        mAdapter = AdapterWorldTime()
        binding.recyclerview.adapter = mAdapter
        binding.recyclerview.layoutManager = LinearLayoutManager(this)

        mAdapter.setOnItemClickListener(object : OnTimeClickListener {
            override fun onItemClick(position: Int) {
                showDateTimeDialog(mAdapter.currentList[position].timeZone)
            }

        })


        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }


            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                val icon: Bitmap
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    val itemView: View = viewHolder.itemView
                    val height = itemView.bottom.toFloat() - itemView.top.toFloat()
                    val width = height / 3
                    if (dX > 0) {
                        paint.color = Color.parseColor("#fb3c3d")
                        val background = RectF(
                            itemView.left.toFloat(),
                            itemView.top.toFloat(),
                            dX,
                            itemView.bottom.toFloat()
                        )
                        c.drawRect(background, paint)
                        icon = BitmapFactory.decodeResource(resources, R.drawable.ic_delete)
                        val iconDest = RectF(
                            itemView.left.toFloat() + width,
                            itemView.top.toFloat() + width,
                            itemView.left.toFloat() + 2 * width,
                            itemView.bottom.toFloat() - width
                        )
                        c.drawBitmap(icon, null, iconDest, paint)
                    } else {
                        paint.color = Color.parseColor("#fb3c3d")
                        val background = RectF(
                            itemView.right.toFloat() + dX,
                            itemView.top.toFloat(),
                            itemView.right.toFloat(),
                            itemView.bottom.toFloat()
                        )
                        c.drawRect(background, paint)
                        icon = BitmapFactory.decodeResource(resources, R.drawable.ic_delete)
                        val iconDest = RectF(
                            itemView.right.toFloat() - 2 * width,
                            itemView.top.toFloat() + width,
                            itemView.right.toFloat() - width,
                            itemView.bottom.toFloat() - width
                        )
                        c.drawBitmap(icon, null, iconDest, paint)
                    }
                }
                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                if (direction == ItemTouchHelper.LEFT || direction == ItemTouchHelper.RIGHT) {
                    worldTimeEntity = mAdapter.currentList[viewHolder.adapterPosition]
                    worldTimeViewModel.deleteWorldTime(worldTimeEntity)
                    showMessage("Item Deleted!")
                }

            }
        }).attachToRecyclerView(binding.recyclerview)

    }

    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val mIntent: Intent? = result.data
            mIntent?.getStringExtra("TimeZoneData")?.let { reply ->
                val mTimeZone = fromStringToObjectZone(reply)
                zoneComparison(mTimeZone)
            }

        }
    }

    private fun openActivityForResult(mIntent: Intent) {
        resultLauncher.launch(mIntent)
    }


    private fun zoneComparison(mTimeZone: WorldTimeEntity) {
        var dummyCheck = false
        for (index in zoneList.indices) {
            if (zoneList[index].cityName == mTimeZone.cityName) {
                dummyCheck = true
                break
            }
        }

        if (!dummyCheck) {
            worldTimeViewModel.insertWorldTime(mTimeZone)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.zone_menu, menu)
        return true
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.btn_refresh -> {
                conversionCheck = false
                mAdapter.notifyDataSetChanged()

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    @SuppressLint("NotifyDataSetChanged")
    private fun showDateTimeDialog(zoneName: String) {
        val mDialog = Dialog(this)
        val dialogBinding: DialogDateTimeBinding = DataBindingUtil.inflate(
            LayoutInflater.from(this), R.layout.dialog_date_time, null, false
        )
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        mDialog.setCancelable(false)
        mDialog.setContentView(dialogBinding.root)

        dialogBinding.dialogLayout.requestLayout()
        dialogBinding.dialogLayout.layoutParams.width =  (ScreenUtils.getScreenWidth(this) * .90).toInt()

        val dtCity = DateTime()

        mYear = dtCity.year
        mMonth = dtCity.monthOfYear
        mDay = dtCity.dayOfMonth
        mHour = dtCity.hourOfDay
        mMinute = dtCity.minuteOfHour

        dialogBinding.tvZoneName.text = zoneName

        dialogBinding.tvDialogTime.text = getCurrentTime(
            mYear,
            mMonth,
            mDay,
            mHour,
            mMinute,
            zoneName,
            "hh:mm a"
        )


        dialogBinding.tvDialogDate.text = getCurrentTime(
            mYear,
            mMonth,
            mDay,
            mHour,
            mMinute,
            zoneName,
            "EEE, MMM d"
        )

        dialogBinding.btnDialogTime.setOnClickListener {
            val newFragment = TimePickerFragment(mDialog.context)
            newFragment.show(supportFragmentManager, "timePicker")
            newFragment.onClickListener(object : OnDateTimeListener {
                override fun onItemClick() {
                    dialogBinding.tvDialogTime.text = getCurrentTime(
                        mYear,
                        mMonth,
                        mDay,
                        mHour,
                        mMinute,
                        zoneName,
                        "hh:mm a"
                    )
                }

            })

        }

        dialogBinding.btnDialogDate.setOnClickListener {
            val newFragment = DatePickerFragment(mDialog.context)
            newFragment.show(supportFragmentManager, "datePicker")
            newFragment.onClickListener(object : OnDateTimeListener {
                override fun onItemClick() {
                    dialogBinding.tvDialogDate.text = getCurrentTime(
                        mYear,
                        mMonth,
                        mDay,
                        mHour,
                        mMinute,
                        zoneName,
                        "EEE, MMM d"
                    )
                }

            })
        }

        dialogBinding.btnCancel.setOnClickListener {
            mDialog.dismiss()
        }

        dialogBinding.btnDone.setOnClickListener {
            mDialog.dismiss()
            conversionCheck = true
            mAdapter.notifyDataSetChanged()

        }

        mDialog.show()
    }

    private fun showMessage(message:String){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
    }
}