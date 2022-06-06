package com.example.smarthome

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.smarthome.databinding.ActivityRegBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class RegActivity : AppCompatActivity() {
    val linkRegUser = "https://smarthome.madskill.ru/user"
    lateinit var binding: ActivityRegBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        val uuid:String = sharedPreferences.getString("PHONE_UUID","nichego").toString()

        binding.apply {
            btnReg.setOnClickListener{
                if (isValidEmail(etMail.text.toString()) && etName.text.toString() != "" && etPass.text.toString() != "") {
                    startReg(uuid)
                }
                else{
                    val builder = AlertDialog.Builder(this@RegActivity)
                    builder.setTitle("Attention")
                        .setMessage("Некорректный ввод")
                        .setPositiveButton("Ok"){
                                dialog, _ -> dialog.cancel()
                        }
                        .show()
                }
            }
            btnEnter.setOnClickListener {
                finish()
            }
        }
    }
    fun isValidEmail(target: CharSequence?): Boolean {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }
    private fun startReg(uuid:String){
        CoroutineScope(Dispatchers.IO).launch {
            try {
                var url = URL(linkRegUser)
                with (url.openConnection() as HttpsURLConnection){
                    requestMethod = "POST"

                    binding.apply {
                        setRequestProperty("email",etMail.text.toString())
                        setRequestProperty("name",etName.text.toString())
                        setRequestProperty("password",etPass.text.toString())
                        setRequestProperty("uuid",uuid)

                    }
                    if(responseCode == 201){
                        withContext(Dispatchers.Main){
                            Toast.makeText(applicationContext,responseMessage,Toast.LENGTH_SHORT).show()
                            finish()
                        }
                    }
                    else{
                        withContext(Dispatchers.Main){
                            val builder = AlertDialog.Builder(this@RegActivity)
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
}