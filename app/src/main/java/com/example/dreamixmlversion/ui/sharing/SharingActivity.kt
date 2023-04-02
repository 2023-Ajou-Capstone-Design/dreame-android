package com.example.dreamixmlversion.ui.sharing

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.dreamixmlversion.databinding.ActivitySharingBinding

class SharingActivity: AppCompatActivity() {

    lateinit var binding: ActivitySharingBinding
    lateinit var sharingAdapter: SharingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySharingBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}