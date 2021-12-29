package com.orbitalsonic.worldtime.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.orbitalsonic.worldtime.R
import com.orbitalsonic.worldtime.datamodel.TimeZoneEntity
import com.orbitalsonic.worldtime.datamodel.WorldTimeEntity
import com.orbitalsonic.worldtime.interfaces.WorldTimeDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.joda.time.DateTimeZone
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.nio.charset.Charset
import java.util.*

@Database(
    entities = [WorldTimeEntity::class, TimeZoneEntity::class],
    version = 1,
    exportSchema = false
)

abstract class WorldTimeDatabase : RoomDatabase() {
    abstract fun worldTimeDao(): WorldTimeDao

    companion object {

        @Volatile
        private var INSTANCE: WorldTimeDatabase? = null
        fun getDatabase(context: Context, scope: CoroutineScope): WorldTimeDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WorldTimeDatabase::class.java,
                    "world_time_database"
                ).addCallback(WorldTimeDatabaseCallback(scope, context))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class WorldTimeDatabaseCallback(
        private val scope: CoroutineScope, context: Context
    ) : RoomDatabase.Callback() {

        private val mContext = context

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    val worldTimeDao = database.worldTimeDao()

                    // Delete all content here.
                    worldTimeDao.deleteAllTimeZone()


                    val currentTimeZone = DateTimeZone.getDefault()

                    val inputStream: InputStream = mContext.resources.openRawResource(R.raw.zone)
                    val reader =
                        BufferedReader(InputStreamReader(inputStream, Charset.forName("UTF-8")))
                    reader.readLines().forEach {

                        val items1 = it.split("/")
                        val items2 = it.split("\"")


                        Log.i("information", items2[5])
//                        Log.i("information", items1[items1.size - 1].split("\"")[0])


                        val loc = Locale("", items2[3])
                        loc.displayCountry

//                        Log.i("information", loc.displayCountry)

                        worldTimeDao.insertTimeZone(
                            TimeZoneEntity(
                                countryName = loc.displayCountry,
                                cityName = items1[items1.size - 1].split("\"")[0],
                                searchName = "${loc.displayCountry} ${items2[5]}",
                                timeZone = items2[5]
                            )
                        )

                        if (currentTimeZone == DateTimeZone.forID(items2[5])) {
                            worldTimeDao.insertWorldTime(WorldTimeEntity(countryName = loc.displayCountry, cityName = items1[items1.size - 1].split("\"")[0], timeZone = items2[5]))
                        }

                    }

                    worldTimeDao.insertWorldTime(WorldTimeEntity(countryName = "United Kingdom", cityName = "London", timeZone = "Europe/London"))

                }
            }
        }
    }

}