package com.delta.eregist.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import com.delta.eregist.R
import com.delta.eregist.util.isValidEmail
import com.delta.eregist.util.validate
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_auth_login.*

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth_login)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        auth = Firebase.auth
        if (auth.currentUser != null) {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        tvRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
        btnLogin.setOnClickListener { actionLogin() }
    }

    fun actionLogin() {
        loginEmailAddress.validate("Valid email address required")
            { s -> s.isValidEmail() }
        loginPassword.validate("Password should be at least 6 characters") { s -> s.isNotEmpty() && s.length >= 6 }

        if (loginEmailAddress.error == null && loginPassword.error == null) {
            doLoginWith(loginEmailAddress.text.toString(), loginPassword.text.toString())
        }
    }

    fun doLoginWith(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success \n"
                            + "Email: " + auth.currentUser?.email + "\n"
                            + "DisplayName: " + auth.currentUser?.displayName + "\n"
                            + "UID: " + auth.currentUser?.uid + "\n")
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Login Failed: " + task.exception?.message,
                        Toast.LENGTH_LONG).show()
                }
            }
    }

    companion object {
        private const val TAG = "LoginActivity"
    }
}