package com.example.eadevelops.pyqslu.activities.drawer_activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eadevelops.pyqslu.R
import com.example.eadevelops.pyqslu.activities.EditProfileActivity
import com.example.eadevelops.pyqslu.activities.TunnelActivity
import com.example.eadevelops.pyqslu.adapters.PostItemsAdapterTwo
import com.example.eadevelops.pyqslu.databinding.ActivityMyProfileBinding
import com.example.eadevelops.pyqslu.models.Post
import com.example.eadevelops.pyqslu.models.User
import com.example.eadevelops.pyqslu.utils.USER_NODE
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso

class MyProfileActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMyProfileBinding.inflate(layoutInflater)
    }
    private lateinit var adapter : PostItemsAdapterTwo
    private var postList = ArrayList<Post>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        adapter = PostItemsAdapterTwo(this, postList)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        binding.editProfile.setOnClickListener {
            startActivity(Intent(this, EditProfileActivity::class.java))
        }

        binding.postOption.setOnClickListener {
            openActivity()
        }

        binding.postOption1.setOnClickListener {
            openActivity()
        }

        binding.postOption2.setOnClickListener {
            openActivity()
        }

        Firebase.firestore.collection(USER_NODE).document(FirebaseAuth.getInstance().currentUser!!.uid)
            .get().addOnSuccessListener {

                val user = it.toObject<User>()
                binding.name.text = user?.name.toString()
                binding.userEmail.text = user?.email.toString()

                if (user?.bio != null){
                    binding.bio.text = user.bio.toString()
                }

                if (user?.course == "" || user?.course == null){
                    binding.course.text = getString(R.string.course)
                }else{
                    binding.course.text = user.course.toString()
                }

                if (user?.instaName == "" || user?.instaName == null){
                    binding.instaName.text = getString(R.string.insta)
                }else{
                    binding.instaName.text = user.instaName.toString()
                }

                Picasso.get().load(user?.image).placeholder(R.drawable.profile_img)
                    .into(binding.userImage)
            }

        Firebase.firestore.collection(Firebase.auth.currentUser!!.uid)
            .get().addOnSuccessListener {
                if (!it.isEmpty){
                    binding.yourPosts.visibility = View.VISIBLE
                }

            val tempList = arrayListOf<Post>()
            for(i in it.documents){
                val post : Post = i.toObject<Post>()!!
                tempList.add(post)
            }
                postList.addAll(tempList)
                postList.reverse()
            adapter.notifyDataSetChanged()
        }

    }

    private fun openActivity(){
        startActivity(Intent(this, TunnelActivity::class.java))
    }
}