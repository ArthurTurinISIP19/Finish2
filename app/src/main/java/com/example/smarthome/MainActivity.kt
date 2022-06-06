package com.example.smarthome

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.smarthome.Fragments.OtherFragment
import com.example.smarthome.Fragments.RoomsFragment
import com.example.smarthome.Fragments.SettingsFragment
import com.example.smarthome.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        val uuid:String = sharedPreferences.getString("PHONE_UUID","nichego").toString()

        val token = intent.getStringExtra("TOKEN").toString()


        binding.bottomNav.apply {
            setOnItemSelectedListener {
                when(it.itemId){
                    R.id.item1 ->{
                        supportFragmentManager
                            .beginTransaction()
                            .replace(R.id.placeholder, RoomsFragment.newInstance(uuid,token))
                            .commit()

                    }
                    R.id.item4 ->{
                        supportFragmentManager
                            .beginTransaction()
                            .replace(R.id.placeholder, SettingsFragment.newInstance(uuid,token))
                            .commit()
                    }
                    else ->{
                        supportFragmentManager
                            .beginTransaction()
                            .replace(R.id.placeholder, OtherFragment.newInstance())
                            .commit()
                    }

                }

                true
            }
            selectedItemId = R.id.item1
        }



    }
}