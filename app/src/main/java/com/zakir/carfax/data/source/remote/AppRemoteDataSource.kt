package com.zakir.carfax.data.source.remote


import com.zakir.carfax.data.Vehicle
import com.zakir.carfax.data.rest.VehicleService
import com.zakir.carfax.data.source.DataSource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class AppRemoteDataSource private constructor() : RemoteDataSource {


    override fun getVehicles(loadVehicleCallback: DataSource.LoadVehicleCallback) {
        val retrofit = Retrofit.Builder()
            .baseUrl(API_END_POINT)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create<VehicleService>(VehicleService::class.java)
        val call = service.getVehicles()

        call.enqueue(object : Callback<Vehicle.Result> {
            override fun onFailure(call: Call<Vehicle.Result>, t: Throwable) {
                loadVehicleCallback.onDataNotAvailable()
            }

            override fun onResponse(
                call: Call<Vehicle.Result>,
                response: Response<Vehicle.Result>
            ) {
                response.body()?.let {
                    loadVehicleCallback.onVehicleLoaded(it)
                }
            }

        })
    }

    override fun refreshTasks() {
        // handled by the repo itself
    }

    companion object {

        private var INSTANCE: AppRemoteDataSource? = null
        private const val API_END_POINT = "https://carfax-for-consumers.firebaseio.com/"

        fun instance(): RemoteDataSource {
            if (INSTANCE == null) {
                INSTANCE = AppRemoteDataSource()
            }
            return INSTANCE as RemoteDataSource
        }
    }
}
