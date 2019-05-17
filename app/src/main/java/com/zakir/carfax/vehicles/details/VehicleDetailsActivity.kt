package com.zakir.carfax.vehicles.details

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.zakir.carfax.R
import com.zakir.carfax.VehicleViewModelFactory
import com.zakir.carfax.data.Vehicle
import com.zakir.carfax.vehicles.VehicleListActivity

class VehicleDetailsActivity : AppCompatActivity() {

    private lateinit var mViewModel: VehicleDetailsViewModel
    private lateinit var mVehicleImageView: ImageView
    private lateinit var mVehicleTitleView: TextView
    private lateinit var mVehiclePriceView: TextView
    private lateinit var mVehicleMileageView: TextView
    private lateinit var mVehicleLocationView: TextView
    private lateinit var mVehicleExteriorColorView: TextView
    private lateinit var mVehicleInteriorColorView: TextView
    private lateinit var mVehicleBodyTypeView: TextView
    private lateinit var mVehicleDriveTypeView: TextView
    private lateinit var mVehicleTransmissionView: TextView
    private lateinit var mVehicleFuelView: TextView
    private lateinit var mCallDealerButton: Button
    private lateinit var mPhoneNumber: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vehicle_details)

        setupViews()
        mViewModel = obtainViewModel(this)

        setupObservers()
        setVehicle(intent)

    }

    private fun setupObservers() {
        mViewModel.getVehicle().observe(this, Observer {
            updateViews(it)
        })
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

    private fun updateViews(it: Vehicle.Listing?) {
        val title = """${it?.make} ${it?.model} ${it?.year} ${it?.trim}"""
        val mileage = "${it?.mileage} mi"

        this.title = title
        mVehicleTitleView.text = title
        mVehiclePriceView.text = """${it?.currentPrice}"""
        mVehicleMileageView.text = mileage
        mVehicleExteriorColorView.text = it?.exteriorColor
        mVehicleInteriorColorView.text = it?.interiorColor
        mVehicleBodyTypeView.text = it?.bodytype
        mVehicleDriveTypeView.text = it?.drivetype
        mVehicleTransmissionView.text = it?.transmission
        mVehicleFuelView.text = it?.fuel

        mViewModel.getDealer(it?.dealerId!!).observe(this, Observer {
            val location = """${it?.city}, ${it?.state}"""
            mVehicleLocationView.text = location
            mPhoneNumber = it?.phone!!
        })

        mViewModel.shouldCallDealer().observe(this, Observer {
            if (it) {
                callPhone()
                mViewModel.setDealerCallDone()
            }
        })

        if (it.firstPhoto != null) {
            Glide.with(this)
                .load(it.firstPhoto)
                .into(mVehicleImageView)
        }

        mCallDealerButton.setOnClickListener {
            mViewModel.onCallButtonClick()
        }
    }

    private fun callPhone() {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:$mPhoneNumber")
        startActivity(intent)
    }

    private fun setupViews() {
        mVehicleImageView = findViewById(R.id.vehicleImageView)
        mVehicleTitleView = findViewById(R.id.vehicleTitleTextView)
        mVehiclePriceView = findViewById(R.id.vehiclePriceTextView)
        mVehicleMileageView = findViewById(R.id.vehicleMileageTextView)
        mVehicleLocationView = findViewById(R.id.vehicleLocationTextView)
        mVehicleExteriorColorView = findViewById(R.id.exteriorColorTextView)
        mVehicleInteriorColorView = findViewById(R.id.interiorColorTextView)
        mVehicleBodyTypeView = findViewById(R.id.bodyStyleTextView)
        mVehicleDriveTypeView = findViewById(R.id.driveTypeTextView)
        mVehicleTransmissionView = findViewById(R.id.transmissionTextView)
        mVehicleFuelView = findViewById(R.id.fuelTextView)
        mCallDealerButton = findViewById(R.id.callDealerButton)
    }
}
