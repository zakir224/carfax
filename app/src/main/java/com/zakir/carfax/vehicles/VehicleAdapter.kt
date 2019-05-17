package com.zakir.carfax.vehicles

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zakir.carfax.data.Vehicle
import com.zakir.carfax.databinding.ItemVehicleViewBinding
import java.util.*

class VehicleAdapter(
    private var mVehicleListings: ArrayList<Vehicle.Listing>,
    private val mVehicleItemClickListener: VehicleItemClickListener
) : RecyclerView.Adapter<VehicleAdapter.VehicleViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VehicleViewHolder {
        val listItemBinding = ItemVehicleViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VehicleViewHolder(listItemBinding)
    }

    override fun onBindViewHolder(holder: VehicleViewHolder, position: Int) {
        val listing = mVehicleListings!![position]
        val s = listing.make +
                " " + listing.model +
                " " + listing.trim +
                " " + listing.year
        holder.itemViewBinding.title = s
        holder.itemViewBinding.mileage = StringBuilder().append(listing.mileage).append(" mi").toString()
        holder.itemViewBinding.location = StringBuilder()
            .append(listing.dealer?.city).append(", ").append(listing.dealer?.state).toString()
        holder.itemViewBinding.price = String.format("%s", listing.listPrice)
        if (listing.images != null) {
            Glide.with(holder.itemView.context)
                .load(listing.images?.firstPhoto?.large)
                .into(holder.itemViewBinding.vehicleImageView)
        } else {
            Glide.with(holder.itemView.context)
                .load(listing.firstPhoto)
                .into(holder.itemViewBinding.vehicleImageView)
        }

        holder.itemViewBinding.callDealerButton.setOnClickListener {
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
        return mVehicleListings.size
    }

    inner class VehicleViewHolder(var itemViewBinding: ItemVehicleViewBinding) :
        RecyclerView.ViewHolder(itemViewBinding.root)

    interface VehicleItemClickListener {
        fun onCallButtonClick(phoneNumber: String)
        fun onVehicleClick(listing: Vehicle.Listing)
    }
}
