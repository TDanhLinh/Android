package com.example.emailfe

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class EmailAdapter(private val emails: List<Email>) : RecyclerView.Adapter<EmailAdapter.EmailViewHolder>() {

    class EmailViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val sender: TextView = itemView.findViewById(R.id.textSender)
        val time: TextView = itemView.findViewById(R.id.textTime)
        val preview: TextView = itemView.findViewById(R.id.textPreview)
        val star: ImageView = itemView.findViewById(R.id.iconStar)
        val avatar: TextView = itemView.findViewById(R.id.textAvatar) // Add this line if you use an avatar
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmailViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_email, parent, false)
        return EmailViewHolder(view)
    }

    override fun onBindViewHolder(holder: EmailViewHolder, position: Int) {
        val email = emails[position]
        holder.sender.text = email.sender
        holder.time.text = email.timeSent
        holder.preview.text = email.preview
        holder.avatar.text = email.sender.first().toString() // Example to set avatar
        // You can set the star icon based on the email's star status
        holder.star.visibility = if (email.isStarred) View.VISIBLE else View.GONE
    }

    override fun getItemCount(): Int {
        return emails.size
    }
}
