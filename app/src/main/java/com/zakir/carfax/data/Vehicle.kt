package com.zakir.carfax.data

import androidx.room.*

object Vehicle {

    data class Result(
        val searchArea: SearchArea,
        val totalListingCount: Int,
        val listings: ArrayList<Listing>
    )

    @Entity(tableName = "vehicle_search_area")
    data class SearchArea(
        @PrimaryKey(autoGenerate = true) var id: Long,
        var city: String,
        var state: String,
        var zip: String
    )

    @Entity(
        tableName = "vehicle_listing",
        foreignKeys = [ForeignKey(
            entity = Dealer::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("dealer_id"),
            onDelete = ForeignKey.CASCADE
        )]
    )

    data class Listing(
        @PrimaryKey var id: String,
        @ColumnInfo(name = "dealer_id") var dealerId: Long,
        var make: String,
        var model: String,
        var trim: String,
        var year: String,
        var listPrice: Float,
        var vin: String,
        var mileage: Int,
        var currentPrice: Float,
        var exteriorColor: String,
        var interiorColor: String,
        var engine: String,
        var drivetype: String,
        var fuel: String,
        var transmission: String,
        var bodytype: String,
        var firstPhoto: String?
    ) {

        @Ignore var images: Image? = null
        @Ignore var dealer: Dealer? = null
    }

    @Entity(tableName = "vehicle_dealer")
    data class Dealer(
        @PrimaryKey(autoGenerate = true) var id: Long,
        @ColumnInfo(name = "car_fax_id") var carfaxId: String,
        var name: String,
        var city: String,
        var state: String,
        var phone: String
    )

    data class Image(
        var firstPhoto: FirstPhoto
    )

    data class FirstPhoto(var large: String, var small: String, var medium: String)

}

