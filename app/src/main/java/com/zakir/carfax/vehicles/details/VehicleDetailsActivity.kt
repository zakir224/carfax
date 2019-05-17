package com.zakir.carfax.vehicles.details

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.zakir.carfax.R
import com.zakir.carfax.VehicleViewModelFactory
import com.zakir.carfax.data.Vehicle
import com.zakir.carfax.databinding.ActivityVehicleDetailsBinding
import com.zakir.carfax.vehicles.VehicleListActivity

class VehicleDetailsActivity : AppCompatActivity(), LifecycleOwner {

    private lateinit var mDetailBinding: ActivityVehicleDetailsBinding
    private lateinit var mViewModel: VehicleDetailsViewModel
    private lateinit var mPhoneNumber: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_vehicle_details)
        mViewModel = obtainViewModel(this)

        setupObservers()
        setVehicle(intent)
    }

    private fun setupObservers() {
        mViewModel.getVehicle().observe(this, Observer {
            updateView(it)
        })

        mViewModel.getDealer().observe(this, Observer {
            val location = """${it?.city}, ${it?.state}"""
            mDetailBinding.location = location
            mPhoneNumber = it?.phone!!
        })

        mViewModel.shouldCallDealer().observe(this, Observer {
            if (it) {
                callPhone()
                mViewModel.setDealerCallDone()
            }
        })
    }

    private fun updateView(it: Vehicle.Listing?) {
        val title = """${it?.make} ${it?.model} ${it?.year} ${it?.trim}"""
        val mileage = "${it?.mileage} mi"
        this.title = title

        mDetailBinding.title = title
        mDetailBinding.mileage = mileage
        mDetailBinding.price = """${it?.currentPrice}"""
        mDetailBinding.bodyStyle = it?.bodytype
        mDetailBinding.driveType = it?.drivetype
        mDetailBinding.exteriorColor = it?.exteriorColor
        mDetailBinding.interiorColor = it?.interiorColor
        mDetailBinding.transmission = it?.transmission
        mDetailBinding.fuel = it?.fuel

        if (it?.firstPhoto != null) {
            Glide.with(this)
                .load(it.firstPhoto)
                .centerCrop()
                .into(mDetailBinding.vehicleImageView)
        }

        mDetailBinding.callDealerButton.setOnClickListener {
            mViewModel.onCallButtonClick()
        }
    }

    private fun setVehicle(intent: Intent?) {
        val vehicleId = intent?.getStringExtra(VehicleListActivity.VEHICLE)
        mViewModel.setVehicleId(vehicleId!!)
    }

    private fun obtainViewModel(activity: AppCompatActivity): VehicleDetailsViewModel {
        val mVehicleViewModelFactory = VehicleViewModelFactory.getInstance(application)

        return ViewModelProviders.of(
            activity,
            mVehicleViewModelFactory
        ).get(VehicleDetailsViewModel::class.java)
    }

    private fun callPhone() {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:$mPhoneNumber")
        startActivity(intent)
    }

}
