package com.example.eadevelops.pyqslu.activities

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.documentfile.provider.DocumentFile
import com.example.eadevelops.pyqslu.R
import com.example.eadevelops.pyqslu.databinding.ActivityUploadPyqactivityBinding
import com.example.eadevelops.pyqslu.models.PDF
import com.example.eadevelops.pyqslu.utils.PDF_FOLDER
import com.example.eadevelops.pyqslu.utils.PDFs
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage

class UploadPYQActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityUploadPyqactivityBinding.inflate(layoutInflater)
    }
    private var pdfFileUri : Uri? = null
    private val launcher = registerForActivityResult(ActivityResultContracts.GetContent()){uri ->
        pdfFileUri = uri
        val fileName = uri?.let { DocumentFile.fromSingleUri(this, it) }?.name
        if(fileName != null){
            binding.fileName.text = fileName.toString()
            binding.fileName.setTextColor(resources.getColor(R.color.green))
        }
        binding.progressBar.visibility = View.GONE
    }
    private lateinit var storageReference : StorageReference
    private lateinit var dataBaseReference: DatabaseReference

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

        storageReference = FirebaseStorage.getInstance().reference.child(PDF_FOLDER)
        dataBaseReference = FirebaseDatabase.getInstance().reference.child(PDFs)

        binding.pdfDataUpload.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            pdfFileUri = null
            launcher.launch("application/pdf")
            if(pdfFileUri == null){
                binding.fileName.text = "No File selected"
                binding.fileName.setTextColor(resources.getColor(R.color.red))
            }
        }

        binding.upload.setOnClickListener {
            if(pdfFileUri != null){
                binding.progressBar.visibility = View.VISIBLE
                uploadPdf()
            }else
                Toast.makeText(this, "Please Select a pdf file", Toast.LENGTH_SHORT).show()
        }
    }
// This is the code to upload pdf to firebase.
    private fun uploadPdf(){

        if(binding.course.text.toString() == "" || binding.branch.text.toString() == "" || binding.type.text.toString() == "" ||
            binding.sem.editText?.text.toString() == "" || binding.subjectName.editText?.text.toString() == "" ||
            binding.year.editText?.text.toString() == ""){

            Toast.makeText(this, "All fields required", Toast.LENGTH_SHORT).show()

        }else{

            val fileName = binding.course.text.toString()+binding.branch.text.toString()+binding.type.text.toString()+binding
                .sem.editText?.text.toString()+binding.subjectName.editText?.text.toString()+binding.year.editText?.text.toString()

            val storage = Firebase.storage
            val fileReference = storage.reference.child("$PDF_FOLDER/$fileName")
            fileReference.metadata.addOnSuccessListener {
                Toast.makeText(this, "PDF already Exist", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                uploadDoc(fileName)
            }
        }
}

    private fun uploadDoc(fileName: String) {
        val mStorageRef = storageReference.child(fileName)

        pdfFileUri?.let {uri->
            mStorageRef.putFile(uri).addOnSuccessListener {
                mStorageRef.downloadUrl.addOnSuccessListener {downloadedURI->

                    val PDFFile = PDF(fileName, downloadedURI.toString())

                    dataBaseReference.child(fileName).setValue(PDFFile).addOnCompleteListener {task->
                        if(task.isSuccessful){
                            Toast.makeText(this, "Uploaded Successfully", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                    }.addOnFailureListener {
                        Toast.makeText(this, it.message.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            }
            binding.progressBar.visibility = View.GONE
        }
    }
}