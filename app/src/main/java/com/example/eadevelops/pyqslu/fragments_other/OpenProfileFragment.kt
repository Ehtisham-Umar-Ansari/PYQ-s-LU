package com.example.eadevelops.pyqslu.fragments_other

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eadevelops.pyqslu.adapters.PostItemsAdapterTwo
import com.example.eadevelops.pyqslu.databinding.FragmentOpenProfileBinding
import com.example.eadevelops.pyqslu.models.Post
import com.example.eadevelops.pyqslu.models.User
import com.example.eadevelops.pyqslu.utils.USER_NODE
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase


class OpenProfileFragment : Fragment() {

    private lateinit var binding: FragmentOpenProfileBinding
    private lateinit var adapter: PostItemsAdapterTwo
    private val itemList = ArrayList<Post>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        // Inflate the layout for this fragment
        binding = FragmentOpenProfileBinding.inflate(inflater, container, false)
        val uid = arguments?.getString("uid")!!
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = PostItemsAdapterTwo(requireContext(), itemList)
        binding.recyclerView.adapter = adapter

        Firebase.firestore.collection(USER_NODE).document(uid).get().addOnSuccessListener {
            val user = it.toObject<User>()!!
            binding.name.text = user.name
            if (user.bio != null){
                binding.bio.text = user.bio
            }else{
                binding.bio.visibility = View.GONE
            }
            binding.userEmail.text = user.email
            if(user.course!=null){
                binding.course.text = user.course
            }else{
                binding.course.visibility = View.GONE
                binding.courseIcon.visibility = View.GONE
            }
            if(user.instaName!= null){
                binding.instaName.text = user.instaName
            }else{
                binding.instaName.visibility = View.GONE
                binding.instaIcon.visibility = View.GONE
            }
        }

        Firebase.firestore.collection(uid).get().addOnSuccessListener {
            val tempList = arrayListOf<Post>()
            for(i in it.documents){
                val post : Post = i.toObject<Post>()!!
                tempList.add(post)
            }
            itemList.addAll(tempList)
            itemList.sortBy { it.time }
            itemList.reverse()
            adapter.notifyDataSetChanged()
        }

        return binding.root
    }
}