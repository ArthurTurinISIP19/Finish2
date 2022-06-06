package com.example.smarthome.Fragments

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import com.example.smarthome.Adapters.RoomsAdapter
import com.example.smarthome.AddRoomActivity
import com.example.smarthome.Classes.Room
import com.example.smarthome.R
import com.example.smarthome.databinding.FragmentRoomsBinding
import kotlinx.coroutines.*
import org.json.JSONObject
import java.net.URL
import javax.net.ssl.HttpsURLConnection


class RoomsFragment : Fragment() {
    lateinit var binding: FragmentRoomsBinding
    lateinit var arrayRooms:ArrayList<Room>

    val linkGetRooms = "https://smarthome.madskill.ru/rooms"
    var token = "none"
    var uuid = "none"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRoomsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            recycler.apply {
                layoutManager = GridLayoutManager(context,2)
            }
            btnAddRoom.setOnClickListener{
                val intent = Intent(requireContext(),AddRoomActivity::class.java)
                intent.putExtra("TOKEN",token)
                startActivityForResult(intent,101)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        arrayRooms = ArrayList()
        getRooms()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 101 && resultCode == RESULT_OK){
            Toast.makeText(context,"Room adding",Toast.LENGTH_SHORT).show()
        }
        else{
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Attention")
                .setMessage("Room not adding")
                .setPositiveButton("Ok"){
                        dialog, _ -> dialog.cancel()
                }
            builder.show()
        }
    }
    companion object {
        @JvmStatic
        fun newInstance(uuid: String, token: String) = RoomsFragment().apply {
            this.token = token
            this.uuid = uuid
        }

    }
    private fun getRooms() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                var url = URL(linkGetRooms)
                with(url.openConnection() as HttpsURLConnection) {

                    setRequestProperty("token",token)
                    setRequestProperty("uuid",uuid)

                    println("Response code: $responseCode")

                    var stringBuffer = StringBuffer()
                    if (responseCode == 200) {
                        inputStream.bufferedReader().use {
                            it.readLine().forEach { line ->
                                stringBuffer.append(line)
                            }
                            it.close()
                        }
                        val jsonObject = JSONObject(stringBuffer.toString())
                        println(jsonObject)
                        val jsonItems = jsonObject.getJSONArray("items")

                        for(i in 0 until  jsonItems.length()){
                            val jsonRoom = jsonItems.getJSONObject(i)
                            val room = Room(jsonRoom.getString("id").toString(),jsonRoom.getString("name").toString(),R.drawable.liv_room_off)
                            arrayRooms.add(room)
                        }

                        binding.apply {
                            withContext(Dispatchers.Main){
                                recycler.adapter = RoomsAdapter(requireContext(),token,arrayRooms)
                            }
                        }
                    } else
                        withContext(Dispatchers.Main) {
                            val builder = AlertDialog.Builder(requireContext())
                            builder.setTitle("Attention")
                                .setMessage(responseMessage)
                                .setPositiveButton("Ok") { dialog, id ->
                                    dialog.cancel()
                                }
                            builder.show()
                        }
                    this.disconnect()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            this.cancel()
        }
    }
}