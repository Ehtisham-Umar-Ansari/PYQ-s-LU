package com.example.eadevelops.pyqslu

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.eadevelops.pyqslu.activities.LoginActivity
import com.example.eadevelops.pyqslu.activities.MainActivity
import com.example.eadevelops.pyqslu.databinding.ActivityRegisterBinding
import com.example.eadevelops.pyqslu.models.User
import com.example.eadevelops.pyqslu.utils.USER_NODE
import com.example.eadevelops.pyqslu.utils.USER_PROFILE_FOLDER
import com.example.eadevelops.pyqslu.utils.uploadImage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var user: User
    private var launcher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            uploadImage(uri, USER_PROFILE_FOLDER) {
                user.image = it
                binding.profileImage.setImageURI(uri)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {

//        This code is for checking if the user is already logged in or not
        if (FirebaseAuth.getInstance().currentUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.login.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        user = User()

        binding.register.setOnClickListener {
            if (binding.userName.editText?.text.toString() == "" ||
                binding.userEmail.editText?.text.toString() == "" ||
                binding.userPassword.editText?.text.toString() == ""
            ) {
                Toast.makeText(
                    this,
                    "Please fill in all the fields to register",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                    binding.userEmail.editText?.text.toString(),
                    binding.userPassword.editText?.text.toString().trim()
                ).addOnCompleteListener { task ->

                    if (task.isSuccessful) {

                        Toast.makeText(
                            this,
                            "Please verify your email",
                            Toast.LENGTH_LONG
                        ).show()

                        FirebaseAuth.getInstance().currentUser!!.sendEmailVerification()
                            .addOnCompleteListener {

                                if (task.isSuccessful){
                                    binding.userName.editText?.setText("")
                                    binding.userEmail.editText?.setText("")
                                    binding.userPassword.editText?.setText("")
                                    registerUser()
                                } else {
                                    Toast.makeText(
                                        this,
                                        task.exception?.localizedMessage,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }

                            }
                    } else {
                        Toast.makeText(
                            this,
                            task.exception?.localizedMessage,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            binding.addImage.setOnClickListener {
                launcher.launch("image/*")
            }
        }
    }

    private fun registerUser(){
        user.name = binding.userName.editText?.text.toString()
        user.password = binding.userPassword.editText?.text.toString()
        user.email = binding.userEmail.editText?.text.toString()

        Firebase.firestore.collection(USER_NODE)
            .document(Firebase.auth.currentUser!!.uid)
            .set(user)
    }

}