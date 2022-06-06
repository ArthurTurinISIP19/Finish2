package com.example.smarthome

import android.app.AlertDialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.smarthome.Classes.Device
import com.example.smarthome.Fragments.LightFragment
import com.example.smarthome.Fragments.ThermostatFragment
import com.example.smarthome.databinding.ActivityDevicesBinding
import kotlinx.coroutines.*
import org.json.JSONObject
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class DevicesActivity : AppCompatActivity() {
    lateinit var binding: ActivityDevicesBinding

    lateinit var roomId:String
    var roomName = "None"
    lateinit var uuid:String
    lateinit var token:String

    lateinit var arrayDevices:ArrayList<Device>

    val linkDevices = "https://smarthome.madskill.ru/devices"

    var count = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDevicesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        arrayDevices = ArrayList()

        val sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        uuid = sharedPreferences.getString("PHONE_UUID","nichego").toString()

        token = intent.getStringExtra("TOKEN").toString()

        roomId = intent.getStringExtra("ROOM_ID").toString()
        roomName = intent.getStringExtra("ROOM_NAME").toString()
        println("Token = $token")

        getDevices()
        binding.apply {
            tvName.text = roomName

            btnBack.setOnClickListener {
                finish()
            }
            btnAddDevices.setOnClickListener {
                val menu = devMenu.menu
               if (count < 2){
                   val builder = AlertDialog.Builder(this@DevicesActivity)
                   builder.setTitle("Choice type")
                       .setMessage("Choice type of device")
                       .setPositiveButton("Light"){
                               dialog,_ -> menu.add(1,R.id.item1,1,"Light").setIcon(R.drawable.led)
                           val roomType = "LED"
                           saveDevice(roomType)
                           count++
                           devMenu.selectedItemId = R.id.item1

                       }
                       .setNegativeButton("Thermostat"){
                               dialog,_ -> menu.add(1,R.id.item2,2,"Thermostat").setIcon(R.drawable.thermostat)
                           val roomType = "Thermostat"
                           saveDevice(roomType)
                           count++
                           devMenu.selectedItemId = R.id.item2
                       }
                       .show()
               }
                else{
                   val builder = AlertDialog.Builder(this@DevicesActivity)
                   builder.setTitle("Attention")
                       .setMessage("Max count")
                       .setPositiveButton("Ok"){
                               dialog,_ -> dialog.cancel()
                       }
                       .show()
               }
            }

            devMenu.setOnItemSelectedListener {
                when(it.itemId){
                    R.id.item1 ->{
                        supportFragmentManager
                            .beginTransaction()
                            .replace(R.id.placeholder,LightFragment.newInstance(roomName))
                            .commit()
                    }
                    R.id.item2 ->{
                        supportFragmentManager
                            .beginTransaction()
                            .replace(R.id.placeholder,ThermostatFragment.newInstance(roomName))
                            .commit()
                    }
                }
                true
            }

        }
    }
    private fun getDevices(){
        CoroutineScope(Dispatchers.IO).launch {
            try {
                var url = URL(linkDevices)
                with(url.openConnection() as HttpsURLConnection) {
                    setRequestProperty("room",roomId)
                    setRequestProperty("token",token)
                    setRequestProperty("uuid",uuid)

                    println("Response code: $responseCode")

                    var stringBuffer = StringBuffer()
                    if (responseCode == 201) {
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
                            val device = Device(jsonRoom.getString("id").toString(),jsonRoom.getString("type").toString())
                            arrayDevices.add(device)
                        }

                        binding.apply {
                            withContext(Dispatchers.Main){
                                val menu = devMenu.menu
                                for (i in 0 until arrayDevices.size){
                                    if(arrayDevices[i].type == "LED"){
                                        menu.add(1,R.id.item1,i,"Light").setIcon(R.drawable.led)
                                        count++
                                    }
                                    else{
                                        menu.add(1,R.id.item2,i,"Thermostat").setIcon(R.drawable.thermostat)
                                        count++
                                    }
                                }
                                devMenu.selectedItemId = menu.getItem(0).itemId
                            }
                        }
                    } else
                        withContext(Dispatchers.Main) {
                            val builder = AlertDialog.Builder(this@DevicesActivity)
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
    private fun saveDevice(roomType: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                var url = URL(linkDevices)
                with (url.openConnection() as HttpsURLConnection){
                    requestMethod = "POST"
                    setRequestProperty("type",roomType)
                    setRequestProperty("room",roomId)
                    setRequestProperty("token",token)
                    setRequestProperty("uuid",uuid)

                    println("Response code1: $responseCode")

                    if(responseCode == 200){
                        withContext(Dispatchers.Main){
                            Toast.makeText(this@DevicesActivity,responseMessage,Toast.LENGTH_SHORT).show()
                        }
                    }
                    else{
                        withContext(Dispatchers.Main){
                            val builder = androidx.appcompat.app.AlertDialog.Builder(this@DevicesActivity)
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