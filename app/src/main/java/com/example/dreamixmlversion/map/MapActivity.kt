package com.example.dreamixmlversion.map

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.dreamixmlversion.databinding.ActivityMapBinding

class MapActivity: AppCompatActivity() {

    lateinit var binding: ActivityMapBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}