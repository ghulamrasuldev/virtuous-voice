package com.example.virtuousvoice

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_child_signup.*

class ChildSignup : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth;
    private val TAG = "testTag"
    private lateinit var etName: EditText
    private lateinit var etUserName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etConfirmPassword: EditText
    private lateinit var etPID: EditText
    private lateinit var user: String
    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_child_signup)


        auth = Firebase.auth

        _btn_sign_up.setOnClickListener{
            val flag = createAccount()
            if (flag){
                val intent = Intent(this, TabbedActivity::class.java)
                intent.putExtra("user", user)
                startActivity(intent)
                finish()
            }
            else{
                Toast.makeText(baseContext, "Something went wrong.",
                    Toast.LENGTH_SHORT).show()
            }
        }

        //Taking user to sign in screen
        _sign_in_as_parent.setOnClickListener{
            val intent = Intent(this, SignIn::class.java)
            intent.putExtra("usertype", "parent")
            startActivity(intent)
            finish()
        }

        //Taking user to sign in screen
        _sign_in_as_individual.setOnClickListener{
            val intent = Intent(this, SignIn::class.java)
            intent.putExtra("usertype", "individual")
            startActivity(intent)
            finish()
        }

        //Taking user to sign in screen
        _sign_in_as_child.setOnClickListener{
            val intent = Intent(this, SignIn::class.java)
            intent.putExtra("usertype", "child")
            startActivity(intent)
            finish()
        }
    }

    fun createAccount(): Boolean {
        var flag : Boolean = false
        etEmail = _sign_up_email
        etName = _sign_up_name
        etUserName = _sign_up_username
        etPassword = _sign_up_password
        etConfirmPassword = _sign_up_verify_password
        etPID = _sign_up_PID

        if (etEmail.text.toString().isNotEmpty() && etName.text.toString()
                .isNotEmpty() && etUserName.text.toString().isNotEmpty()
            && etPassword.text.toString().isNotEmpty() && etConfirmPassword.text.toString().isNotEmpty() && etPID.text.toString().isNotEmpty()
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
                            flag = true
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success")
                            val user = auth.currentUser
//                        updateUI(user)
                            saveToFirestore(
                                etEmail.text.toString().trim(),
                                etName.text.toString().trim(),
                                etUserName.text.toString().trim(),
                                etPID.text.toString().trim()
                            )
                        } else {
                            flag = false
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
        user = etUserName.text.toString()
        return flag

    }

    private fun saveToFirestore(email: String, name: String, userName: String, pid: String) {


        val user = hashMapOf(
            "name" to name,
            "userName" to userName,
            "email" to email,
            "pid" to pid,
            "userType" to 2
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