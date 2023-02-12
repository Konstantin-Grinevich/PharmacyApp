package com.example.appstudents4.pharmacyNet

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appstudents4.AppStudentIntentApplication
import com.example.appstudents4.MainActivity
import com.example.appstudents4.MyConstants.DEFAULT_UUID
import com.example.appstudents4.R
import com.example.appstudents4.api.PharmacyRestApiService
import com.example.appstudents4.data.PharmacyNet
import com.example.appstudents4.repository.PharmacyNetDBRepository
import java.util.*

class PharmacyNetListDBFragment : Fragment() {

    lateinit var pharmacyNetListDBViewModel: PharmacyNetListDBViewModel
    private lateinit var pharmacyNetListRecycleView: RecyclerView
    private lateinit var btnShowShortInfo : Button
    private var isUsed = true

    private var adapter: PharmacyNetListAdapter? = PharmacyNetListAdapter(emptyList())

    companion object {
        private var INSTANCE: PharmacyNetListDBFragment? = null

        fun getInstance(): PharmacyNetListDBFragment {
            if (INSTANCE == null) {
                INSTANCE = PharmacyNetListDBFragment()
            }
            return INSTANCE ?: throw IllegalStateException("Отображение списка не создано!")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layoutView = inflater.inflate(R.layout.list_of_students, container, false)
        pharmacyNetListRecycleView = layoutView.findViewById(R.id.rvList)
        pharmacyNetListRecycleView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        pharmacyNetListRecycleView.adapter = adapter
        (activity as? AppCompatActivity)?.supportActionBar?.title = "Аптеки"
        return layoutView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pharmacyNetListDBViewModel = ViewModelProvider(this).get(PharmacyNetListDBViewModel::class.java)
        pharmacyNetListDBViewModel.pharmacyNetListLiveData.observe(
            viewLifecycleOwner,
            Observer{ pharmacyNetList ->
                pharmacyNetList?.let{
                    if (isUsed) {
                        PharmacyRestApiService.get().fetchPharmacyNetList(pharmacyNetList)
                        isUsed = false
                    }
                    updateUI(pharmacyNetList)
                }
            }
        )
        loadPharmacyNetList()
    }

    private fun updateUI(pharmacyNetList: List<PharmacyNet>? = null) {
        if (pharmacyNetList == null) return
        adapter = PharmacyNetListAdapter(pharmacyNetList)
        pharmacyNetListRecycleView.adapter = PharmacyNetListAdapter(pharmacyNetList)
    }

    private inner class PharmacyNetListAdapter(private val items: List<PharmacyNet>) : RecyclerView.Adapter<PharmacyNetHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PharmacyNetHolder {
            val view = layoutInflater.inflate(R.layout.student_list_element, parent, false)
            btnShowShortInfo = view.findViewById(R.id.btnShowShortInfo)
            return PharmacyNetHolder(view)
        }

        override fun onBindViewHolder(holder: PharmacyNetHolder, position: Int) {
            holder.bind(items[position])
        }

        override fun getItemCount(): Int = items.size

    }

    private inner class PharmacyNetHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener, View.OnLongClickListener {
        private lateinit var pharmacyNet: PharmacyNet
        private val fioTextView : TextView = itemView.findViewById(R.id.number)
        private val addressTextView : TextView = itemView.findViewById(R.id.address)

        fun bind (pharmacyNet : PharmacyNet) {
            this.pharmacyNet = pharmacyNet
            fioTextView.text = "${pharmacyNet.number}"
            addressTextView.text = pharmacyNet.address
            btnShowShortInfo.setOnClickListener {
                AlertDialog.Builder(requireContext())
                    .setTitle("Краткая информация аптеки №${pharmacyNet.number}")
                    .setMessage("Адрес аптеки: ${pharmacyNet.address}\n\nВремя работы: ${pharmacyNet.openingHours}\n\nЛет со дня открытия: ${pharmacyNet.age}")
                    .setPositiveButton("Ок", null)
                    .setCancelable(true)
                    .create()
                    .show()
            }
        }

        init {
            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener(this)
        }
        override fun onClick(v: View?) {
            callbacks?.onPharmacyNetSelected(pharmacyNet.id)
        }

        override fun onLongClick(v: View?): Boolean {
            callbacks?.onPharmacyNetLongClicked(pharmacyNet)
            return true
        }
    }

    interface Callbacks {
        fun onPharmacyNetSelected(pharmacyNetId: UUID)
        fun onPharmacyNetLongClicked(pharmacyNet: PharmacyNet)
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

    fun loadPharmacyNetList() {
        pharmacyNetListDBViewModel.loadPharmacyNetList(UUID.fromString(
            PreferenceManager.getDefaultSharedPreferences(AppStudentIntentApplication.applicationContext())
                .getString("pharmacyId", DEFAULT_UUID)))
    }

}