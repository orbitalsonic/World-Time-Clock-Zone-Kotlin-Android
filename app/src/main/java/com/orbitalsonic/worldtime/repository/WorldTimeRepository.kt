package com.orbitalsonic.worldtime.repository

import androidx.annotation.WorkerThread
import com.orbitalsonic.worldtime.datamodel.TimeZoneEntity
import com.orbitalsonic.worldtime.datamodel.WorldTimeEntity
import com.orbitalsonic.worldtime.interfaces.WorldTimeDao
import kotlinx.coroutines.flow.Flow

class WorldTimeRepository(private val worldTimeDao: WorldTimeDao) {

    /***
     World Time Methods
     */
    val allWorldTimeList: Flow<List<WorldTimeEntity>> = worldTimeDao.getAllWorldTimeList()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertWorldTime(worldTimeEntity: WorldTimeEntity) {
        worldTimeDao.insertWorldTime(worldTimeEntity)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteWorldTime(worldTimeEntity: WorldTimeEntity) {
        worldTimeDao.deleteWorldTime(worldTimeEntity)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun updateWorldTime(worldTimeEntity: WorldTimeEntity) {
        worldTimeDao.updateWorldTime(worldTimeEntity)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteAllWorldTimeList() {
        worldTimeDao.deleteAllWorldTimeList()
    }

    /***
     * Time Zone Methods
     */

    val allTimeZoneList: Flow<List<TimeZoneEntity>> = worldTimeDao.getAllTimeZoneList()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertTimeZone(timeZoneEntity: TimeZoneEntity) {
        worldTimeDao.insertTimeZone(timeZoneEntity)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteTimeZone(timeZoneEntity: TimeZoneEntity) {
        worldTimeDao.deleteTimeZone(timeZoneEntity)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun updateTimeZone(timeZoneEntity: TimeZoneEntity) {
        worldTimeDao.updateTimeZone(timeZoneEntity)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteAllTimeZoneList() {
        worldTimeDao.deleteAllTimeZone()
    }

}