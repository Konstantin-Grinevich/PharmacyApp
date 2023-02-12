package com.example.appstudents4.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*
import com.example.appstudents.data.Pharmacy
import com.example.appstudents4.data.PharmacyNet
import java.util.*

@Dao
interface PharmacyDao {
    @Query("SELECT * FROM pharmacy")
    fun getPharmacyList(): LiveData<List<Pharmacy>>

    @Query("SELECT * FROM pharmacy ORDER BY name")
    fun getPharmacyListSortedByName(): LiveData<List<Pharmacy>>

    @Query("SELECT * FROM pharmacy WHERE id=(:id)")
    fun getPharmacy(id: UUID): LiveData<Pharmacy?>

    @Update
    fun updatePharmacy(pharmacy: Pharmacy)

    @Insert
    fun addPharmacy(pharmacy: Pharmacy)

    @Delete
    fun deletePharmacy(pharmacy: Pharmacy)
}