package com.orbitalsonic.worldtime.datamodel

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "world_time_table")
data class WorldTimeEntity(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @ColumnInfo(name = "countryName") var countryName: String,
    @ColumnInfo(name = "cityName") var cityName: String,
    @ColumnInfo(name = "timeZone") var timeZone:String
)
