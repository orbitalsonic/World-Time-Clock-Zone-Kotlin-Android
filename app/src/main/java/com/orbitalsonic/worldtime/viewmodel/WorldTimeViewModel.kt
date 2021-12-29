package com.orbitalsonic.worldtime.viewmodel

import androidx.lifecycle.*
import com.orbitalsonic.worldtime.datamodel.TimeZoneEntity
import com.orbitalsonic.worldtime.datamodel.WorldTimeEntity
import com.orbitalsonic.worldtime.repository.WorldTimeRepository
import kotlinx.coroutines.launch
import java.lang.IllegalStateException

class WorldTimeViewModel(private val worldTimeRepository: WorldTimeRepository) : ViewModel() {


    /***
     * World Time Methods
     */

    val allWorldTimeList: LiveData<List<WorldTimeEntity>> = worldTimeRepository.allWorldTimeList.asLiveData()

    fun insertWorldTime(worldTimeEntity: WorldTimeEntity) = viewModelScope.launch {
        worldTimeRepository.insertWorldTime(worldTimeEntity)
    }

    fun deleteWorldTime(worldTimeEntity: WorldTimeEntity) = viewModelScope.launch {
        worldTimeRepository.deleteWorldTime(worldTimeEntity)
    }

    fun updateWorldTime(worldTimeEntity: WorldTimeEntity) = viewModelScope.launch {
        worldTimeRepository.updateWorldTime(worldTimeEntity)
    }

    fun deleteAllWorldTimeList() = viewModelScope.launch {
        worldTimeRepository.deleteAllWorldTimeList()
    }


    /***
     * Time Zone Methods
     */

    val allTimeZoneList: LiveData<List<TimeZoneEntity>> = worldTimeRepository.allTimeZoneList.asLiveData()

    fun insertTimeZone(timeZoneEntity: TimeZoneEntity) = viewModelScope.launch {
        worldTimeRepository.insertTimeZone(timeZoneEntity)
    }

    fun deleteTimeZone(timeZoneEntity: TimeZoneEntity) = viewModelScope.launch {
        worldTimeRepository.deleteTimeZone(timeZoneEntity)
    }

    fun updateTimeZone(timeZoneEntity: TimeZoneEntity) = viewModelScope.launch {
        worldTimeRepository.updateTimeZone(timeZoneEntity)
    }

    fun deleteAllTimeZoneList() = viewModelScope.launch {
        worldTimeRepository.deleteAllTimeZoneList()
    }


    /***
     * ViewModelFactory
     */
    class WorldTimeViewModelFactory(private val repository: WorldTimeRepository):ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(WorldTimeViewModel::class.java)){
                @Suppress("UNCHECKED_CAST")
                return WorldTimeViewModel(repository) as T
            }
            throw IllegalStateException("Unknown ViewModel Class")
        }

    }

}