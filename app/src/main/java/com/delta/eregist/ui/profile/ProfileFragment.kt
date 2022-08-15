package com.delta.eregist.ui.profile

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.delta.eregist.R
import com.delta.eregist.model.UserModel
import com.delta.eregist.ui.profile.update.UpdateProfile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_profile.*


class ProfileFragment : Fragment() {
    private lateinit var db: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        db = Firebase.firestore

        val root = inflater.inflate(R.layout.fragment_profile, container, false)

        val userAuth = FirebaseAuth.getInstance().currentUser
        val userRef = db.collection("users").document(userAuth!!.uid)
        var userProfile: DocumentSnapshot?

        userRef.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                userProfile = task.result
                val user = userProfile!!.toObject(UserModel::class.java)

                val tvFullName = root.findViewById<TextView>(R.id.tvFullName)
                tvFullName.text = user!!.fullName
                val tvFullNameBottom = root.findViewById<TextView>(R.id.tvFullNameBottom)
                tvFullNameBottom.text = user!!.fullName
                val tvEmail = root.findViewById<TextView>(R.id.tvEmail)
                tvEmail.text = userAuth.email

                val decodedString: ByteArray = Base64.decode(user.photo, Base64.DEFAULT)
                val decodedByte =
                    BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
                val imageViewProfile = root.findViewById<ImageView>(R.id.imageViewProfile)
                imageViewProfile.setImageBitmap(decodedByte)
            } else {
                Log.d("UserProfile", "Cached get failed: ", task.exception)
            }
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        btnUpdateProfile.setOnClickListener {
            val intent = Intent(activity, UpdateProfile::class.java)
            startActivityForResult(intent, 10001);
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if ((requestCode == 10001) && (resultCode == Activity.RESULT_OK)) {
            parentFragmentManager.beginTransaction().detach(this).commitNow()
            parentFragmentManager.beginTransaction().attach(this).commitNow()
        }
    }
}