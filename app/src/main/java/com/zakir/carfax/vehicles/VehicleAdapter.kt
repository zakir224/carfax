package com.zakir.carfax.vehicles

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zakir.carfax.R
import com.zakir.carfax.data.Vehicle

import java.util.ArrayList

class VehicleAdapter(
    private var mVehicleListings: ArrayList<Vehicle.Listing>,
    val mVehicleItemClickListener: VehicleItemClickListener
) : RecyclerView.Adapter<VehicleAdapter.VehicleViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VehicleViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_vehicle_view, parent, false)
        return VehicleViewHolder(view)
    }

    override fun onBindViewHolder(holder: VehicleViewHolder, position: Int) {
        val listing = mVehicleListings!![position]
        val s = listing.make +
                " " + listing.model +
                " " + listing.trim +
                " " + listing.year
        holder.mVehicleTitleView.text = s
        holder.mVehicleMileageView.text = StringBuilder().append(listing.mileage).append(" mi").toString()
        holder.mVehicleLocationView.text = StringBuilder()
            .append(listing.dealer?.city).append(", ").append(listing.dealer?.state)
        holder.mVehiclePriceView.text = String.format("%s", listing.listPrice)
        if (listing.images != null) {
            Glide.with(holder.itemView.context)
                .load(listing.images?.firstPhoto?.large)
                .into(holder.mVehicleImageView)
        } else {
            Glide.with(holder.itemView.context)
                .load(listing.firstPhoto)
                .into(holder.mVehicleImageView)
        }

        holder.mCallDealerButton.setOnClickListener {
            mVehicleItemClickListener.onCallButtonClick(listing.dealer?.phone!!)
        }

        holder.itemView.setOnClickListener {
            mVehicleItemClickListener.onVehicleClick(listing)
        }
    }

    fun setVehicles(vehicles: List<Vehicle.Listing>) {
        this.mVehicleListings = vehicles as ArrayList<Vehicle.Listing>
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return mVehicleListings!!.size
    }

    inner class VehicleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var mVehicleImageView: ImageView = itemView.findViewById(R.id.vehicleImageView)
        var mVehicleTitleView: TextView = itemView.findViewById(R.id.vehicleTitleTextView)
        var mVehiclePriceView: TextView = itemView.findViewById(R.id.vehiclePriceTextView)
        var mVehicleMileageView: TextView = itemView.findViewById(R.id.vehicleMileageTextView)
        var mVehicleLocationView: TextView = itemView.findViewById(R.id.vehicleLocationTextView)
        var mCallDealerButton: Button = itemView.findViewById(R.id.callDealerButton)
    }

    public interface VehicleItemClickListener {
        fun onCallButtonClick(phoneNumber: String)
        fun onVehicleClick(listing: Vehicle.Listing)
    }
}
