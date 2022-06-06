package com.example.smarthome

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat.animate
import com.example.smarthome.Classes.SSLOff
import com.example.smarthome.databinding.ActivityLaunchBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.net.HttpURLConnection
import java.net.URL
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.*
import javax.net.ssl.*

class LaunchActivity : AppCompatActivity() {
    lateinit var binding: ActivityLaunchBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLaunchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        SSLOff().trustAllCertificates()
        val sharedPreferences = getSharedPreferences("sharedPrefs",Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        binding.apply {
                    animate(imgLogo).apply{
                        rotationBy(360f)
                        duration = 6000L
                        withStartAction {
                            editor.apply{
                                putString("PHONE_UUID", UUID.randomUUID().toString().toUpperCase())
                            }.apply()
                        }
                        withEndAction{
                            val intent = Intent(applicationContext,EnterActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                        start()
                    }
        }
    }
}

