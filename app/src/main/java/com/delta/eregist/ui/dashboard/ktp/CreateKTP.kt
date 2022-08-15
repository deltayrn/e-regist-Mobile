package com.delta.eregist.ui.dashboard.ktp

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.delta.eregist.R
import com.delta.eregist.model.KTPCreate
import com.delta.eregist.model.RequestKTPNew
import com.delta.eregist.model.UserModel
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_ktp_create.*
import java.text.SimpleDateFormat
import java.util.*

class CreateKTP : AppCompatActivity() {
    lateinit var spinner: Spinner
    lateinit var textView: TextView
    lateinit var userRef: DocumentReference
    lateinit var userAuth: FirebaseUser
    lateinit var user: UserModel
    lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ktp_create)

        val actionBar = supportActionBar!!
        actionBar.title = getString(R.string.dashboard_buat_ktp)
        //set back button
        actionBar.setDisplayHomeAsUpEnabled(true)

        db = Firebase.firestore
        userAuth = FirebaseAuth.getInstance().currentUser!!
        userRef = db.collection("users").document(userAuth!!.uid)

        userRef.get(Source.CACHE).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val userProfile = task.result
                user = userProfile!!.toObject(UserModel::class.java)!!
                user.id = userAuth!!.uid
                user.email = userAuth!!.email
                user.photo = ""
            }
        }

        et_datepicker.setOnClickListener {
            val dpd = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    val format = SimpleDateFormat("yyyy-MM-dd")
                    val c = Calendar.getInstance()
                    c.set(year, monthOfYear, dayOfMonth)
                    et_datepicker.setText(format.format(c.time))
                },
                2000,
                0,
                1
            )
            dpd.show()

        }

        btnSimpanKTPnew.setOnClickListener {
            submitKTPNew()
        }

        helpIcon.setOnClickListener {
            val builder = AlertDialog.Builder(this)

            with(builder)
            {
                setTitle("Cara Penggunaan Pembuatan KTP Baru")
                setMessage(
                    "" +
                            "We have a message \n" +
                            "Hello World \n" +
                            "1. Point satu \n" +
                            "2. Point dua \n" +
                            "\u2022 Bullet point \n" +
                            "\u2022 Another bullet point" +
                            ""
                )
                setPositiveButton("OK") { dialog, id ->
                    // User clicked OK button
                }
                show()
            }
        }
    }

    fun getJenisKelamin(): String {
        val selected = jenisKelaminKTPnew.selectedItemPosition
        if (selected == 0) {
            return "L"
        } else if (selected == 1) {
            return "P"
        }
        return "?"
    }

    fun submitKTPNew() {
        val newKTP = KTPCreate(
            nama = etNama.text.toString(),
            tempat_lahir = et_tempatlahir.text.toString(),
            tanggal_lahir = et_datepicker.text.toString(),
            jenis_kelamin = getJenisKelamin(),
            agama = spinnerAgama.selectedItem.toString(),
            pekerjaan = etPekerjaan.text.toString(),
            status_perkawinan = spinnerStatusPerkawinan.selectedItem.toString()
        )
        val newDocument = RequestKTPNew(
            data_ktp = newKTP,
            user = user,
            admin_approval = 0,
            type = "ktp_new",
            status = 0,
            createdAt = Timestamp.now()
        )
        newDocument.generateCode()

        db.collection("requests")
            .add(newDocument)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot written with ID: ${documentReference.id}")
                setResult(Activity.RESULT_OK)
                finish()
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
                setResult(Activity.RESULT_CANCELED)
                finish()
            }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    companion object {
        private const val TAG = "CreateKTPActivity"
    }
}