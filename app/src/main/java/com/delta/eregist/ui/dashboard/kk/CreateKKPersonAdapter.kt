package com.delta.eregist.ui.dashboard.kk

import android.app.DatePickerDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.delta.eregist.databinding.ActivityKkCreatePersonBinding
import com.delta.eregist.model.KKPerson
import java.text.SimpleDateFormat
import java.util.*

class CreateKKPersonAdapter(private val personList: ArrayList<KKPerson>) :
    RecyclerView.Adapter<CreateKKPersonAdapter.PersonViewHolder>() {

    inner class PersonViewHolder(private val binding: ActivityKkCreatePersonBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(person: KKPerson, position: Int) {
            binding.index = position
            binding.person = person
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonViewHolder =
        PersonViewHolder(
            ActivityKkCreatePersonBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: PersonViewHolder, position: Int) {
        holder.bind(personList[position], position)
    }

    override fun getItemCount(): Int = personList.size
}

@BindingAdapter("textViewDatePickerOnclick")
fun onDatePickerClick(tv: TextView, selected: String) {
    val ctx = tv.context
    tv.setOnClickListener {
        DatePickerDialog(ctx, { _, year, monthOfYear, dayOfMonth ->
            val format = SimpleDateFormat("yyyy-MM-dd")
            val c = Calendar.getInstance()
            c.set(year, monthOfYear, dayOfMonth)
            tv.text = format.format(c.time)
        }, 2000, 0, 1).show()
    }
}

@BindingAdapter("spinnerJenisKelaminOnClick")
fun onSpinnerJenisKelaminClick(spinner: Spinner, person: KKPerson) {
    spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            val selected = spinner.selectedItemPosition
            if (selected == 0) {
                person.jenis_kelamin = "L"
            } else if (selected == 1) {
                person.jenis_kelamin = "P"
            }
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {}
    }
}

@BindingAdapter("spinnerStatusPerkawinanOnClick")
fun onSpinnerStatusPerkawinanClick(spinner: Spinner, person: KKPerson) {
    spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            person.status_perkawinan = spinner.selectedItem.toString()
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {}
    }
}

@BindingAdapter("spinnerAgamaOnClick")
fun onSpinnerAgamaClick(spinner: Spinner, person: KKPerson) {
    spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            person.agama = spinner.selectedItem.toString()
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {}
    }
}