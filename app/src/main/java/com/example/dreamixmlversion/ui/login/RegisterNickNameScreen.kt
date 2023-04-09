package com.example.dreamixmlversion.ui.login

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.dreamixmlversion.MainActivity
import com.example.dreamixmlversion.R

class RegisterNickNameScreen : Fragment() {

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_register_nickname, container, false)

        view.findViewById<Button>(R.id.registerButton).setOnClickListener {
            requireActivity().finish()
        }

        return view
    }
}