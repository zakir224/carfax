package com.zakir.carfax.vehicles

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zakir.carfax.R
import com.zakir.carfax.VehicleViewModelFactory
import com.zakir.carfax.data.Vehicle
import android.content.Intent
import android.net.Uri
import com.zakir.carfax.vehicles.details.VehicleDetailsActivity


class VehicleListActivity : AppCompatActivity(), VehicleAdapter.VehicleItemClickListener {
    override fun onCallButtonClick(phoneNumber: String) {
        mViewModel.onCallButtonClick(phoneNumber)
    }

    override fun onVehicleClick(listing: Vehicle.Listing) {
        mViewModel.onVehicleItemClick(listing)
    }

    private lateinit var mViewModel: VehicleListViewModel
    private lateinit var mVehicleRecyclerView: RecyclerView
    private lateinit var mVehicleAdapter: VehicleAdapter
    private var mDealerPhone: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupListView()
        mViewModel = obtainViewModel(this)
    }

    override fun onResume() {
        super.onResume()
        mViewModel.getVehicles().observe(this, Observer {
            mVehicleAdapter.setVehicles(it)
        })

        mViewModel.getPhone().observe(this, Observer {
            mDealerPhone = it
        })

        mViewModel.shouldShowDetailsActivity().observe(this, Observer {
            if (it.isNotEmpty()) {
                openDetailsActivity(it)
                mViewModel.openingDetails()
            }
        })

        mViewModel.shouldCallDealer().observe(this, Observer {
            if(it) {
                mViewModel.callingDealer()
                callPhone(mDealerPhone!!)
            }
        })
    }

    private fun callPhone(mPhone: String) {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:$mPhone")
        startActivity(intent)
    }

    public fun openDetailsActivity(id: String) {
        val intent = Intent(this, VehicleDetailsActivity::class.java)
        intent.putExtra("listing_id", id)
        startActivity(intent)
    }

    private fun setupListView() {
        mVehicleRecyclerView = findViewById(R.id.vehicleListRecyclerView)
        mVehicleRecyclerView.layoutManager = LinearLayoutManager(this)
        mVehicleAdapter = VehicleAdapter(ArrayList(), this)
        mVehicleRecyclerView.adapter = mVehicleAdapter
    }

    private fun obtainViewModel(activity: AppCompatActivity): VehicleListViewModel {
        val mVehicleViewModelFactory = VehicleViewModelFactory.getInstance(application)

        return ViewModelProviders.of(activity, mVehicleViewModelFactory).get(VehicleListViewModel::class.java)
    }


}