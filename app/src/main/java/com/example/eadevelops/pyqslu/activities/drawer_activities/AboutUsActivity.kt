package com.example.eadevelops.pyqslu.activities.drawer_activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.eadevelops.pyqslu.R
import com.example.eadevelops.pyqslu.databinding.ActivityAboutUsBinding

class AboutUsActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityAboutUsBinding.inflate(layoutInflater)
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

        binding.instagramUrl.setOnClickListener {
            openWeb("https://www.instagram.com/ehtishamansari05")
        }
        binding.linkedinUrl.setOnClickListener {
            openWeb("https://www.linkedin.com/in/ehtisham-ansari")
        }

    }

    private fun openWeb(url : String){
        val openWebsiteIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(openWebsiteIntent)
    }
}