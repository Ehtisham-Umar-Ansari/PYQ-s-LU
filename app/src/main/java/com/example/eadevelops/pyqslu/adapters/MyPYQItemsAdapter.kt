package com.example.eadevelops.pyqslu.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.eadevelops.pyqslu.databinding.PyqItemBinding
import com.example.eadevelops.pyqslu.models.PDF

class MyPYQItemsAdapter(private val context : Context, private var pdfList : List<PDF>, private val listener : ClickedItem)
    : RecyclerView.Adapter<MyPYQItemsAdapter.PYQViewHolder>() {

    inner class PYQViewHolder(val binding : PyqItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PYQViewHolder {
        val binding = PyqItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return PYQViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return pdfList.size
    }

    override fun onBindViewHolder(holder: PYQViewHolder, position: Int) {
        holder.binding.pdfTitle.text = pdfList[position].fileName
        holder.binding.pdfFile.setOnClickListener {
            listener.onClicked(pdfList[position])
        }
    }

    fun searchList(searchList : List<PDF>){
        pdfList = searchList
        notifyDataSetChanged()
    }

    interface ClickedItem{
        fun onClicked(item: PDF)
    }

}