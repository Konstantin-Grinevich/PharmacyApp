package com.example.appstudents4.repository

import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.example.appstudents.data.Pharmacy
import com.example.appstudents4.data.PharmacyNet
import com.example.appstudents4.database.PharmacyNetDatabase
import java.lang.NullPointerException
import java.util.*
import java.util.concurrent.Executors

private const val DATABASE_NAME : String = "pharmacy-net-database"

class PharmacyNetDBRepository private constructor(context: Context) {

    companion object {
        private var INSTANCE : PharmacyNetDBRepository? = null
        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = PharmacyNetDBRepository(context)
            }
        }
        fun get(): PharmacyNetDBRepository {
            return INSTANCE?: throw IllegalStateException("PharmacyNetDBRepository must be initialized")
        }
    }
    private val database: PharmacyNetDatabase = Room.databaseBuilder(
        context.applicationContext,
        PharmacyNetDatabase::class.java,
        DATABASE_NAME
    )
        .fallbackToDestructiveMigration()
        .build()

    private val pharmacyNetDao = database.pharmacyNetDao()
    private val pharmacyDao = database.pharmacyDao()

    fun getPharmacyNetList(id: UUID): LiveData<List<PharmacyNet>> = pharmacyNetDao.getPharmacyNetList(id)
    fun getPharmacyNetListSortedByNum(id: UUID): LiveData<List<PharmacyNet>> = pharmacyNetDao.getPharmacyNetListSortedByNum(id)
    fun getPharmacyNetListSortedByAddress(id: UUID): LiveData<List<PharmacyNet>> = pharmacyNetDao.getPharmacyNetListSortedByAddress(id)
    fun getPharmacyNet(id: UUID): LiveData<PharmacyNet?> = pharmacyNetDao.getPharmacyNet(id)

    fun getPharmacyList(): LiveData<List<Pharmacy>> = pharmacyDao.getPharmacyList()
    fun getPharmacy(id: UUID): LiveData<Pharmacy?> = pharmacyDao.getPharmacy(id)

    private val executor = Executors.newSingleThreadExecutor()
    fun updatePharmacyNet(pharmacyNet: PharmacyNet) {
        executor.execute {
            pharmacyNetDao.updatePharmacyNet(pharmacyNet)
        }
    }

    fun addPharmacyNet(pharmacyNet:PharmacyNet) {
        try {
        executor.execute {
            pharmacyNetDao.addPharmacyNet(pharmacyNet)
        }
        } catch (e : SQLiteConstraintException) {
            throw NullPointerException()
        }
    }

    fun deletePharmacyNet(pharmacyNet:PharmacyNet) {
        executor.execute {
            pharmacyNetDao.deletePharmacyNet(pharmacyNet)
        }
    }

    fun updatePharmacy(pharmacy: Pharmacy) {
        executor.execute {
            pharmacyDao.updatePharmacy(pharmacy)
        }
    }

    fun addPharmacy(pharmacy:Pharmacy) {
        executor.execute {
            pharmacyDao.addPharmacy(pharmacy)
        }
    }

    fun deletePharmacy(pharmacyNet:Pharmacy) {
        executor.execute {
            pharmacyDao.deletePharmacy(pharmacyNet)
        }
    }
}
