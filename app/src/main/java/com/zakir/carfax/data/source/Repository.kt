package com.zakir.carfax.data.source

import com.zakir.carfax.data.Vehicle
import com.zakir.carfax.data.source.local.LocalDataSource
import com.zakir.carfax.data.source.remote.RemoteDataSource

class Repository private constructor(
    private var mRemoteDataSource: RemoteDataSource,
    private var mLocalDataSource: LocalDataSource
) : LocalDataSource, RemoteDataSource {
    override fun getVehicle(id: String, loadDealerCallback: DataSource.LoadVehicleCallback) {
        mLocalDataSource.getVehicle(id, loadDealerCallback)
    }

    override fun saveResult(result: Vehicle.Result) {
        mLocalDataSource.saveResult(result)
    }

    private var mCachedResponse: Vehicle.Result? = null

    private var mCacheIsDirty = false

    override fun getSearchArea(loadSearchAreaCallback: DataSource.LoadSearchAreaCallback) {
        return mLocalDataSource.getSearchArea(loadSearchAreaCallback)
    }

    override fun getDealer(id: Long, loadDealerCallback: DataSource.LoadDealerCallback) {
        return mLocalDataSource.getDealer(id, loadDealerCallback)
    }

    override fun saveListing(vehicle: Vehicle.Listing) {
        mLocalDataSource.saveListing(vehicle)
    }

    override fun saveSearchArea(searchArea: Vehicle.SearchArea) {
        mLocalDataSource.saveSearchArea(searchArea)
    }

    override fun saveDealer(dealer: Vehicle.Dealer, loadDealerCallback: DataSource.LoadDealerCallback) {
        return mLocalDataSource.saveDealer(dealer, loadDealerCallback)
    }

    override fun getDealer(carFaxId: String, loadDealerCallback: DataSource.LoadDealerCallback) {
        return getDealer(carFaxId, loadDealerCallback)
    }

    override fun deleteAllListings() {
        mLocalDataSource.deleteAllListings()
    }

    override fun deleteAllDealers() {
        mLocalDataSource.deleteAllDealers()
    }

    override fun deleteSearchArea() {
        mLocalDataSource.deleteSearchArea()
    }

    override fun refreshTasks() {
        mCacheIsDirty = true
    }

    override fun getVehicles(loadVehicleCallback: DataSource.LoadVehicleCallback) {

        if (mCachedResponse != null && !mCacheIsDirty) {
            loadVehicleCallback.onVehicleLoaded(mCachedResponse!!)
            return
        }


        if (mCacheIsDirty) {
            getTasksFromRemoteDataSource(loadVehicleCallback)
        } else {
            mLocalDataSource.getVehicles(object : DataSource.LoadVehicleCallback {
                override fun onVehicleLoaded(vehicle: Vehicle.Listing) {

                }

                override fun onVehicleLoaded(result: Vehicle.Result) {
                    refreshCache(result)
                    loadVehicleCallback.onVehicleLoaded(mCachedResponse!!)
                }

                override fun onDataNotAvailable() {
                    getTasksFromRemoteDataSource(loadVehicleCallback)
                }
            })
        }
    }

    private fun getTasksFromRemoteDataSource(callback: DataSource.LoadVehicleCallback) {
        mRemoteDataSource.getVehicles(object : DataSource.LoadVehicleCallback {
            override fun onVehicleLoaded(vehicle: Vehicle.Listing) {

            }

            override fun onVehicleLoaded(result: Vehicle.Result) {
                refreshCache(result)
                refreshLocalDataSource(result)
                callback.onVehicleLoaded(mCachedResponse!!)
            }

            override fun onDataNotAvailable() {
                callback.onDataNotAvailable()
            }
        })
    }

    private fun refreshCache(result: Vehicle.Result) {
        mCachedResponse?.listings?.clear()
        if (mCachedResponse == null) {
            mCachedResponse = result
        }
        mCachedResponse = result
        mCacheIsDirty = false
    }

    private fun refreshLocalDataSource(result: Vehicle.Result) {
        deleteCache()
        saveResult(result)
    }

    private fun deleteCache() {
        mLocalDataSource.deleteSearchArea()
        mLocalDataSource.deleteAllDealers()
        mLocalDataSource.deleteAllListings()
    }

    companion object {

        private var INSTANCE: Repository? = null

        fun instance(
            mRemoteDataSource: RemoteDataSource,
            mLocalDataSource: LocalDataSource
        ): Repository {
            if (INSTANCE == null) {
                synchronized(Repository::class.java) {
                    INSTANCE = Repository(
                        mRemoteDataSource,
                        mLocalDataSource
                    )
                }
            }
            return INSTANCE as Repository
        }
    }
}
