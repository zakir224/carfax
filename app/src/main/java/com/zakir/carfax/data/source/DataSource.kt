package com.zakir.carfax.data.source

import com.zakir.carfax.data.Vehicle


interface DataSource {

    interface LoadVehicleCallback {
        fun onVehicleLoaded(result: Vehicle.Result)
        fun onDataNotAvailable()
    }

    interface LoadDealerCallback {
        fun onDealersLoaded(result: List<Vehicle.Dealer>)
        fun onDealerFound(result: Vehicle.Dealer)
        fun onDealerFound(id: Long)
        fun onDealerNotAvailable()
    }

    interface LoadSearchAreaCallback {
        fun onAreaLoaded(result: Vehicle.SearchArea)
        fun onAreaNotAvailable()
    }

    fun getVehicles(loadVehicleCallback: LoadVehicleCallback)
    fun refreshTasks()
}
