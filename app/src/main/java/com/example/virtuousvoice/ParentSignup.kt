package com.example.virtuousvoice

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_parent_signup.*

class ParentSignup : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth;
    private val TAG = "testTag"
    private lateinit var etName: EditText
    private lateinit var etUserName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etNumber: EditText
    private lateinit var etPassword: EditText
    private lateinit var etConfirmPassword: EditText

    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parent_signup)



        auth = Firebase.auth
        //Taking user to sign in screen
        _sign_in_as_parent.setOnClickListener {
//            val intent = Intent(this, SignIn::class.java)
//            intent.putExtra("user","parent")
//            startActivity(intent)
//            finish()

            createAccount()


        }

        //Taking user to sign in screen
        _sign_in_as_child.setOnClickListener {
            val intent = Intent(this, SignIn::class.java)
            intent.putExtra("user", "child")
            startActivity(intent)
            finish()
        }

        //Taking user to sign in screen
        _sign_in_as_individual.setOnClickListener {
            val intent = Intent(this, SignIn::class.java)
            intent.putExtra("user", "individual")
            startActivity(intent)
            finish()
        }
    }

    private fun createAccount() {

        etEmail = findViewById(R.id._sign_up_email)
        etName = findViewById(R.id._sign_up_name)
        etUserName = findViewById(R.id._sign_up_username)
        etNumber = findViewById(R.id._sign_up_number)
        etPassword = findViewById(R.id._sign_up_password)
        etConfirmPassword = findViewById(R.id._sign_up_verify_password)

        if (etEmail.text.toString().isNotEmpty() && etName.text.toString()
                .isNotEmpty() && etUserName.text.toString().isNotEmpty()
            && etNumber.text.toString().isNotEmpty() && etPassword.text.toString()
                .isNotEmpty() && etConfirmPassword.text.toString().isNotEmpty()

        ) {

            if (etPassword.text.toString()
                    .isNotEmpty() && etPassword.text.toString() == etConfirmPassword.text.toString()
            ) {


                auth.createUserWithEmailAndPassword(
                    etEmail.text.toString(),
                    etPassword.text.toString()
                )
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success")
                            val user = auth.currentUser
//                        updateUI(user)
                            saveToFirestore(
                                etEmail.text.toString().trim(),
                                etName.text.toString().trim(),
                                etUserName.text.toString().trim(),
                                etNumber.text.toString().trim()
                            )
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        Toast.makeText(baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()
//                        updateUI(null)
                        }
                    }
            } else {
                Log.d(TAG, "createUserWithEmail:Password Doesn't match")

            }
        }
        else{
            Toast.makeText(this, "Some Fields are empty", Toast.LENGTH_SHORT).show()
        }
    }


    private fun saveToFirestore(email: String, name: String, userName: String, number: String) {


        val user = hashMapOf(
            "name" to name,
            "userName" to userName,
            "number" to number,
            "email" to email,
            "userType" to 1
        )

        db.collection("users")
            .add(user)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
    }
}