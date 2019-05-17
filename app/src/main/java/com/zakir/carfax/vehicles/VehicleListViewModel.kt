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
    private var mLoading: MutableLiveData<Boolean>? = null
    private var mSelectedVehicleId: MutableLiveData<String>? = null

    fun getVehicles(): LiveData<List<Vehicle.Listing>> {
        if (mVehicleListings == null) {
            mVehicleListings = MutableLiveData()
            loadVehicles(false)
        }
        return mVehicleListings as LiveData<List<Vehicle.Listing>>
    }

    fun getLoading(): LiveData<Boolean> {
        if (mLoading == null) {
            mLoading = MutableLiveData()
        }
        return mLoading as LiveData<Boolean>
    }

    fun getPhone(): LiveData<String> {
        if (mDealerPhoneNumber == null)
            mDealerPhoneNumber = MutableLiveData()
        return mDealerPhoneNumber!!
    }

    fun shouldShowDetailsActivity(): LiveData<String> {
        if (mSelectedVehicleId == null)
            mSelectedVehicleId = MutableLiveData()

        return mSelectedVehicleId!!
    }

    fun shouldCallDealer(): LiveData<Boolean> {
        if (mShouldCallDealer == null)
            mShouldCallDealer = MutableLiveData()
        return mShouldCallDealer!!
    }

    private fun loadVehicles(forceUpdate: Boolean) {
        if(forceUpdate) {
            mRepository.refreshTasks()
        }
        mLoading?.postValue(true)
        mRepository.getVehicles(object : DataSource.LoadVehicleCallback {
            override fun onVehicleLoaded(vehicle: Vehicle.Listing) {

            }

            override fun onVehicleLoaded(result: Vehicle.Result) {
                mLoading?.postValue(false)
                mVehicleListings?.postValue(result.listings as List<Vehicle.Listing>?)
            }

            override fun onDataNotAvailable() {
                mLoading?.postValue(false)
            }

        })
    }

    fun onCallButtonClick(mPhone: String) {
        mDealerPhoneNumber?.postValue(mPhone)
        mShouldCallDealer?.postValue(true)
    }

    fun onVehicleItemClick(listing: Vehicle.Listing) {
        mSelectedVehicleId?.postValue(listing.id)
    }

    fun callingDealer() {
        mShouldCallDealer?.postValue(false)
    }

    fun clearSelectedVehicleId() {
        mSelectedVehicleId?.postValue("")
    }

    fun onForceRefresh() {
        loadVehicles(true)
    }
}


