package com.example.eadevelops.pyqslu.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.eadevelops.pyqslu.R
import com.example.eadevelops.pyqslu.RegisterActivity
import com.example.eadevelops.pyqslu.databinding.ActivityLoginBinding
import com.example.eadevelops.pyqslu.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.signUp.setOnClickListener {
            if(FirebaseAuth.getInstance().currentUser != null){
                FirebaseAuth.getInstance().signOut()
            }
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }

        binding.login.setOnClickListener {
            if (binding.userEmail.editText?.text.toString() == "" ||
                binding.userPassword.editText?.text.toString() == ""
            ) {
                Toast.makeText(this, "All fields required",
                    Toast.LENGTH_SHORT).show()
            } else {
                val user = User(binding.userEmail.editText?.text.toString(),
                    binding.userPassword.editText?.text.toString())

                Firebase.auth.signInWithEmailAndPassword(user.email!!, user.password!!)
                    .addOnCompleteListener{
                        if(it.isSuccessful){
                            if(FirebaseAuth.getInstance().currentUser!!.isEmailVerified){
                                Toast.makeText(this@LoginActivity, "Login Successful",
                                    Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this, MainActivity::class.java))
                                finish()
                            }else{
                                Firebase.auth.signOut()
                                Toast.makeText(this, "Please Verify your email",
                                    Toast.LENGTH_SHORT).show()
                            }
                        }else{
                            Toast.makeText(this, it.exception?.localizedMessage,
                                Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
    }
}