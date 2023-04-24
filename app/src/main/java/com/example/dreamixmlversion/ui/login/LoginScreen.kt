package com.example.dreamixmlversion.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.example.dreamixmlversion.R

class LoginScreen: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        view.findViewById<Button>(R.id.loginButton).setOnClickListener {
            val address = view.findViewById<EditText>(R.id.addressEditTextView).text.toString()

            val action = LoginScreenDirections.actionLoginScreenToQuestion(address)
            view.findNavController().navigate(action)
        }

        return view
    }
}