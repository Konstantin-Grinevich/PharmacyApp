package com.example.appstudents4.pharmacy

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.appstudents.data.Pharmacy
import com.example.appstudents4.repository.PharmacyNetDBRepository

class PharmacyListDBViewModel : ViewModel() {
    private val pharmacyNetDBRepository = PharmacyNetDBRepository.get()
    val pharmacyListLiveData = pharmacyNetDBRepository.getPharmacyList()

}