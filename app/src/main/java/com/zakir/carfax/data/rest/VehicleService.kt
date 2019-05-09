package com.zakir.carfax.data.rest


import com.zakir.carfax.data.Vehicle.Result
import retrofit2.Call
import retrofit2.http.GET

interface VehicleService {

    @GET("assignment.json/")
    fun getVehicles(): Call<Result>
}