package com.delta.eregist.ui.dashboard

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.delta.eregist.R
import com.delta.eregist.model.UserModel
import com.delta.eregist.ui.dashboard.kk.CreateKK
import com.delta.eregist.ui.dashboard.kk.UpdateKK
import com.delta.eregist.ui.dashboard.ktp.CreateKTP
import com.delta.eregist.ui.dashboard.ktp.UpdateKTP
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_dashboard.*

class DashboardFragment : Fragment() {
    private lateinit var db: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)

        db = Firebase.firestore
        val userAuth = FirebaseAuth.getInstance().currentUser
        val userRef = db.collection("users").document(userAuth!!.uid)
        var userProfile: DocumentSnapshot?

        userRef.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                userProfile = task.result
                val user = userProfile!!.toObject(UserModel::class.java)

                val decodedString: ByteArray = Base64.decode(user!!.photo, Base64.DEFAULT)
                val decodedByte =
                    BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
                imageView.setImageBitmap(decodedByte)
                textView8.text = "Halo, selamat datang " + user!!.fullName + "!"
            } else {
                Log.d("UserProfile", "Cached get failed: ", task.exception)
            }
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        card_view_buat_ktp.setOnClickListener {
            val intent = Intent(activity, CreateKTP::class.java)
            startActivityForResult(intent, 1)
        }
        card_view_update_ktp.setOnClickListener {
            val intent = Intent(activity, UpdateKTP::class.java)
            startActivityForResult(intent, 2)
        }
        card_view_buat_kk.setOnClickListener {
            val intent = Intent(activity, CreateKK::class.java)
            startActivityForResult(intent, 3)
        }
        card_view_update_kk.setOnClickListener {
            val intent = Intent(activity, UpdateKK::class.java)
            startActivityForResult(intent, 4)
        }

        helpIcon.setOnClickListener {
            val builder = AlertDialog.Builder(context)

            with(builder)
            {
                setTitle("Cara Penggunaan Aplikasi")
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            val snackbar = Snackbar.make(
                requireActivity().findViewById(R.id.nav_host_fragment), // Parent view
                "Request KTP Baru Berhasil!", // Message to show
                Snackbar.LENGTH_SHORT // How long to display the message.
            )
            snackbar.setAction("Tutup") { snackbar.dismiss() }
            snackbar.show()
        }
        if (requestCode == 2 && resultCode == Activity.RESULT_OK) {
            val snackbar = Snackbar.make(
                requireActivity().findViewById(R.id.nav_host_fragment), // Parent view
                "Request Update KTP Berhasil!", // Message to show
                Snackbar.LENGTH_SHORT // How long to display the message.
            )
            snackbar.setAction("Tutup") { snackbar.dismiss() }
            snackbar.show()
        }
        if (requestCode == 3 && resultCode == Activity.RESULT_OK) {
            val snackbar = Snackbar.make(
                requireActivity().findViewById(R.id.nav_host_fragment), // Parent view
                "Request Permintaan KK Baru Berhasil!", // Message to show
                Snackbar.LENGTH_SHORT // How long to display the message.
            )
            snackbar.setAction("Tutup") { snackbar.dismiss() }
            snackbar.show()
        }
        if (requestCode == 4 && resultCode == Activity.RESULT_OK) {
            val snackbar = Snackbar.make(
                requireActivity().findViewById(R.id.nav_host_fragment), // Parent view
                "Request Permintaan Update KK Berhasil!", // Message to show
                Snackbar.LENGTH_SHORT // How long to display the message.
            )
            snackbar.setAction("Tutup") { snackbar.dismiss() }
            snackbar.show()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}