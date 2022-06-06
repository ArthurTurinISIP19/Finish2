package com.example.smarthome.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.smarthome.R
import com.example.smarthome.databinding.FragmentThermostatBinding


class ThermostatFragment : Fragment() {
    lateinit var binding: FragmentThermostatBinding

    lateinit var roomName:String

    var temp = 30
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentThermostatBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            tvRoomName.text = roomName
            tvTemp.text = temp.toString()

            btnHeating.setOnCheckedChangeListener { buttonView, isChecked ->
                if(isChecked){
                    temp = 30
                    tvTemp.text = temp.toString()
                    buttonView.setBackgroundResource(R.drawable.heating_on)
                    btnCool.isChecked = false
                }
                else{
                    buttonView.setBackgroundResource(R.drawable.heating)
                    btnCool.isChecked = true
                }
            }
            btnCool.setOnCheckedChangeListener { buttonView, isChecked ->
                if(isChecked){
                    temp = -30
                    tvTemp.text = temp.toString()
                    buttonView.setBackgroundResource(R.drawable.cool_on)
                    btnHeating.isChecked = false
                }
                else{
                    buttonView.setBackgroundResource(R.drawable.cool)
                    btnHeating.isChecked = true
                }
            }
            btnHeating.isChecked = true
        }
    }
    companion object {
        @JvmStatic
        fun newInstance(roomName: String) = ThermostatFragment().apply {
            this.roomName = roomName
        }
    }
}