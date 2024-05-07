package com.example.eadevelops.pyqslu.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.eadevelops.pyqslu.R
import com.example.eadevelops.pyqslu.databinding.NavHeaderBinding
import com.example.eadevelops.pyqslu.models.User
import com.example.eadevelops.pyqslu.utils.USER_NODE
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso

class NavHeader : AppCompatActivity() {

    private val binding by lazy {
        NavHeaderBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        Firebase.firestore.collection(USER_NODE).document(FirebaseAuth.getInstance().currentUser!!.uid)
            .get().addOnSuccessListener {
                val user = it.toObject<User>()
                Picasso.get().load(user?.image).placeholder(R.drawable.profile_img)
                    .into(binding.viewProfileImg)
                binding.viewProfile.text = user?.email.toString()
            }
    }

}