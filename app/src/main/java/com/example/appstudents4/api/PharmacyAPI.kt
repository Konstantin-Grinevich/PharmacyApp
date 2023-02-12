package com.example.appstudents4.api

import com.example.appstudents.data.Pharmacy
import com.example.appstudents4.data.PharmacyNet
import retrofit2.Call
import retrofit2.http.*
import java.util.*


interface PharmacyAPI {
    @GET("pharmacy")
    fun getPharmacyList() : Call<List<Pharmacy>>

    @GET("pharmacyNet")
    fun getPharmacyNetList() : Call<List<PharmacyNet>>

    @GET("pharmacyNet?phamId={phamId}")
    fun getPharmacyNetListById(@Path("phamId") phamId: UUID) : Call<List<PharmacyNet>>

    @POST("pharmacy")
    fun createPharmacy(@Body pharmacy: Pharmacy) : Call<Pharmacy>

    @POST("pharmacyNet")
    fun createPharmacyNet(@Body pharmacyNet: PharmacyNet) : Call<PharmacyNet>

    @PUT("pharmacy/{id}")
    fun updatePharmacy(@Body pharmacy: Pharmacy, @Path("id") id: UUID) : Call<Pharmacy>

    @PUT("pharmacyNet/{id}")
    fun updatePharmacyNet(@Body pharmacyNet: PharmacyNet, @Path("id") id: UUID) : Call<PharmacyNet>

    @DELETE("pharmacy/{id}")
    fun deletePharmacy(@Path("id") id: UUID) : Call<Pharmacy>

    @DELETE("pharmacyNet/{id}")
    fun deletePharmacyNet(@Path("id") id: UUID) : Call<PharmacyNet>
}