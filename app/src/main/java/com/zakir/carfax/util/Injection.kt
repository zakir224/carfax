package com.zakir.carfax.util

import android.content.Context
import com.zakir.carfax.data.source.Repository
import com.zakir.carfax.data.source.local.AppLocalDataSource
import com.zakir.carfax.data.source.local.LocalDataSource
import com.zakir.carfax.data.source.local.VehicleDatabase
import com.zakir.carfax.data.source.remote.AppRemoteDataSource
import com.zakir.carfax.data.source.remote.RemoteDataSource

object Injection {

    fun provideRepository(context: Context): Repository {
        var mVehicleDatabase: VehicleDatabase = VehicleDatabase.getInstance(context)
        var mLocalDataSource :LocalDataSource = AppLocalDataSource.instance(AppExecutors(), mVehicleDatabase.vehicleListingDao(),
            mVehicleDatabase.vehicleDealerDao(),mVehicleDatabase.vehicleAreaDao())
        var mRemoteDataSource: RemoteDataSource = AppRemoteDataSource.instance()

        return Repository.instance(mRemoteDataSource, mLocalDataSource)
    }
}
