package com.example.eadevelops.pyqslu.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.eadevelops.pyqslu.R
import com.example.eadevelops.pyqslu.databinding.ActivityGeneralPostBinding
import com.example.eadevelops.pyqslu.models.Post
import com.example.eadevelops.pyqslu.models.User
import com.example.eadevelops.pyqslu.utils.POST
import com.example.eadevelops.pyqslu.utils.POST_FOLDER
import com.example.eadevelops.pyqslu.utils.USER_NODE
import com.example.eadevelops.pyqslu.utils.uploadImage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase

class GeneralPostActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityGeneralPostBinding.inflate(layoutInflater)
    }
    private var imageUrl : String? = null
    private val launcher = registerForActivityResult(ActivityResultContracts.GetContent()){ uri->
        uri?.let {
            uploadImage(uri, POST_FOLDER){url->
                if(url!=null){
                    binding.postImageIcon.setImageURI(uri)
                    imageUrl = url
                }
            }
        }
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

        binding.postImageIcon.setOnClickListener {
            launcher.launch("image/*")
        }

        binding.cancelButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        val isNotice = false

        binding.postButton.setOnClickListener {
            Firebase.firestore.collection(USER_NODE).document().get().addOnSuccessListener {
                var user = it.toObject<User>()

                val post : Post = if (imageUrl == null){
                    Post(binding.caption.editText?.text.toString(), FirebaseAuth.getInstance().currentUser
                    !!.uid, System.currentTimeMillis().toString(), "post")
                }else{
                    Post(imageUrl!!, binding.caption.editText?.text.toString(), FirebaseAuth.getInstance().currentUser
                    !!.uid, System.currentTimeMillis().toString(), "post")
                }

                // Setting code on firebase database
                Firebase.firestore.collection(POST).document().set(post).addOnSuccessListener {
                    startActivity(Intent(this@GeneralPostActivity, MainActivity::class.java))
                    finish()
                }
                Firebase.firestore.collection(Firebase.auth.currentUser!!.uid).document().set(post)
            }
        }

    }
}