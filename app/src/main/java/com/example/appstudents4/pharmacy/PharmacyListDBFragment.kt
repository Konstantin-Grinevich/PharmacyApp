package com.example.appstudents4.pharmacy

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appstudents.data.Pharmacy
import com.example.appstudents4.AppStudentIntentApplication
import com.example.appstudents4.R
import com.example.appstudents4.api.PharmacyRestApiService
import com.example.appstudents4.data.PharmacyNet
import com.example.appstudents4.repository.PharmacyNetDBRepository
import java.util.*

class PharmacyListDBFragment : Fragment() {

    lateinit var pharmacyListDBViewModel: PharmacyListDBViewModel
    private lateinit var pharmacyListRecycleView: RecyclerView
    private var isUsed = true

    private var adapter: PharmacyListAdapter? = PharmacyListAdapter(emptyList())

    companion object {
        private var INSTANCE: PharmacyListDBFragment? = null

        fun getInstance(): PharmacyListDBFragment {
            if (INSTANCE == null) {
                INSTANCE = PharmacyListDBFragment()
            }
            return INSTANCE ?: throw IllegalStateException("Отображение списка не создано!")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layoutView = inflater.inflate(R.layout.list_of_pharmacies, container, false)
        pharmacyListRecycleView = layoutView.findViewById(R.id.rvList)
        pharmacyListRecycleView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        pharmacyListRecycleView.adapter = adapter
        (activity as? AppCompatActivity)?.supportActionBar?.title = "Сети аптек"
        return layoutView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pharmacyListDBViewModel = ViewModelProvider(this).get(PharmacyListDBViewModel::class.java)
        pharmacyListDBViewModel.pharmacyListLiveData.observe(
            viewLifecycleOwner,
            Observer{ pharmacyList ->
                pharmacyList?.let{
                    if (isUsed) {
                        PharmacyRestApiService.get().fetchPharmacyList(pharmacyList)
                        isUsed = false
                    }
                    updateUI(pharmacyList)
                }
            }
        )

    }

    private fun updateUI(pharmacyList: List<Pharmacy>? = null) {
        if (pharmacyList == null) return
        adapter = PharmacyListAdapter(pharmacyList)
        pharmacyListRecycleView.adapter = PharmacyListAdapter(pharmacyList)
    }

    private inner class PharmacyListAdapter(private val items: List<Pharmacy>) : RecyclerView.Adapter<PharmacyHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PharmacyHolder {
            val view = layoutInflater.inflate(R.layout.pharmacy_list_element, parent, false)
            return PharmacyHolder(view)
        }

        override fun onBindViewHolder(holder: PharmacyHolder, position: Int) {
            holder.bind(items[position])
        }

        override fun getItemCount(): Int = items.size

    }

    private inner class PharmacyHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener, View.OnLongClickListener {
        private lateinit var pharmacy: Pharmacy
        private val nameTextView : TextView = itemView.findViewById(R.id.name)

        fun bind (pharmacy : Pharmacy) {
            this.pharmacy = pharmacy
            nameTextView.text = "${pharmacy.name}"
        }

        init {
            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener(this)
        }
        override fun onClick(v: View?) {
            val preference=
                PreferenceManager.getDefaultSharedPreferences(AppStudentIntentApplication.applicationContext())
            preference.edit().apply() {
                putString("pharmacyId", pharmacy.id.toString())
                apply()
            }
            callbacks?.onPharmacySelected(pharmacy.id)
        }

        override fun onLongClick(v: View?): Boolean {
            callbacks?.onPharmacyLongClicked(pharmacy)
            return true
        }
    }

    interface Callbacks {
        fun onPharmacySelected(pharmacyId: UUID)
        fun onPharmacyLongClicked(pharmacy: Pharmacy)
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

}