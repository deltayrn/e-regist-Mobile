package com.delta.eregist.ui.dashboard.kk

import android.app.Activity
import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.delta.eregist.R
import com.delta.eregist.databinding.ActivityKkUpdateBinding
import com.delta.eregist.model.KKPerson
import com.delta.eregist.model.UserModel
import com.delta.eregist.model.requestKKUpdate
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_kk_update.*

class UpdateKK : AppCompatActivity() {
    lateinit var userRef: DocumentReference
    lateinit var userAuth: FirebaseUser
    lateinit var user: UserModel
    lateinit var db: FirebaseFirestore

    private val personList: ArrayList<KKPerson> = ArrayList()
    private lateinit var personAdapter: CreateKKPersonAdapter
    private lateinit var binding: ActivityKkUpdateBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val actionBar = supportActionBar!!
        actionBar.title = "Update Kartu Keluarga"
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

        binding = DataBindingUtil.setContentView(this, R.layout.activity_kk_update)

        setAdapter()
        setListener()
        addNewPerson()
    }

    fun setAdapter() {
        personAdapter = CreateKKPersonAdapter(personList)
        binding.rvPersonList.apply {
            layoutManager = LinearLayoutManager(this@UpdateKK)
            adapter = personAdapter
        }
    }

    private fun setListener() {
        binding.btnAddPerson.setOnClickListener { addNewPerson() }
        binding.btnSimpanKKUpdate.setOnClickListener { submitKKUpdate() }
        helpIcon.setOnClickListener {
            val builder = AlertDialog.Builder(this)

            with(builder)
            {
                setTitle("Cara Penggunaan Update KK")
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

    fun addNewPerson() {
        personList.add(KKPerson())
        personAdapter.notifyDataSetChanged()
    }

    fun submitKKUpdate() {
        val dataSubmit = requestKKUpdate(
            user = user,
            no_kk = etNomorKK.text.toString(),
            admin_approval = 0,
            type = "kk_update",
            status = 0,
            createdAt = Timestamp.now(),
            data_kk = personList
        )
        dataSubmit.generateCode()

        db.collection("requests")
            .add(dataSubmit)
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
        private const val TAG = "UpdateKKActivity"
    }
}