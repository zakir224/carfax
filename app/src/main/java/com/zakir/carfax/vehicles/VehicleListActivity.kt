package com.zakir.carfax.vehicles

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.zakir.carfax.R
import com.zakir.carfax.data.Vehicle
import com.zakir.carfax.data.source.DataSource
import com.zakir.carfax.data.source.Repository
import com.zakir.carfax.data.source.local.AppLocalDataSource
import com.zakir.carfax.data.source.local.VehicleDatabase
import com.zakir.carfax.data.source.remote.AppRemoteDataSource
import com.zakir.carfax.data.source.remote.RemoteDataSource
import com.zakir.carfax.util.AppExecutors

class VehicleListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.item_vehicle_view)
    }
}