package com.example.smarthome.Fragments

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.smarthome.Classes.AppInfo
import com.example.smarthome.EnterActivity
import com.example.smarthome.MainActivity
import com.example.smarthome.R
import com.example.smarthome.databinding.FragmentSettingsBinding
import kotlinx.coroutines.*
import org.json.JSONArray
import org.json.JSONObject
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class SettingsFragment : Fragment() {
    lateinit var binding:FragmentSettingsBinding

    var token = "none"
    var uuid = "none"

    private val linkGetSettings = "https://smarthome.madskill.ru/profile?token=$token&uuid=$uuid"
    private val linkSetSettings = "https://smarthome.madskill.ru/profile"
    private val linkExit = "https://smarthome.madskill.ru/user"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding = FragmentSettingsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getSettings()


        binding.apply {

            btnSave.setOnClickListener {
                CoroutineScope(Dispatchers.IO).launch {
                    try {

                        var url = URL(linkSetSettings)
                        with (url.openConnection() as HttpsURLConnection){
                            requestMethod = "POST"

                            binding.apply {
                                setRequestProperty("token",token)
                                setRequestProperty("email",etMail.text.toString())
                                setRequestProperty("name", "Vasylok")
                                setRequestProperty("username",etUserName.text.toString())
                                setRequestProperty("dateOf",etDate.text.toString())
                                setRequestProperty("phone",etPhone.text.toString())
                                setRequestProperty("uuid",uuid)

                            }
                            println(responseCode)
                            println(responseMessage)
                            if(responseCode == 200){
                                withContext(Dispatchers.Main){
                                    Toast.makeText(context,responseMessage,Toast.LENGTH_SHORT).show()
                                }
                            }
                            else{
                                val builder = AlertDialog.Builder(requireContext())
                                builder.setTitle("Attention")
                                    .setMessage(responseMessage)
                                    .setPositiveButton("Ok"){
                                            dialog,id -> dialog.cancel()
                                    }
                                builder.show()
                            }
                            this.disconnect()
                        }
                    }catch (e: Exception){
                        e.printStackTrace()
                    }
                    this.cancel()
                }
            }

            btnExit.setOnClickListener {
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        var url = URL(linkExit)
                        with (url.openConnection() as HttpsURLConnection){
                            setRequestProperty("X-HTTP-Method-Override", "DELETE")
                            requestMethod = "POST"

                            setRequestProperty("token",token)
                            println(responseCode)
                            if(responseCode == 200){
                                val intent = Intent(context,EnterActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                startActivity(intent)
                            }
                            else{
                                withContext(Dispatchers.Main){
                                    val builder = AlertDialog.Builder(requireContext())
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
                    }catch (e: Exception){
                        e.printStackTrace()
                    }
                }
            }
            btnCamera.setOnClickListener {
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                startActivityForResult(intent,101)
            }
        }
    }

    private fun getSettings() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                println(token)
                println(uuid)
                var url = URL(linkGetSettings)
                with (url.openConnection() as HttpsURLConnection){
                    println("Response code: $responseCode")

                    var stringBuffer = StringBuffer()
                    if(responseCode == 201){
                        inputStream.bufferedReader().use {
                            it.readLine().forEach { line ->
                                stringBuffer.append(line)
                            }
                            it.close()
                        }
                        val jsonObject = JSONObject(stringBuffer.toString())
                        println(jsonObject)
                        val jsonItems = jsonObject.getJSONArray("item")

                        binding.apply {
                            etMail.setText(jsonItems.getJSONObject(0).get("email").toString())
                            etPhone.setText(jsonItems.getJSONObject(0).get("phone").toString())
                            etDate.setText(jsonItems.getJSONObject(0).get("date").toString())
                            etUserName.setText(jsonItems.getJSONObject(0).get("username").toString())
                        }
                    }
                    else
                        withContext(Dispatchers.Main){
                            val builder = AlertDialog.Builder(requireContext())
                            builder.setTitle("Attention")
                                .setMessage(responseMessage)
                                .setPositiveButton("Ok"){
                                        dialog,id -> dialog.cancel()
                                }
                            builder.show()
                        }
                    this.disconnect()
                }
            }catch (e:Exception){
                e.printStackTrace()
            }
            this.cancel()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 101 && resultCode == RESULT_OK){
            val uri: Uri? = data?.data
            println(uri)
            binding.userImage.setImageURI(uri)
        }

    }

    companion object {
        @JvmStatic
        fun newInstance(uuid: String, token: String) = SettingsFragment().apply {
            this.token = token
            this.uuid = uuid
        }
    }
}