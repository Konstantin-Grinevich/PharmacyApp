package com.example.appstudents4.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.appstudents.data.Pharmacy
import com.example.appstudents4.data.PharmacyNet

@Database(entities = [PharmacyNet::class, Pharmacy::class], version = 3)
@TypeConverters(PharmacyNetTypeConverters::class)
abstract class PharmacyNetDatabase : RoomDatabase() {
    abstract fun pharmacyNetDao() : PharmacyNetDao
    abstract fun pharmacyDao() : PharmacyDao
}