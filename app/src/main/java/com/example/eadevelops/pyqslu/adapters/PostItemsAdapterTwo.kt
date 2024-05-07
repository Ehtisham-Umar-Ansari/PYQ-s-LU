package com.example.eadevelops.pyqslu.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.eadevelops.pyqslu.R
import com.example.eadevelops.pyqslu.databinding.PostItemsBinding
import com.example.eadevelops.pyqslu.models.Post
import com.example.eadevelops.pyqslu.models.User
import com.example.eadevelops.pyqslu.utils.USER_NODE
import com.github.marlonlom.utilities.timeago.TimeAgo
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.firestore.toObject
import com.squareup.picasso.Picasso

class PostItemsAdapterTwo(var context: Context, private var postList: ArrayList<Post>) : RecyclerView.Adapter<PostItemsAdapterTwo.PostViewHolder>() {

    inner class PostViewHolder(val binding: PostItemsBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = PostItemsBinding.inflate(LayoutInflater.from(context), parent, false)
        return PostViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return  postList.size
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        Firebase.firestore.collection(USER_NODE).document(postList[position].uid).get()
            .addOnSuccessListener {
                val user = it.toObject<User>()

                Picasso.get().load(user!!.image).placeholder(R.drawable.profile_img).into(holder.binding.userImage)

                holder.binding.name.text = user.name

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
            }

        holder.binding.share.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, postList[position].image)
            context.startActivity(intent)
        }
    }

}