package com.example.eadevelops.pyqslu.activities

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.eadevelops.pyqslu.R
import com.example.eadevelops.pyqslu.databinding.ActivityPdfviewerBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL

class PDFViewerActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityPdfviewerBinding.inflate(layoutInflater)
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

        val fileName = intent.extras?.getString("fileName")
        val downloadUrl = intent.extras?.getString("downloadUrl")

        lifecycleScope.launch(Dispatchers.IO) {
            val inputStream = URL(downloadUrl).openStream()
            withContext(Dispatchers.Main){
                binding.PDFView.fromStream(inputStream).onRender {pages->
                    if (pages >= 1){
                        binding.progressBar.visibility = View.GONE
                    }
                }.load()
            }
        }
    }
}