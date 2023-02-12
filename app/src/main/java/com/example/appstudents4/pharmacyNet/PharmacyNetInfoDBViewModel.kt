package com.example.appstudents4.pharmacyNet

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.appstudents4.data.PharmacyNet
import com.example.appstudents4.repository.PharmacyNetDBRepository
import java.util.*

class PharmacyNetInfoDBViewModel: ViewModel() {
    private val pharmacyNetRepository = PharmacyNetDBRepository.get()
    private val pharmacyNetIdLiveData = MutableLiveData<UUID>()

    var pharmacyNetLiveData: LiveData<PharmacyNet?> =
        Transformations.switchMap(pharmacyNetIdLiveData) { pharmacyNetId ->
            pharmacyNetRepository.getPharmacyNet(pharmacyNetId)
        }

    fun loadPharmacyNet(pharmacyNetId: UUID) {
        pharmacyNetIdLiveData.value = pharmacyNetId
    }

    fun newPharmacyNet(pharmacyNet:PharmacyNet) {
        pharmacyNetRepository.addPharmacyNet(pharmacyNet)
    }

    fun savePharmacyNet(pharmacyNet: PharmacyNet) {
        pharmacyNetRepository.updatePharmacyNet(pharmacyNet)
    }

    fun deletePharmacyNet(pharmacyNet: PharmacyNet) {
        pharmacyNetRepository.deletePharmacyNet(pharmacyNet)
    }
}