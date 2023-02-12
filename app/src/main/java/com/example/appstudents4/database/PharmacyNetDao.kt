package com.example.appstudents4.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.appstudents.data.Pharmacy
import com.example.appstudents4.data.PharmacyNet
import java.util.*

@Dao
interface PharmacyNetDao {
    @Query("SELECT * FROM pharmacyNet WHERE phamId = (:id)")
    fun getPharmacyNetList(id: UUID): LiveData<List<PharmacyNet>>

    @Query("SELECT * FROM pharmacyNet WHERE phamId = (:id) ORDER BY number")
    fun getPharmacyNetListSortedByNum(id: UUID): LiveData<List<PharmacyNet>>

    @Query("SELECT * FROM pharmacyNet WHERE phamId = (:id) ORDER BY address")
    fun getPharmacyNetListSortedByAddress(id: UUID): LiveData<List<PharmacyNet>>

    @Query("SELECT * FROM pharmacyNet WHERE id=(:id)")
    fun getPharmacyNet(id: UUID): LiveData<PharmacyNet?>

    @Update
    fun updatePharmacyNet(pharmacyNet: PharmacyNet)

    @Insert
    fun addPharmacyNet(pharmacyNet: PharmacyNet)

    @Delete
    fun deletePharmacyNet(pharmacyNet: PharmacyNet)
}