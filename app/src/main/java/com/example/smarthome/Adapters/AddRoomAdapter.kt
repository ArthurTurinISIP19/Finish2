package com.example.smarthome.Adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.core.view.size
import androidx.recyclerview.widget.RecyclerView
import com.example.smarthome.Classes.Room
import com.example.smarthome.R
import com.google.android.material.card.MaterialCardView

class AddRoomAdapter(val context: Context, val arrayRooms: ArrayList<Room>): RecyclerView.Adapter<AddRoomAdapter.ViewHolder>() {

    var checkedRoom = "Kitchen"


    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val cardImage: ImageView = itemView.findViewById(R.id.cardImage)
        val cardTitle: TextView = itemView.findViewById(R.id.cardTitle)
        val roomCard:MaterialCardView = itemView.findViewById(R.id.roomCard)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_add_room,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.cardImage.setImageResource(arrayRooms[position].img)
        holder.cardTitle.text = arrayRooms[position].title

    }

    override fun getItemCount() = arrayRooms.size
}