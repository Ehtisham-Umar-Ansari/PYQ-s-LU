package com.example.eadevelops.pyqslu.adapters

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eadevelops.pyqslu.R
import com.example.eadevelops.pyqslu.databinding.NoticeItemBinding
import com.example.eadevelops.pyqslu.fragments_other.OpenProfileFragment
import com.example.eadevelops.pyqslu.models.Post
import com.example.eadevelops.pyqslu.models.User
import com.example.eadevelops.pyqslu.utils.USER_NODE
import com.github.marlonlom.utilities.timeago.TimeAgo
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.firestore.toObject
import com.squareup.picasso.Picasso

class PostItemAdapterOne(var context: Context, private var postList: ArrayList<Post>, private val fragmentManager: FragmentManager)
    : RecyclerView.Adapter<PostItemAdapterOne.NoticeViewHolder>() {

    inner class NoticeViewHolder(val binding: NoticeItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoticeViewHolder {
        val binding = NoticeItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return NoticeViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return  postList.size
    }

    override fun onBindViewHolder(holder: NoticeViewHolder, position: Int) {
        Firebase.firestore.collection(USER_NODE).document(postList[position].uid).get()
            .addOnSuccessListener {
                val user = it.toObject<User>()

                Picasso.get().load(user!!.image).placeholder(R.drawable.profile_img).into(holder.binding.userImage)

                holder.binding.name.text = user.name

                // This piece of code
                if(postList[position].notice == "NOTICE"){
                    holder.binding.noticeTag.visibility = View.VISIBLE
                }

                val text = TimeAgo.using(postList[position].time.toLong())
                holder.binding.time.text = text

                if(postList[position].image != ""){
                    Picasso.get().load(postList[position].image).into(holder.binding.postedImage)
                }else{
                    holder.binding.postedImage.visibility = View.GONE
                }

                holder.binding.caption.text = postList[position].caption

                //This is written later to open profile
                holder.binding.userImage.setOnClickListener {
                    replaceFrameWithFragment(postList[position].uid)
                }
            }

        holder.binding.share.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, postList[position].image)
            context.startActivity(intent)
        }
    }

    private fun replaceFrameWithFragment(uid: String) {
        val bundle = Bundle()
        bundle.putString("uid", uid)
        val targetFragment = OpenProfileFragment()
        targetFragment.arguments = bundle
        val fragTransaction = fragmentManager.beginTransaction()
        fragTransaction.replace(R.id.fragment_container, targetFragment)
        fragTransaction.addToBackStack(null)
        fragTransaction.commit()
    }

}