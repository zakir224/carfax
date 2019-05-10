package com.zakir.carfax.vehicles

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zakir.carfax.data.Vehicle
import com.zakir.carfax.data.source.DataSource
import com.zakir.carfax.data.source.Repository


class VehicleListViewModel(val mRepository: Repository) : ViewModel() {


    private var mVehicleListings: MutableLiveData<List<Vehicle.Listing>>? = null
    private var mDealerPhoneNumber: MutableLiveData<String>? = null
    private var mShouldCallDealer: MutableLiveData<Boolean>? = null
    private var mShowDetailsActivity: MutableLiveData<String>? = null

    fun getVehicles(): LiveData<List<Vehicle.Listing>> {
        if (mVehicleListings == null) {
            mVehicleListings = MutableLiveData()
            loadVehicles()
        }
        return mVehicleListings as MutableLiveData<List<Vehicle.Listing>>
    }

    fun getPhone(): LiveData<String> {
        if(mDealerPhoneNumber == null)
            mDealerPhoneNumber = MutableLiveData()
        return mDealerPhoneNumber!!
    }

    fun shouldShowDetailsActivity(): LiveData<String>{
        if(mShowDetailsActivity == null)
            mShowDetailsActivity = MutableLiveData()

        return mShowDetailsActivity!!
    }

    fun shouldCallDealer(): LiveData<Boolean> {
        if(mShouldCallDealer == null)
            mShouldCallDealer = MutableLiveData()
        return mShouldCallDealer!!
    }

    private fun loadVehicles() {
        mRepository.getVehicles(object : DataSource.LoadVehicleCallback {
            override fun onVehicleLoaded(result: Vehicle.Result) {
                mVehicleListings?.postValue(result.listings as List<Vehicle.Listing>?)
            }

            override fun onDataNotAvailable() {

            }

        })
    }

    fun onCallButtonClick(mPhone: String) {
        mDealerPhoneNumber?.postValue(mPhone)
        mShouldCallDealer?.postValue(true)
    }

    fun onVehicleItemClick(listing: Vehicle.Listing) {
        mShowDetailsActivity?.postValue(listing.id)
    }

    fun callingDealer() {
        mShouldCallDealer?.postValue(false)
    }

    fun openingDetails() {
        mShowDetailsActivity?.postValue("")
    }
}


