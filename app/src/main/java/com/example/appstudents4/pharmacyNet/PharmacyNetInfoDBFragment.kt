package com.example.appstudents4.pharmacyNet

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.isDigitsOnly
import androidx.preference.PreferenceManager
import com.example.appstudents4.AppStudentIntentApplication
import com.example.appstudents4.MyConstants.DEFAULT_UUID
import com.example.appstudents4.MyConstants.FILL_FIELD_MESSAGE
import com.example.appstudents4.MyConstants.NO_DIGITS_FIELD_MESSAGE
import com.example.appstudents4.R
import com.example.appstudents4.api.PharmacyRestApiService
import com.example.appstudents4.data.PharmacyNet
import java.util.*

private const val PHARMACY_NET_ID_TAG : String = "pharmacy-net-id"

class PharmacyNetInfoDBFragment : Fragment() {

    private var pharmacyNet:PharmacyNet? = null
    private lateinit var pharmacyNetInfoDBViewModel: PharmacyNetInfoDBViewModel
    private lateinit var etNumber: EditText
    private lateinit var etAddress : EditText
    private lateinit var etHours : EditText
    private lateinit var etEmployees : EditText
    private lateinit var etCity : EditText
    private lateinit var etSquare : EditText
    private lateinit var etAttendance : EditText
    private lateinit var dpDate : DatePicker
    private lateinit var btnSave : Button
    private lateinit var btnCancel : Button

    companion object {
        fun newInstance(pharmacyNetId:UUID): PharmacyNetInfoDBFragment {
            val args = Bundle().apply {
                putString(PHARMACY_NET_ID_TAG, pharmacyNetId.toString())
            }

            return PharmacyNetInfoDBFragment().apply {
                arguments = args
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as? AppCompatActivity)?.supportActionBar?.title = "Аптека"
        val pharmacyNetId:UUID = UUID.fromString(arguments?.getString(PHARMACY_NET_ID_TAG))
        pharmacyNetInfoDBViewModel = ViewModelProvider(this).get(PharmacyNetInfoDBViewModel::class.java)
        pharmacyNetInfoDBViewModel.loadPharmacyNet(pharmacyNetId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.student_info, container, false)
        etNumber = view.findViewById(R.id.etNumber)
        etAddress = view.findViewById(R.id.etAddress)
        etHours = view.findViewById(R.id.etHours)
        etEmployees = view.findViewById(R.id.etEmployees)
        etCity = view.findViewById(R.id.etCity)
        etSquare = view.findViewById(R.id.square)
        etAttendance = view.findViewById(R.id.attendance)
        dpDate = view.findViewById(R.id.date)
        btnSave = view.findViewById(R.id.btnSave)
        btnSave.setOnClickListener{
            if (validateFields()) {
                if (pharmacyNet == null) {
                    pharmacyNet = PharmacyNet()
                    updatePharmacyNet()
                    pharmacyNetInfoDBViewModel.newPharmacyNet(pharmacyNet!!)
                    PharmacyRestApiService.get().uploadPharmacyNet(pharmacyNet!!)
                } else {
                    updatePharmacyNet()
                    pharmacyNetInfoDBViewModel.savePharmacyNet(pharmacyNet!!)
                    PharmacyRestApiService.get().updatePharmacyNet(pharmacyNet!!)
                }
                callbacks?.showDBPharmacyNetList()
            }
        }
        btnCancel = view.findViewById(R.id.btnCancel)
        btnCancel.setOnClickListener {
            callbacks?.showDBPharmacyNetList()
        }
        return view
    }

    fun updatePharmacyNet() {
        val dateBirth = GregorianCalendar(dpDate.year, dpDate.month, dpDate.dayOfMonth)
        pharmacyNet?.number = etNumber.text.toString().toInt()
        pharmacyNet?.address = etAddress.text.toString()
        pharmacyNet?.openingHours =etHours.text.toString()
        pharmacyNet?.birthDate =dateBirth.time
        pharmacyNet?.employees =etEmployees.text.toString()
        pharmacyNet?.city =etCity.text.toString()
        pharmacyNet?.square =etSquare.text.toString()
        pharmacyNet?.attendance =etAttendance.text.toString()
        pharmacyNet?.phamId = UUID.fromString(
            PreferenceManager.getDefaultSharedPreferences(AppStudentIntentApplication.applicationContext())
                .getString("pharmacyId", DEFAULT_UUID))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pharmacyNetInfoDBViewModel.pharmacyNetLiveData.observe(viewLifecycleOwner) { pharmacyNet ->
            pharmacyNet?.let {
                this.pharmacyNet = pharmacyNet
                updateUI()
            }
        }
    }

    fun updateUI() {
        etNumber.setText(pharmacyNet?.number.toString())
        etAddress.setText(pharmacyNet?.address)
        etHours.setText(pharmacyNet?.openingHours)
        val dateBirth = GregorianCalendar()
        dateBirth.time = pharmacyNet?.birthDate
        dpDate.updateDate(dateBirth.get(Calendar.YEAR), dateBirth.get(Calendar.MONTH), dateBirth.get(Calendar.DAY_OF_MONTH))
        etEmployees.setText(pharmacyNet?.employees)
        etCity.setText(pharmacyNet?.city)
        etSquare.setText(pharmacyNet?.square)
        etAttendance.setText(pharmacyNet?.attendance)
    }

    interface Callbacks {
        fun showDBPharmacyNetList()
    }

    private var callbacks: Callbacks? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    fun validateFields() : Boolean{
        var isValidate = true

        when {
            etNumber.text.toString().isBlank() -> {
                etNumber.error = FILL_FIELD_MESSAGE
                isValidate = false
            }
            !etNumber.text.toString().isDigitsOnly() -> {
                etNumber.error = NO_DIGITS_FIELD_MESSAGE
                isValidate = false
            }
            etAddress.text.toString().isBlank() -> {
                etAddress.error = FILL_FIELD_MESSAGE
                isValidate = false
            }
            etHours.text.toString().isBlank() -> {
                etHours.error = FILL_FIELD_MESSAGE
                isValidate = false
            }
            etEmployees.text.toString().isBlank() -> {
                etEmployees.error = FILL_FIELD_MESSAGE
                isValidate = false
            }
            !etEmployees.text.toString().isDigitsOnly() -> {
                etEmployees.error = NO_DIGITS_FIELD_MESSAGE
                isValidate = false
            }
            etCity.text.toString().isBlank() -> {
                etCity.error = FILL_FIELD_MESSAGE
                isValidate = false
            }
            etSquare.text.toString().isBlank() -> {
                etSquare.error = FILL_FIELD_MESSAGE
                isValidate = false
            }
            !etSquare.text.toString().isDigitsOnly() -> {
                etSquare.error = NO_DIGITS_FIELD_MESSAGE
                isValidate = false
            }
            etAttendance.text.toString().isBlank() -> {
                etAttendance.error = FILL_FIELD_MESSAGE
                isValidate = false
            }
            !etAttendance.text.toString().isDigitsOnly() -> {
                etAttendance.error = NO_DIGITS_FIELD_MESSAGE
                isValidate = false
            }
        }
        return isValidate
    }
}