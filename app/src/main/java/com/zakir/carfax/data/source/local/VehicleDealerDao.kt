package com.zakir.carfax.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.zakir.carfax.data.Vehicle

@Dao
interface VehicleDealerDao {

    @Query("SELECT * FROM vehicle_dealer")
    fun getDealers(): List<Vehicle.Dealer>

    @Query("SELECT * FROM vehicle_dealer WHERE id LIKE :id LIMIT 1")
    fun getDealer(id: Long): Vehicle.Dealer

    @Query("DELETE  FROM vehicle_dealer")
    fun deleteDealer()

    @Insert
    fun addDealer(listings: Vehicle.Dealer): Long

    @Query("SELECT id FROM vehicle_dealer WHERE car_fax_id LIKE :carFaxId LIMIT 1")
    fun getDealer(carFaxId: String): Long
}
