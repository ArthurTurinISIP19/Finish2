package com.example.smarthome

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import com.example.smarthome.Adapters.AddRoomAdapter
import com.example.smarthome.Adapters.RoomsAdapter
import com.example.smarthome.Classes.AppInfo
import com.example.smarthome.Classes.Room
import com.example.smarthome.databinding.ActivityAddRoomBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class AddRoomActivity : AppCompatActivity() {
    lateinit var binding:ActivityAddRoomBinding
    lateinit var arrayRooms:ArrayList<Room>

    private lateinit var uuid:String
    private lateinit var token:String
    val linkSaveRoom = "https://smarthome.madskill.ru/rooms"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddRoomBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        uuid = sharedPreferences.getString("PHONE_UUID","nichego").toString()

        token = intent.getStringExtra("TOKEN").toString()

        arrayRooms = ArrayList()

        val room = Room("1","Kitchen",R.drawable.kitch_room_off)
        val room1 = Room("2","Bedroom",R.drawable.bedroom_off)
        val room2 = Room("3","Bathroom",R.drawable.bathroom_off)
        val room3 = Room("4","Office",R.drawable.office_room_off)
        val room4 = Room("5","TV Room",R.drawable.tv_room_off)
        val room5 = Room("6","Living Room",R.drawable.liv_room_off)
        val room6 = Room("7","Garage",R.drawable.garage_room_off)
        val room7 = Room("8","Toilet",R.drawable.toilet_room_off)
        val room8 = Room("9","Kid Room",R.drawable.kids_room_off)


        arrayRooms.add(room)
        arrayRooms.add(room1)
        arrayRooms.add(room2)
        arrayRooms.add(room3)
        arrayRooms.add(room4)
        arrayRooms.add(room5)
        arrayRooms.add(room6)
        arrayRooms.add(room7)
        arrayRooms.add(room8)


        binding.apply {
            recycler.apply {
                layoutManager = GridLayoutManager(context,3)
                adapter = AddRoomAdapter(this@AddRoomActivity,arrayRooms)

            }
            btnBack.setOnClickListener {
                finish()
            }
            btnSave.setOnClickListener {
               if (etRoomName.text!!.isNotEmpty()){
                   saveRoom()
                   val intent = Intent()
                   setResult(RESULT_OK, intent)
                   finish()
               }
               else{
                   val builder = AlertDialog.Builder(this@AddRoomActivity)
                   builder.setTitle("Attention")
                       .setMessage("Room name is empty")
                       .setPositiveButton("Ok"){
                               dialog,id -> dialog.cancel()
                       }
                   builder.show()
               }
            }
        }



    }
    private fun saveRoom(){
        CoroutineScope(Dispatchers.IO).launch {
            try {
                var url = URL(linkSaveRoom)
                with (url.openConnection() as HttpsURLConnection){
                    requestMethod = "POST"
                    setRequestProperty("name",binding.etRoomName.text.toString())
                    setRequestProperty("type","Room")
                    setRequestProperty("token",token)
                    setRequestProperty("uuid",uuid)
                    println("Response code1: $responseCode")

                    if(responseCode == 201){
                        withContext(Dispatchers.Main){
                            finish()
                        }
                    }
                    else{
                        withContext(Dispatchers.Main){
                            val builder = AlertDialog.Builder(this@AddRoomActivity)
                            builder.setTitle("Attention")
                                .setMessage(responseMessage)
                                .setPositiveButton("Ok"){
                                        dialog,id -> dialog.cancel()
                                }
                            builder.show()
                        }
                    }
                    this.disconnect()
                }
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
    }

}