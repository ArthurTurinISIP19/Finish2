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
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.smarthome.AddRoomActivity
import com.example.smarthome.Classes.Room
import com.example.smarthome.DevicesActivity
import com.example.smarthome.R

class RoomsAdapter(val context: Context,val token:String, val arrayRooms: ArrayList<Room>): RecyclerView.Adapter<RoomsAdapter.ViewHolder>() {
    class ViewHolder(itemView:View): RecyclerView.ViewHolder(itemView) {
        val cardImage:ImageView = itemView.findViewById(R.id.cardImage)
        val cardTitle:TextView = itemView.findViewById(R.id.cardTitle)

        val roomCard: CardView = itemView.findViewById(R.id.roomCard)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view:View = LayoutInflater.from(parent.context).inflate(R.layout.item_room,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.cardImage.setImageResource(arrayRooms[position].img)
        holder.cardTitle.text = arrayRooms[position].title

        holder.roomCard.setOnClickListener{
            val intent = Intent(context,DevicesActivity::class.java)
            intent.putExtra("ROOM_NAME",arrayRooms[position].title)
            intent.putExtra("ROOM_ID",arrayRooms[position].room_id)
            intent.putExtra("TOKEN",token)
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = arrayRooms.size
}