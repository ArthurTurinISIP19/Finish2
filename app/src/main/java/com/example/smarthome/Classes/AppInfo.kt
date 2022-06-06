package com.example.smarthome.Classes

import android.os.Build

class AppInfo{
    val appId = "com.example.smarthome"
    val device = Build.BRAND.toUpperCase() +" "+ Build.MODEL.toUpperCase()
}
