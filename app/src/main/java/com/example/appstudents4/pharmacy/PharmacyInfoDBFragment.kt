package com.example.appstudents4.pharmacy

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.appstudents.data.Pharmacy
import com.example.appstudents4.MyConstants.FILL_FIELD_MESSAGE
import com.example.appstudents4.R
import com.example.appstudents4.api.PharmacyAPI
import com.example.appstudents4.api.PharmacyRestApiService
import com.example.appstudents4.pharmacy.PharmacyInfoDBViewModel
import java.util.*

private const val PHARMACY_NET_ID_TAG : String = "pharmacy-id"

class PharmacyInfoDBFragment : Fragment() {

    private var pharmacy: Pharmacy? = null
    private lateinit var pharmacyInfoDBViewModel: PharmacyInfoDBViewModel
    private lateinit var etName: EditText
    private lateinit var btnSave : Button
    private lateinit var btnCancel : Button

    companion object {
        fun newInstance(pharmacyId:UUID): PharmacyInfoDBFragment {
            val args = Bundle().apply {
                putString(PHARMACY_NET_ID_TAG, pharmacyId.toString())
            }

            return PharmacyInfoDBFragment().apply {
                arguments = args
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val pharmacyId:UUID = UUID.fromString(arguments?.getString(PHARMACY_NET_ID_TAG))
        pharmacyInfoDBViewModel = ViewModelProvider(this).get(PharmacyInfoDBViewModel::class.java)
        pharmacyInfoDBViewModel.loadPharmacy(pharmacyId)
        (activity as? AppCompatActivity)?.supportActionBar?.title = "Создание сети аптек"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.pharmacy_info, container, false)
        etName = view.findViewById(R.id.name)
        btnSave = view.findViewById(R.id.btnSave)
        btnSave.setOnClickListener{
            if (validateFields()) {
                if (pharmacy == null) {
                    pharmacy = Pharmacy()
                    updatePharmacy()
                    pharmacyInfoDBViewModel.newPharmacy(pharmacy!!)
                    PharmacyRestApiService.get().uploadPharmacy(pharmacy!!)
                } else {
                    updatePharmacy()
                    pharmacyInfoDBViewModel.savePharmacy(pharmacy!!)
                    PharmacyRestApiService.get().updatePharmacy(pharmacy!!)
                }
                callbacks?.showDBPharmacyList()
            }
        }
        btnCancel = view.findViewById(R.id.btnCancel)
        btnCancel.setOnClickListener {
            callbacks?.showDBPharmacyList()
        }
        return view
    }

    fun updatePharmacy() {
        pharmacy?.name = etName.text.toString()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pharmacyInfoDBViewModel.pharmacyLiveData.observe(viewLifecycleOwner) { pharmacy ->
            pharmacy?.let {
                this.pharmacy = pharmacy
                updateUI()
            }
        }
    }

    fun updateUI() {
        etName.setText(pharmacy?.name)
    }

    interface Callbacks {
        fun showDBPharmacyList()
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

        if (etName.text.toString().isBlank()) {
            etName.error = FILL_FIELD_MESSAGE
            isValidate = false
        }
        return isValidate
    }
}