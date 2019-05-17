package com.zakir.carfax.vehicles.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zakir.carfax.data.Vehicle
import com.zakir.carfax.data.source.DataSource
import com.zakir.carfax.data.source.Repository

class VehicleDetailsViewModel(private val mVehicleRepository: Repository) : ViewModel() {

    private var mVehicleListing: MutableLiveData<Vehicle.Listing>? = null
    private var mVehicleDealer: MutableLiveData<Vehicle.Dealer>? = null
    private var mCallDealer: MutableLiveData<Boolean>? = null

    fun getVehicle(): LiveData<Vehicle.Listing>{
        if(mVehicleListing == null)
            mVehicleListing = MutableLiveData()

        return mVehicleListing!!
    }

    fun shouldCallDealer(): LiveData<Boolean>{
        if(mCallDealer == null)
            mCallDealer = MutableLiveData()

        return mCallDealer!!
    }

    fun onCallButtonClick() {
        mCallDealer?.postValue(true)
    }

    fun setDealerCallDone() {
        mCallDealer?.postValue(false)
    }

    fun getDealer(id: Long): LiveData<Vehicle.Dealer> {
        if(mVehicleDealer == null) {
            mVehicleDealer = MutableLiveData()
            fetchDealer(id)
        }

        return mVehicleDealer!!
    }

    private fun fetchDealer(id: Long) {
        mVehicleRepository.getDealer(id, object: DataSource.LoadDealerCallback{
            override fun onDealersLoaded(result: List<Vehicle.Dealer>) {

            }

            override fun onDealerFound(result: Vehicle.Dealer) {
                mVehicleDealer?.postValue(result)
            }

            override fun onDealerFound(id: Long) {

            }

            override fun onDealerNotAvailable() {

            }

        })
    }

    fun setVehicleId(mVehicleId: String){
        mVehicleRepository.getVehicle(mVehicleId, object : DataSource.LoadVehicleCallback{
            override fun onVehicleLoaded(result: Vehicle.Result) {

            }

            override fun onVehicleLoaded(vehicle: Vehicle.Listing) {
                mVehicleListing?.postValue(vehicle)
            }

            override fun onDataNotAvailable() {

            }

        })

    }
}