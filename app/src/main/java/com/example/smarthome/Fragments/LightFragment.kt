package com.example.smarthome.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.smarthome.R
import com.example.smarthome.databinding.FragmentLightBinding

class LightFragment : Fragment() {
    lateinit var binding: FragmentLightBinding

    lateinit var roomName:String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLightBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            tvRoomName.text = roomName
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(roomName: String) = LightFragment().apply {
            this.roomName = roomName
        }
    }
}