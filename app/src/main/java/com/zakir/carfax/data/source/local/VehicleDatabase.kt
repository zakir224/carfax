package com.zakir.carfax.data.source.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.zakir.carfax.data.Vehicle

@Database(
    entities = [Vehicle.Listing::class, Vehicle.Dealer::class,
        Vehicle.SearchArea::class], version = 2
)
abstract class VehicleDatabase: RoomDatabase() {
    abstract fun vehicleListingDao(): VehicleDao
    abstract fun vehicleAreaDao(): VehicleSearchDao
    abstract fun vehicleDealerDao(): VehicleDealerDao


    companion object {
        private var INSTANCE: VehicleDatabase? = null
        private val sLock = Any()
        fun getInstance(context: Context): VehicleDatabase {

            synchronized(sLock) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder<VehicleDatabase>(
                        context.applicationContext,
                        VehicleDatabase::class.java, "Vehicles.db"
                    )
                        .build()
                }
                return INSTANCE as VehicleDatabase
            }
        }
    }

}