package com.example.smarthome

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.smarthome.Classes.AppInfo
import com.example.smarthome.databinding.ActivityEnterBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class EnterActivity : AppCompatActivity() {
    lateinit var binding:ActivityEnterBinding
    val linkRegMob = "https://smarthome.madskill.ru/mobile"
    val linkEnter = "https://smarthome.madskill.ru/user"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEnterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        val uuid:String = sharedPreferences.getString("PHONE_UUID","nichego").toString()

        regMobile(uuid)
        binding.apply {
            btnReg.setOnClickListener{
                val intent = Intent(applicationContext,RegActivity::class.java)
                startActivity(intent)
            }
            btnEnter.setOnClickListener{
                if (isValidEmail(etMail.text.toString()) && etPass.text.toString() != "") {
                    toEnter(uuid)
                }
                else{
                    val builder = AlertDialog.Builder(this@EnterActivity)
                    builder.setTitle("Attention")
                        .setMessage("Некорректный ввод")
                        .setPositiveButton("Ok"){
                                dialog, _ -> dialog.cancel()
                        }
                        .show()
                }
                   // Toast.makeText(applicationContext, "Некорректный ввод", Toast.LENGTH_SHORT).show()
            }
        }
    }
    fun isValidEmail(target: CharSequence?): Boolean {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }
    private fun toEnter(uuid:String){
        CoroutineScope(Dispatchers.IO).launch {
            try {
                var url = URL(linkEnter)
                with (url.openConnection() as HttpsURLConnection){
                    setRequestProperty("X-HTTP-Method-Override", "OPTIONS")
                    requestMethod = "POST"
                    binding.apply {
                        setRequestProperty("email",etMail.text.toString())
                        setRequestProperty("password",etPass.text.toString())
                        setRequestProperty("uuid",uuid)
                    }
                    var stringBuffer:StringBuffer = StringBuffer()
                    if(responseCode == 201){
                        inputStream.bufferedReader().use {
                            it.readLine().forEach { line ->
                                stringBuffer.append(line)
                            }
                        }
                        val jsonObject = JSONObject(stringBuffer.toString())
                        println("Token = "+jsonObject.get("token"))
                        val token = jsonObject.get("token").toString()
                        withContext(Dispatchers.Main){
                            Toast.makeText(applicationContext,responseMessage, Toast.LENGTH_SHORT).show()
                            val intent = Intent(applicationContext,MainActivity::class.java)
                            intent.putExtra("TOKEN",token)
                            startActivity(intent)
                            finish()
                        }
                    }
                    else{
                        withContext(Dispatchers.Main){
//                            Toast.makeText(
//                                applicationContext,
//                                responseMessage,
//                                Toast.LENGTH_SHORT
//                            ).show()
                            val builder = AlertDialog.Builder(this@EnterActivity)
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

    private fun regMobile(uuid:String){
        CoroutineScope(Dispatchers.IO).launch {
            try {
                var url = URL(linkRegMob)
                with (url.openConnection() as HttpsURLConnection){
                    requestMethod = "POST"

                    val data = "uuid=$uuid&appId=${AppInfo().appId}&device=${AppInfo().device}"
                    outputStream.bufferedWriter().use {
                        it.write(data)
                        it.flush()
                        it.close()
                    }
                    println("Response code1: $responseCode")
                    this.disconnect()
                }
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
    }
}