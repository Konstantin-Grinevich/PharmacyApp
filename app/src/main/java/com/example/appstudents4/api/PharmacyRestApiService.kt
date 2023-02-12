package com.example.appstudents4.api

import android.util.Log
import com.example.appstudents.data.Pharmacy
import com.example.appstudents.data.PharmacyList
import com.example.appstudents4.MyConstants.TAG
import com.example.appstudents4.data.PharmacyNet
import com.example.appstudents4.pharmacyNet.PharmacyNetListDBFragment
import com.example.appstudents4.repository.PharmacyNetDBRepository
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.streams.toList

class PharmacyRestApiService private constructor() {
    private var pharmacyAPI: PharmacyAPI? = null

    private val client = OkHttpClient.Builder()
        .connectTimeout(15L, TimeUnit.SECONDS)
        .readTimeout(15L, TimeUnit.SECONDS)
        .writeTimeout(15L, TimeUnit.SECONDS)
        .build()

    companion object {
        private var INSTANCE: PharmacyRestApiService? = null

        fun newInstance() {
            if (INSTANCE == null) {
                INSTANCE = PharmacyRestApiService()
            }
        }

        fun get(): PharmacyRestApiService {
            return INSTANCE
                ?: throw IllegalStateException("PharmacyRestApiService не инициализирован")
        }
    }

    private fun getAPI() {
        pharmacyAPI = null
        Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build().apply {
                pharmacyAPI = create(PharmacyAPI::class.java)
            }
    }

    fun fetchPharmacyList(pharmacyList: List<Pharmacy>) {
        if (pharmacyAPI == null) getAPI()
        if (pharmacyAPI != null) {
            val ids = pharmacyList.stream().map { p -> p.id }.toList()
            val pharmaciesRequest: Call<List<Pharmacy>> = pharmacyAPI!!.getPharmacyList()

            pharmaciesRequest.enqueue(object : Callback<List<Pharmacy>> {
                override fun onFailure(call: Call<List<Pharmacy>>, t: Throwable) {
                    Log.e(TAG, "Ошибка получения списка сетей аптек")
                }

                override fun onResponse(
                    call: Call<List<Pharmacy>>,
                    response: Response<List<Pharmacy>>
                ) {
                    val repos = PharmacyNetDBRepository.get()
                    response.body()?.forEach { p ->
                        if (ids.contains(p.id)) {
                            repos.updatePharmacy(p)
                        } else {
                            repos.addPharmacy(p)
                        }
                    }

                }

            })
        }
    }

    fun fetchPharmacyNetList(pharmacyNetList: List<PharmacyNet>) {
        if (pharmacyAPI == null) getAPI()
        if (pharmacyAPI != null) {

            val pharmaciesNetRequest: Call<List<PharmacyNet>> = pharmacyAPI!!.getPharmacyNetListById(pharmacyNetList.get(0).phamId)
            val ids = pharmacyNetList.stream().map { p -> p.id }.toList()

            pharmaciesNetRequest.enqueue(object : Callback<List<PharmacyNet>> {
                override fun onFailure(call: Call<List<PharmacyNet>>, t: Throwable) {
                    Log.e(TAG, "Ошибка получения списка аптек")
                }

                override fun onResponse(
                    call: Call<List<PharmacyNet>>,
                    response: Response<List<PharmacyNet>>
                ) {
                    val repos = PharmacyNetDBRepository.get()
                    response.body()?.forEach { p ->
                        if (ids.contains(p.id)) {
                            repos.updatePharmacyNet(p)
                        } else {
                            repos.addPharmacyNet(p)
                        }
                    }
                }
            })
        }
    }

    fun uploadPharmacy(pharmacy: Pharmacy) {
        if (pharmacyAPI == null) getAPI()
        if (pharmacyAPI != null) {
            val pharmaciesRequest: Call<Pharmacy> = pharmacyAPI!!.createPharmacy(pharmacy)


            pharmaciesRequest.enqueue(object : Callback<Pharmacy> {
                override fun onFailure(call: Call<Pharmacy>, t: Throwable) {
                    Log.e(TAG, "Ошибка получения списка сетей аптек")
                }

                override fun onResponse(call: Call<Pharmacy>, response: Response<Pharmacy>) {
                    Log.i(TAG, "Данные успешно загружены")
                }
            })
        }
    }

