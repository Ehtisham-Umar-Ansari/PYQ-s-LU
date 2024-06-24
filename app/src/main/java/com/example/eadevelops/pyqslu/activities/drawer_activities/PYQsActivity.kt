package com.example.eadevelops.pyqslu.activities.drawer_activities

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.eadevelops.pyqslu.R
import com.example.eadevelops.pyqslu.activities.RetrievePYQ
import com.example.eadevelops.pyqslu.databinding.ActivityPyqsBinding

class PYQsActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityPyqsBinding.inflate(layoutInflater)
    }

    override fun onResume() {
        super.onResume()

        var objects = resources.getStringArray(R.array.Course)
        var arrayAdapter = ArrayAdapter(this, R.layout.dropdown_menu, objects)
        binding.course.setAdapter(arrayAdapter)

        objects = resources.getStringArray(R.array.Branch)
        arrayAdapter = ArrayAdapter(this, R.layout.dropdown_menu, objects)
        binding.branch.setAdapter(arrayAdapter)

        objects = resources.getStringArray(R.array.Type)
        arrayAdapter = ArrayAdapter(this, R.layout.dropdown_menu, objects)
        binding.type.setAdapter(arrayAdapter)
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

        binding.showAll.setOnClickListener {
            val intent = Intent(this, RetrievePYQ::class.java)
                .putExtra("from", "PYQ")
            startActivity(intent)
        }

        binding.get.setOnClickListener {
            if(binding.course.text.toString() != "" && binding.branch.text.toString() != ""
                && binding.type.text.toString() != "" && binding.sem.editText?.text.toString() != ""){
                val intent = Intent(this, RetrievePYQ::class.java)
                .putExtra("fileName", binding.course.text.toString()+binding.branch.text.toString()+
                        binding.type.text.toString()+binding.sem.editText?.text.toString())
                .putExtra("from", "PYQ")
                startActivity(intent)
            }else{
                Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
}