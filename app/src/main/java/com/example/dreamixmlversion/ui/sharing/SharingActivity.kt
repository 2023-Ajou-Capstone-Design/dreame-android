package com.example.dreamixmlversion.ui.sharing

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.example.dreamixmlversion.R
import com.example.dreamixmlversion.databinding.ActivitySharingBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SharingActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySharingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySharingBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}