package com.example.eadevelops.pyqslu.activities

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.eadevelops.pyqslu.R
import com.example.eadevelops.pyqslu.databinding.ActivityNoticeBinding
import com.example.eadevelops.pyqslu.models.Post
import com.example.eadevelops.pyqslu.models.User
import com.example.eadevelops.pyqslu.utils.NOTICE
import com.example.eadevelops.pyqslu.utils.NOTICE_FOLDER
import com.example.eadevelops.pyqslu.utils.POST_FOLDER
import com.example.eadevelops.pyqslu.utils.USER_NODE
import com.example.eadevelops.pyqslu.utils.uploadImage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase

class NoticeActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityNoticeBinding.inflate(layoutInflater)
    }
    private var imageUrl : String? = null
    private val launcher = registerForActivityResult(ActivityResultContracts.GetContent()){ uri->
        uri?.let {
            uploadImage(uri, NOTICE_FOLDER){ url->
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

        binding.postButton.setOnClickListener {
            Firebase.firestore.collection(USER_NODE).document().get().addOnSuccessListener {
                var user = it.toObject<User>()

                //Passing the value notice_tag here as passed in the Post Model

                val notice : Post = if (imageUrl == null){
                    Post(binding.caption.editText?.text.toString(), FirebaseAuth.getInstance().currentUser
                    !!.uid, System.currentTimeMillis().toString(), binding.noticeTag.text.toString())
                }else{
                    Post(imageUrl!!, binding.caption.editText?.text.toString(), FirebaseAuth.getInstance().currentUser
                    !!.uid, System.currentTimeMillis().toString(), binding.noticeTag.text.toString())
                }
                Firebase.firestore.collection(NOTICE).document().set(notice).addOnSuccessListener {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
                Firebase.firestore.collection(Firebase.auth.currentUser!!.uid).document().set(notice)
            }
        }
    }
}