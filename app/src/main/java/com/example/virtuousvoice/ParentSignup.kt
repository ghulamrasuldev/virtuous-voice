package com.example.virtuousvoice

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_parent_signup.*
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlin.random.Random

class ParentSignup : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth;
    private val TAG = "testTag"
    private lateinit var etName: String
    private lateinit var etUserName: String
    private lateinit var etEmail: String
    private lateinit var etNumber: String
    private lateinit var etPassword: String
    private lateinit var etConfirmPassword: String
    private lateinit var etPID: String
    private lateinit var user: String

    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parent_signup)

        auth = Firebase.auth

        //Generate PID
        _btn_sign_up_generate_PID.setOnClickListener{
            _sign_up_PID.setText(generatePID())
        }

        //Signing up user
        _btn_sign_up.setOnClickListener{
            val flag = createAccount()
            val intent = Intent(this, TabbedActivity::class.java)
            intent.putExtra("usertype", "parent")
            startActivity(intent)
//            finish()
//            if (flag){
//
//            }
//            else{
//                Toast.makeText(baseContext, "Something went wrong.",
//                    Toast.LENGTH_SHORT).show()
//            }

        }

        //Taking user to sign in screen
        _sign_in_as_parent.setOnClickListener{
            val intent = Intent(this, SignIn::class.java)
            intent.putExtra("usertype","parent")
            startActivity(intent)
            finish()
        }

        //Taking user to sign in screen
        _sign_in_as_child.setOnClickListener{
            val intent = Intent(this, SignIn::class.java)
            intent.putExtra("usertype","child")
            startActivity(intent)
            finish()
        }
    }

    fun createAccount(): Boolean {
        var flag : Boolean = false
        etEmail = _sign_up_email.text.toString()
        etName = _sign_up_name.text.toString()
        etUserName = _sign_up_username.text.toString()
        etNumber = _sign_up_number.text.toString()
        etPassword = _sign_up_password.text.toString()
        etConfirmPassword = _sign_up_verify_password.text.toString()
        etPID = _sign_up_PID.text.toString()

        if (etEmail.isNotEmpty() && etName.isNotEmpty() && etUserName.isNotEmpty()
            && etNumber.isNotEmpty() && etPassword.isNotEmpty() && etConfirmPassword.isNotEmpty()
            && etPID.isNotEmpty()) {

            if (etPassword.isNotEmpty() && etPassword == etConfirmPassword) {
                auth.createUserWithEmailAndPassword(etEmail, etPassword).addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            flag = true
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success")
                            val user = auth.currentUser
                            saveToFirestore(etEmail.trim(), etName.trim(), etUserName.trim(), etNumber.trim(), etPID.trim())
                        } else {
                            flag = false
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.exception)
                            Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
                        }
                    }
            }

            else {
                Log.d(TAG, "createUserWithEmail:Password Doesn't match")
            }
        }
        else {
            Toast.makeText(this, "Some Fields are empty", Toast.LENGTH_SHORT).show()
        }
        user = etUserName
        return flag
    }

    private fun saveToFirestore(email: String, name: String, userName: String, number: String, pid: String) {


        val user = hashMapOf("name" to name, "userName" to userName, "number" to number, "email" to email, "pid" to pid, "userType" to 1)

        db.collection("users").add(user).addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }.addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
    }

    fun generatePID(): String{
        var pid: String
        pid = Random.nextInt(1000,9999).toString()
        return pid
    }
}