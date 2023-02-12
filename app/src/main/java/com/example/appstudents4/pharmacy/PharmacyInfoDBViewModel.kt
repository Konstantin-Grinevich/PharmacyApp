package com.example.appstudents4.pharmacy

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.appstudents.data.Pharmacy
import com.example.appstudents4.repository.PharmacyNetDBRepository
import java.util.*

class PharmacyInfoDBViewModel: ViewModel() {
    private val pharmacyNetRepository = PharmacyNetDBRepository.get()
    private val pharmacyIdLiveData = MutableLiveData<UUID>()

    var pharmacyLiveData: LiveData<Pharmacy?> =
        Transformations.switchMap(pharmacyIdLiveData) { pharmacyId ->
            pharmacyNetRepository.getPharmacy(pharmacyId)
        }

    fun loadPharmacy(pharmacyId: UUID) {
        pharmacyIdLiveData.value = pharmacyId
    }

    fun newPharmacy(pharmacy:Pharmacy) {
        pharmacyNetRepository.addPharmacy(pharmacy)
    }

    fun savePharmacy(pharmacy: Pharmacy) {
        pharmacyNetRepository.updatePharmacy(pharmacy)
    }

    fun deletePharmacy(pharmacy: Pharmacy) {
        pharmacyNetRepository.deletePharmacy(pharmacy)
    }
}