package com.zakir.carfax.data.source.local

import com.zakir.carfax.data.Vehicle
import com.zakir.carfax.data.source.DataSource
import com.zakir.carfax.util.AppExecutors

class AppLocalDataSource private constructor(
    private val appExecutors: AppExecutors,
    private val vehicleDao: VehicleDao,
    private val dealerDao: VehicleDealerDao,
    private val searchDao: VehicleSearchDao
) : LocalDataSource {
    override fun refreshTasks() {
        // handled by the repo itself
    }

    override fun getSearchArea(loadSearchAreaCallback: DataSource.LoadSearchAreaCallback) {
        appExecutors.diskIO().execute{
            val area = searchDao.getSearchArea()
            loadSearchAreaCallback.onAreaLoaded(area)
        }
    }

    override fun getDealer(id: Long, loadDealerCallback: DataSource.LoadDealerCallback) {
        appExecutors.diskIO().execute{
            val dealer = dealerDao.getDealer(id)
            loadDealerCallback.onDealerFound(dealer)
        }
    }

    override fun saveListing(vehicle: Vehicle.Listing) {
        appExecutors.diskIO().execute{vehicleDao.addListing(vehicle)}
    }

    override fun deleteAllListings() {
        appExecutors.diskIO().execute{vehicleDao.deleteListing()}
    }

    override fun saveSearchArea(searchArea: Vehicle.SearchArea) {
        appExecutors.diskIO().execute{searchDao.addSearchArea(searchArea)}
    }

    override fun deleteSearchArea() {
        appExecutors.diskIO().execute{searchDao.deleteSearchArea()}
    }

    override fun saveDealer(dealer: Vehicle.Dealer, loadDealerCallback: DataSource.LoadDealerCallback) {
        appExecutors.diskIO().execute{
            val addedDealer = dealerDao.addDealer(dealer)
            loadDealerCallback.onDealerFound(addedDealer)
        }
    }

    override fun getDealer(carFaxId: String, loadDealerCallback: DataSource.LoadDealerCallback) {
        appExecutors.diskIO().execute{
            val addedDealer = dealerDao.getDealer(carFaxId)
            loadDealerCallback.onDealerFound(addedDealer)
        }
    }

    override fun deleteAllDealers() {
        appExecutors.diskIO().execute{dealerDao.deleteDealer()}
    }


    override fun getVehicles(loadVehicleCallback: DataSource.LoadVehicleCallback) {
        val runnable = Runnable {
            val vehicles = vehicleDao.getAll()
            if(vehicles.isEmpty()) {
                appExecutors.mainThread().execute { loadVehicleCallback.onDataNotAvailable() }
                return@Runnable
            }
            val searchArea = searchDao.getSearchArea()

            for (vehicle in vehicles) {
                vehicle.dealer = dealerDao.getDealer(vehicle.dealerId)
            }

            val vehiclesCache: Vehicle.Result = Vehicle.Result(
                searchArea,
                vehicles.size, vehicles as ArrayList<Vehicle.Listing>
            )

            appExecutors.mainThread().execute {
                if (vehicles.isEmpty()) {
                    // This will be called if the table is new or just empty.
                    loadVehicleCallback.onDataNotAvailable()
                } else {
                    loadVehicleCallback.onVehicleLoaded(vehiclesCache)
                }
            }
        }

        appExecutors.diskIO().execute(runnable)
    }

    override fun saveResult(result: Vehicle.Result) {
        val runnable = Runnable {
            for (vehicle in result.listings) {
                val savedDealer: Long
                val existingDealer = dealerDao.getDealer(vehicle.dealer!!.carfaxId)
                savedDealer = if (existingDealer > 0) {
                    existingDealer
                } else {
                    dealerDao.addDealer(vehicle.dealer!!)
                }
                vehicle.dealerId = savedDealer
                vehicle.firstPhoto = vehicle.images?.firstPhoto?.large
                vehicleDao.addListing(vehicle)
            }
            searchDao.addSearchArea(result.searchArea)
        }

        appExecutors.diskIO().execute(runnable)
    }

    companion object {

        private var INSTANCE: AppLocalDataSource? = null

        fun instance(
            appExecutors: AppExecutors,
            vehicleDao: VehicleDao,
            dealerDao: VehicleDealerDao,
            searchDao: VehicleSearchDao
        ): LocalDataSource {
            if (INSTANCE == null) {
                INSTANCE = AppLocalDataSource(appExecutors, vehicleDao, dealerDao, searchDao)
            }
            return INSTANCE as AppLocalDataSource
        }
    }

    override fun getVehicle(id: String, loadDealerCallback: DataSource.LoadVehicleCallback) {
        val runnable = Runnable {
            val vehicle = vehicleDao.findById(id)
            loadDealerCallback.onVehicleLoaded(vehicle)
        }

        appExecutors.diskIO().execute(runnable)
    }
}