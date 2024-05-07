package com.example.eadevelops.pyqslu.activities

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.eadevelops.pyqslu.R
import com.example.eadevelops.pyqslu.databinding.ActivityEditProfileBinding
import com.example.eadevelops.pyqslu.models.User
import com.example.eadevelops.pyqslu.utils.USER_NODE
import com.example.eadevelops.pyqslu.utils.USER_PROFILE_FOLDER
import com.example.eadevelops.pyqslu.utils.uploadImage
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso

class EditProfileActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityEditProfileBinding.inflate(layoutInflater)
    }
    private lateinit var user : User
    private val launcher = registerForActivityResult(ActivityResultContracts.GetContent()){ uri->
        uri?.let {
            uploadImage(uri, USER_PROFILE_FOLDER){
                if(it!=null){
                    user.image = it
                    binding.userImage.setImageURI(uri)
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
        user = User()
        Firebase.firestore.collection(USER_NODE).document(Firebase.auth.currentUser!!.uid)
            .get().addOnSuccessListener {
                user = it.toObject<User>()!!
                binding.userName.editText?.setText(user.name)
                binding.userName.isEnabled = false
                binding.userEmail.editText?.setText(user.email)
                binding.userEmail.isEnabled = false

                binding.bio.editText?.setText(user.bio)
                binding.course.editText?.setText(user.course)
                binding.instaUrl.editText?.setText(user.instaUrl)

                Picasso.get().load(user.image).placeholder(R.drawable.profile_img).into(binding.userImage)
            }

        binding.update.setOnClickListener {

            val bio = binding.bio.editText?.text.toString()
            val course = binding.course.editText?.text.toString()
            val instaUrl = binding.instaUrl.editText?.text.toString()

            update(bio, course, instaUrl)

            Firebase.firestore.collection(USER_NODE).document(Firebase.auth.currentUser!!.uid)
                .set(user).addOnSuccessListener {
                    Toast.makeText(this, binding.bio.editText?.text.toString(), Toast.LENGTH_SHORT).show()
                }

            finish()
        }

        binding.changeDp.setOnClickListener{
            launcher.launch("image/*")
        }
    }

    //Code to update data in fire store database;
    private fun update(bio: String, course: String, instaUrl: String) {
        val userDetails = mapOf(
            "bio" to bio,
            "course" to course,
            "instaUrl" to instaUrl
        )

        FirebaseFirestore.getInstance().collection(USER_NODE)
            .whereEqualTo("email", user.email).get().addOnCompleteListener{
                if (it.isSuccessful && !it.result.isEmpty){
                    val documentSnapshot = it.result.documents[0]
                    val docID = documentSnapshot.id
                    FirebaseFirestore.getInstance().collection(USER_NODE).document(docID)
                        .update(userDetails).addOnFailureListener {
                            Toast.makeText(this, "Some error occurred", Toast.LENGTH_SHORT).show()
                        }
                }
            }
    }
}