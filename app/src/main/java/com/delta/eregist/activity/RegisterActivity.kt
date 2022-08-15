package com.delta.eregist.activity

//import com.google.firebase.database.DatabaseReference
//import com.google.firebase.database.ktx.database
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.delta.eregist.R
import com.delta.eregist.model.UserModel
import com.delta.eregist.util.isValidEmail
import com.delta.eregist.util.validate
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_auth_register.*

class RegisterActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
//    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth_register)

        auth = Firebase.auth
        db = Firebase.firestore

        val actionBar = supportActionBar!!
        actionBar.title = getString(R.string.register)
        //set back button
        actionBar.setDisplayHomeAsUpEnabled(true)

        btnRegister.setOnClickListener { actionRegister() }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    fun actionRegister() {
        registerEmail.validate("Valid email address required")
        { s -> s.isValidEmail() }
        registerPassword.validate("Password should be at least 6 characters") { s -> s.isNotEmpty() && s.length >= 6 }
        registerFullName.validate("Full Name must be filled") { s -> s.isNotEmpty() }
        registerPasswordCheck.validate("Password must be same") { s -> s.isNotEmpty() && s == registerPassword.text.toString() }

        if (registerEmail.error == null
            && registerFullName.error == null
            && registerPassword.error == null
            && registerPasswordCheck.error == null
        ) {
            auth.createUserWithEmailAndPassword(registerEmail.text.toString(), registerPassword.text.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Register success
                        Log.d(TAG, "createUserWithEmail:success")

                        val userFirebase = task.result?.user!!
                        val newUser = UserModel(
                            fullName = registerFullName.text.toString(),
                            email = userFirebase.email,
                            type = "user"
                        )
                        db.collection("users").document(userFirebase.uid).set(newUser)
                            .addOnSuccessListener {
                                Toast.makeText(
                                    baseContext,
                                    "Registration Success, please login with your credentials",
                                    Toast.LENGTH_LONG
                                ).show()
                                val intent = Intent(this, LoginActivity::class.java)
                                startActivity(intent)
                            }
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        Toast.makeText(baseContext, "Registration failed: " + task.exception?.message,
                            Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    companion object {
        private const val TAG = "RegistrationActivity"
    }
}