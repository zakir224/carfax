package com.zakir.carfax.data.source.local

import com.zakir.carfax.data.Vehicle
import com.zakir.carfax.data.source.DataSource

interface LocalDataSource : DataSource{
    fun saveListing(vehicle: Vehicle.Listing)
    fun saveResult(result: Vehicle.Result)
    fun deleteAllListings()
    fun saveSearchArea(searchArea: Vehicle.SearchArea)
    fun getSearchArea(loadSearchAreaCallback: DataSource.LoadSearchAreaCallback)
    fun deleteSearchArea()
    fun saveDealer(dealer: Vehicle.Dealer, loadDealerCallback: DataSource.LoadDealerCallback)
    fun deleteAllDealers()
    fun getDealer(carFaxId: String, loadDealerCallback: DataSource.LoadDealerCallback)
    fun getDealer(id: Long, loadDealerCallback: DataSource.LoadDealerCallback)
}