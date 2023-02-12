package com.example.appstudents4

import android.app.Application
import android.content.Context
import com.example.appstudents4.api.PharmacyRestApiService
import com.example.appstudents4.repository.PharmacyNetDBRepository

class AppStudentIntentApplication : Application() {

    init {
        instance = this
    }

    companion object {
        private var instance: AppStudentIntentApplication? = null
        fun applicationContext() : Context {
            return instance!!.applicationContext
        }
    }
    override fun onCreate() {
        super.onCreate()
        PharmacyNetDBRepository.initialize(this)
        PharmacyRestApiService.newInstance()
    }
}