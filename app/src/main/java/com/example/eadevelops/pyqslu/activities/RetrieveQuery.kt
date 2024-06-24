package com.example.eadevelops.pyqslu.activities

import android.content.ClipData
import android.os.Bundle
import android.content.ClipboardManager
import android.content.Intent
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eadevelops.pyqslu.R
import com.example.eadevelops.pyqslu.adapters.MyPYQItemsAdapter
import com.example.eadevelops.pyqslu.databinding.ActivityRetrievePyqBinding
import com.example.eadevelops.pyqslu.models.PDF
import com.example.eadevelops.pyqslu.utils.PDFs
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class RetrievePYQ : AppCompatActivity(), MyPYQItemsAdapter.ClickedItem {

    private val binding by lazy {
        ActivityRetrievePyqBinding.inflate(layoutInflater)
    }

    private lateinit var adapter : MyPYQItemsAdapter
    private lateinit var dataBaseReference: DatabaseReference
    private lateinit var pdfList : ArrayList<PDF>
    private var eventListener: ValueEventListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        pdfList = ArrayList()
        adapter = MyPYQItemsAdapter(this, pdfList, this)
        binding.recyclerView.adapter = adapter
        val from = intent.getStringExtra("from").toString()

        dataBaseReference = FirebaseDatabase.getInstance().reference.child(PDFs).child(from)

        eventListener = dataBaseReference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                pdfList.clear()
                for(itemSnapShot in snapshot.children){
                    val pdfClass = itemSnapShot.getValue(PDF::class.java)
                    if(pdfClass != null){
                        pdfList.add(pdfClass)
                    }
                }
                adapter.notifyDataSetChanged()

                if (intent.hasExtra("fileName")) {
                    val fileName = intent.getStringExtra("fileName")!!
                    binding.searchView.setQuery(fileName, true)

                    //Copying to clipboard
                    val clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
                    val clip = ClipData.newPlainText("FileName", fileName)
                    clipboard.setPrimaryClip(clip)

                    Toast.makeText(this@RetrievePYQ, "Query Copied", Toast.LENGTH_SHORT).show()

                    searchList(fileName)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

        search()
    }

    private fun search(){
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(text: String): Boolean {
                searchList(text)
                return true
            }
        })
    }

    fun searchList(text: String){
        val searchList = ArrayList<PDF>()
        for (i in pdfList){
            if(i.fileName.contains(text, ignoreCase = true)){
                searchList.add(i)
            }
        }
        adapter.searchList(searchList)
    }

    override fun onClicked(item: PDF) {
        val intent = Intent(this, PDFViewerActivity::class.java)
        intent.putExtra("fileName", item.fileName)
        intent.putExtra("downloadUrl", item.downloadUrl)
        startActivity(intent)
    }
}
