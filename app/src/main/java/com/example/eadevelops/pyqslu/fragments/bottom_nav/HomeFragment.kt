package com.example.eadevelops.pyqslu.fragments.bottom_nav

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.eadevelops.pyqslu.adapters.PostItemAdapterOne
import com.example.eadevelops.pyqslu.databinding.FragmentHomeBinding
import com.example.eadevelops.pyqslu.models.Post
import com.example.eadevelops.pyqslu.utils.POST
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase

class HomeFragment : Fragment() {

    private lateinit var binding : FragmentHomeBinding
    private lateinit var adapter : PostItemAdapterOne
    private val postList = ArrayList<Post>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        adapter = PostItemAdapterOne(requireContext(), postList, requireActivity().supportFragmentManager)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        getPost()

        binding.swipeRefresh.setOnRefreshListener {
            getPost()
            binding.swipeRefresh.isRefreshing = false
        }

        return binding.root
    }

    private fun getPost(){
        Firebase.firestore.collection(POST).get().addOnSuccessListener {
            val tempList = ArrayList<Post>()
            postList.clear()

            for(i in it.documents){
                val post = i.toObject<Post>()!!
                tempList.add(post)
            }
            postList.addAll(tempList)
            postList.sortBy { it.time }
            postList.reverse()
            adapter.notifyDataSetChanged()
        }.addOnFailureListener {
            Toast.makeText(requireContext(), it.localizedMessage?.toString(), Toast.LENGTH_LONG).show()
        }
    }
}