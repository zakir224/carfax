package com.zakir.carfax.data.source.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.zakir.carfax.data.Vehicle

@Dao
interface VehicleDao {

    @Query("SELECT * FROM vehicle_listing")
    fun getAll(): List<Vehicle.Listing>

    @Query("DELETE  FROM vehicle_listing")
    fun deleteListing()

    @Query("SELECT * FROM vehicle_listing WHERE id LIKE :id LIMIT 1")
    fun findById(id: String): Vehicle.Listing

    @Delete
    fun delete(vehicle: Vehicle.Listing)

    @Insert
    fun addListing(vararg listings: Vehicle.Listing)
}