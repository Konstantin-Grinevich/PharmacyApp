package com.example.appstudents4

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.preference.PreferenceManager
import com.example.appstudents.data.Pharmacy
import com.example.appstudents4.MyConstants.PHARMACY_LIST_FRAGMENT_TAG
import com.example.appstudents4.MyConstants.PHARMACY_NET_LIST_FRAGMENT_TAG
import com.example.appstudents4.api.PharmacyRestApiService
import com.example.appstudents4.data.PharmacyNet
import com.example.appstudents4.pharmacy.PharmacyInfoDBFragment
import com.example.appstudents4.pharmacy.PharmacyListDBFragment
import com.example.appstudents4.pharmacyNet.PharmacyNetInfoDBFragment
import com.example.appstudents4.pharmacyNet.PharmacyNetListDBFragment
import com.example.appstudents4.repository.PharmacyNetDBRepository
import java.util.*

class MainActivity : AppCompatActivity(),
    PharmacyNetListDBFragment.Callbacks,
    PharmacyNetInfoDBFragment.Callbacks,
    PharmacyInfoDBFragment.Callbacks,
    PharmacyListDBFragment.Callbacks
{
    private var miAdd: MenuItem? = null
    private var miPhamAdd: MenuItem? = null
    private var miSortPharmacyNetByNum: MenuItem? = null
    private var miSortPharmacyNetByAddress: MenuItem? = null
    private var miBack: MenuItem? = null
    private var miGetFromDB: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        showDBPharmacyList()
        val callback = object : OnBackPressedCallback(true)
        {
            override fun handleOnBackPressed() {
                checkLogout()
            }
        }
        onBackPressedDispatcher.addCallback(this, callback)
    }

    private fun checkLogout() {
        AlertDialog.Builder(this)
            .setTitle("Выход!")
            .setMessage("Вы действительно хотите выйти из приложения?")
            .setPositiveButton("Да") {
                _, _ -> finish()
            }
            .setNegativeButton("Нет", null)
            .setCancelable(true)
            .create()
            .show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        miAdd = menu.findItem(R.id.miAdd)
        miPhamAdd = menu.findItem(R.id.miPhamAdd)
        miSortPharmacyNetByNum = menu.findItem(R.id.miSortByNum)
        miSortPharmacyNetByAddress = menu.findItem(R.id.miSortByAddress)
        miBack = menu.findItem(R.id.miBack)
        miGetFromDB = menu.findItem(R.id.miGetFromDB)

        miBack?.isVisible = false
        miAdd?.isVisible = false
        miSortPharmacyNetByNum?.isVisible = false
        miSortPharmacyNetByAddress?.isVisible = false
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.miAdd -> {
                //showNewStudent()
                showPharmacyNetDetailDB(PharmacyNet().id)
                true
            }
            R.id.miPhamAdd -> {
                showPharmacyDetailDB(Pharmacy().id)
                true
            }
            R.id.miBack -> {
                showDBPharmacyList()
                miBack?.isVisible = false
                miPhamAdd?.isVisible = true
                miAdd?.isVisible = false
                miSortPharmacyNetByNum?.isVisible = false
                miSortPharmacyNetByAddress?.isVisible = false
                true
            }
            R.id.miSortByNum -> {
                showDBPharmacyNetListSortedByNum()
                true
            }
            R.id.miSortByAddress -> {
                showDBPharmacyNetListSortedByAddress()
                true
            }
            R.id.miGetFromDB -> {
                miGetFromDB?.isVisible = false
                //PharmacyRestApiService.get().fetchPharmacyList()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }

    fun checkDeletePharmacyNet(pharmacyNet: PharmacyNet){
        val s = pharmacyNet.address + " "+
                pharmacyNet.openingHours
        AlertDialog.Builder(this)
            .setTitle("УДАЛЕНИЕ!")
            .setMessage("Вы действительно хотите удалить аптеку $s ?")

            .setPositiveButton("Да"){_, _ ->
                PharmacyNetDBRepository.get().deletePharmacyNet(pharmacyNet)
                PharmacyRestApiService.get().deletePharmacyNet(pharmacyNet.id)

            }
            .setNegativeButton("Нет", null)
            .setCancelable(true)
            .create()
            .show()
    }

    fun checkDeletePharmacy(pharmacy: Pharmacy){
        val s = pharmacy.name
        AlertDialog.Builder(this)
            .setTitle("УДАЛЕНИЕ!")
            .setMessage("Вы действительно хотите удалить сеть аптеку $s ?")
            .setPositiveButton("Да"){_, _ ->
                PharmacyNetDBRepository.get().deletePharmacy(pharmacy)
                PharmacyRestApiService.get().deletePharmacy(pharmacy.id)
            }
            .setNegativeButton("Нет", null)
            .setCancelable(true)
            .create()
            .show()
    }

    override fun onPharmacyNetSelected(pharmacyNetId: UUID) {
        showPharmacyNetDetailDB(pharmacyNetId)
    }

        override fun onPharmacyNetLongClicked(pharmacyNet: PharmacyNet) {
        checkDeletePharmacyNet(pharmacyNet)
    }

    private fun showPharmacyNetDetailDB(pharmacyId: UUID) {
        val fragment = PharmacyNetInfoDBFragment.newInstance(pharmacyId)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frame, fragment)
            .commit()
    }

    private fun showPharmacyDetailDB(pharmacyId: UUID) {
        val fragment = PharmacyInfoDBFragment.newInstance(pharmacyId)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frame, fragment)
            .commit()
    }

    override fun showDBPharmacyNetList() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frame, PharmacyNetListDBFragment.getInstance(), PHARMACY_NET_LIST_FRAGMENT_TAG)
            .commit()
    }

    override fun showDBPharmacyList() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frame, PharmacyListDBFragment.getInstance(), PHARMACY_LIST_FRAGMENT_TAG)
            .commit()
    }

    override fun onPharmacySelected(pharmacyId: UUID) {
        miAdd?.isVisible = true
        miPhamAdd?.isVisible = false
        miBack?.isVisible = true
        miSortPharmacyNetByNum?.isVisible = true
        miSortPharmacyNetByAddress?.isVisible = true
        miGetFromDB?.isVisible = false

        showDBPharmacyNetList()
    }

    override fun onPharmacyLongClicked(pharmacy: Pharmacy) {
        checkDeletePharmacy(pharmacy)
    }

    fun showDBPharmacyNetListSortedByNum() {
        PreferenceManager.getDefaultSharedPreferences(AppStudentIntentApplication.applicationContext())
            .edit().apply() {
                putString("sortedBy", "Number")
                apply()
            }
        PharmacyNetListDBFragment.getInstance().loadPharmacyNetList()
    }

    fun showDBPharmacyNetListSortedByAddress() {
        PreferenceManager.getDefaultSharedPreferences(AppStudentIntentApplication.applicationContext())
            .edit().apply() {
                putString("sortedBy", "Address")
                apply()
            }
        PharmacyNetListDBFragment.getInstance().loadPharmacyNetList()
    }

}