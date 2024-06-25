package com.example.eadevelops.pyqslu.fragments.bottom_nav

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eadevelops.pyqslu.adapters.PostItemAdapterOne
import com.example.eadevelops.pyqslu.databinding.FragmentNoticeBinding
import com.example.eadevelops.pyqslu.models.Post
import com.example.eadevelops.pyqslu.utils.NOTICE
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase

class NoticeFragment : Fragment() {

    private lateinit var binding : FragmentNoticeBinding
    private lateinit var adapter: PostItemAdapterOne
    private var noticeList = ArrayList<Post>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentNoticeBinding.inflate(inflater, container, false)

        adapter = PostItemAdapterOne(requireContext(), noticeList, requireActivity().supportFragmentManager)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        getNotice()

        binding.swipeRefresh.setOnRefreshListener {
            getNotice()
            binding.swipeRefresh.isRefreshing = false
        }

        return binding.root
    }

    private fun getNotice(){
        Firebase.firestore.collection(NOTICE).get().addOnSuccessListener {
            val tempList = ArrayList<Post>()
            noticeList.clear()

            for(i in it.documents){
                val notice = i.toObject<Post>()!!
                tempList.add(notice)
            }
            noticeList.addAll(tempList)
            noticeList.sortBy { it.time }
            noticeList.reverse()
            adapter.notifyDataSetChanged()
        }.addOnFailureListener {
            Toast.makeText(requireContext(), it.localizedMessage?.toString(), Toast.LENGTH_LONG).show()
        }
    }
}
