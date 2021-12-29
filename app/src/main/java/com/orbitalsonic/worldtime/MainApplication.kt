package com.orbitalsonic.worldtime

import android.app.Application
import com.orbitalsonic.worldtime.database.WorldTimeDatabase
import com.orbitalsonic.worldtime.repository.WorldTimeRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class MainApplication:Application() {
    private val applicationScope = CoroutineScope(SupervisorJob())
    private val database by lazy { WorldTimeDatabase.getDatabase(this,applicationScope) }
    val repository by lazy { WorldTimeRepository(database.worldTimeDao()) }
}