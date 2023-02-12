package com.example.appstudents4.pharmacyNet

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.preference.PreferenceManager
import com.example.appstudents4.AppStudentIntentApplication
import com.example.appstudents4.MyConstants
import com.example.appstudents4.repository.PharmacyNetDBRepository
import java.util.*

class PharmacyNetListDBViewModel : ViewModel() {
    private val pharmacyNetDBRepository = PharmacyNetDBRepository.get()
    private val pharmacyIdLiveData = MutableLiveData<UUID>()
    var pharmacyNetListLiveData = Transformations.switchMap(pharmacyIdLiveData) { pharmacyNetId ->
        var type = PreferenceManager.getDefaultSharedPreferences(AppStudentIntentApplication.applicationContext())
            .getString("sortedBy", "")
        val preference =
            PreferenceManager.getDefaultSharedPreferences(AppStudentIntentApplication.applicationContext())
        preference.edit().apply() {
            remove("sortedBy")
            apply()
        }
        when (type) {
            "Number" -> pharmacyNetDBRepository.getPharmacyNetListSortedByNum(pharmacyNetId)
            "Address" -> pharmacyNetDBRepository.getPharmacyNetListSortedByAddress(pharmacyNetId)
            else -> {pharmacyNetDBRepository.getPharmacyNetList(pharmacyNetId)}
        }
    }

    fun loadPharmacyNetList(pharmacyId: UUID) {
        pharmacyIdLiveData.value = pharmacyId
    }

}
