package com.example.eadevelops.pyqslu.fragments.bottom_nav

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.eadevelops.pyqslu.activities.GeneralPostActivity
import com.example.eadevelops.pyqslu.activities.NoticeActivity
import com.example.eadevelops.pyqslu.activities.UploadNotesActivity
import com.example.eadevelops.pyqslu.activities.UploadPYQActivity
import com.example.eadevelops.pyqslu.databinding.FragmentAddBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AddFragment : BottomSheetDialogFragment() {

    private lateinit var binding : FragmentAddBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentAddBinding.inflate(inflater, container, false)

        binding.uploadPost.setOnClickListener{
            startActivity(Intent(requireContext(), GeneralPostActivity::class.java))
        }

        binding.uploadNotice.setOnClickListener{
            startActivity(Intent(requireContext(), NoticeActivity::class.java))
        }

        binding.uploadNotes.setOnClickListener{
            startActivity(Intent(requireContext(), UploadNotesActivity::class.java))
        }

        binding.uploadPyq.setOnClickListener{
            startActivity(Intent(requireContext(), UploadPYQActivity::class.java))
        }

        return binding.root
    }
}