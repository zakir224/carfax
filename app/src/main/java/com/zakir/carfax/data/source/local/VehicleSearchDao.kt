package com.zakir.carfax.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.zakir.carfax.data.Vehicle

@Dao
interface VehicleSearchDao {

    @Query("SELECT * FROM vehicle_search_area LIMIT 1")
    fun getSearchArea(): Vehicle.SearchArea

    @Query("DELETE FROM vehicle_search_area")
    fun deleteSearchArea()

    @Insert
    fun addSearchArea(vararg listings: Vehicle.SearchArea)
}