    fun uploadPharmacyNet(pharmacyNet: PharmacyNet) {
        if (pharmacyAPI == null) getAPI()
        if (pharmacyAPI != null) {
            val pharmaciesRequest: Call<PharmacyNet> = pharmacyAPI!!.createPharmacyNet(pharmacyNet)


            pharmaciesRequest.enqueue(object : Callback<PharmacyNet> {
                override fun onFailure(call: Call<PharmacyNet>, t: Throwable) {
                    Log.e(TAG, "Ошибка получения списка сетей аптек")
                }

                override fun onResponse(call: Call<PharmacyNet>, response: Response<PharmacyNet>) {
                    Log.i(TAG, "Данные успешно загружены")
                }
            })
        }
    }

    fun updatePharmacy(pharmacy: Pharmacy) {
        if (pharmacyAPI == null) getAPI()
        if (pharmacyAPI != null) {
            val pharmaciesRequest: Call<Pharmacy> =
                pharmacyAPI!!.updatePharmacy(pharmacy, pharmacy.id)
            pharmaciesRequest.enqueue(object : Callback<Pharmacy> {
                override fun onFailure(call: Call<Pharmacy>, t: Throwable) {
                    Log.e(TAG, "Ошибка получения списка сетей аптек")
                }

                override fun onResponse(call: Call<Pharmacy>, response: Response<Pharmacy>) {
                    Log.i(TAG, "Данные успешно обновлены")
                }
            })
        }
    }

    fun updatePharmacyNet(pharmacyNet: PharmacyNet) {
        if (pharmacyAPI == null) getAPI()
        if (pharmacyAPI != null) {
            val pharmaciesRequest: Call<PharmacyNet> =
                pharmacyAPI!!.updatePharmacyNet(pharmacyNet, pharmacyNet.id)
            pharmaciesRequest.enqueue(object : Callback<PharmacyNet> {
                override fun onFailure(call: Call<PharmacyNet>, t: Throwable) {
                    Log.e(TAG, "Ошибка получения списка сетей аптек")
                }

                override fun onResponse(call: Call<PharmacyNet>, response: Response<PharmacyNet>) {
                    Log.i(TAG, "Данные успешно обновлены")
                }
            })
        }
    }

    fun deletePharmacy(id: UUID) {
        if (pharmacyAPI == null) getAPI()
        if (pharmacyAPI != null) {
            val pharmaciesRequest: Call<Pharmacy> =
                pharmacyAPI!!.deletePharmacy(id)
            pharmaciesRequest.enqueue(object : Callback<Pharmacy> {
                override fun onFailure(call: Call<Pharmacy>, t: Throwable) {
                    Log.e(TAG, "Ошибка при удалении сети аптек")
                }

                override fun onResponse(call: Call<Pharmacy>, response: Response<Pharmacy>) {
                    Log.i(TAG, "Данные успешно удалены")
                }
            })
        }

        val pharmacyNet =
            PharmacyNetListDBFragment.getInstance().pharmacyNetListDBViewModel.pharmacyNetListLiveData.value
        if (!pharmacyNet.isNullOrEmpty()) {
            var uuids = pharmacyNet!!.stream()
                .map { p -> p.id }
                .toList()
            uuids.forEach { uuid ->
                val pharmaciesRequest: Call<PharmacyNet> =
                    pharmacyAPI!!.deletePharmacyNet(uuid)
                pharmaciesRequest.enqueue(object : Callback<PharmacyNet> {
                    override fun onFailure(call: Call<PharmacyNet>, t: Throwable) {
                        Log.e(TAG, "Ошибка при удалении аптеки")
                    }

                    override fun onResponse(
                        call: Call<PharmacyNet>,
                        response: Response<PharmacyNet>
                    ) {
                        Log.i(TAG, "Данные успешно удалены")
                    }
                })
            }
        }
    }

    fun deletePharmacyNet(id: UUID) {
        if (pharmacyAPI == null) getAPI()
        if (pharmacyAPI != null) {
            val pharmaciesRequest: Call<PharmacyNet> =
                pharmacyAPI!!.deletePharmacyNet(id)
            pharmaciesRequest.enqueue(object : Callback<PharmacyNet> {
                override fun onFailure(call: Call<PharmacyNet>, t: Throwable) {
                    Log.e(TAG, "Ошибка при удалении аптеки")
                }

                override fun onResponse(call: Call<PharmacyNet>, response: Response<PharmacyNet>) {
                    Log.i(TAG, "Данные успешно удалены")
                }
            })
        }
    }
}