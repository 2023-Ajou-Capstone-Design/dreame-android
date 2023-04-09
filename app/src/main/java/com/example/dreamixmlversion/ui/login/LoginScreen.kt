package com.example.dreamixmlversion.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.dreamixmlversion.R

class LoginScreen: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        view.findViewById<Button>(R.id.loginButton).setOnClickListener {
            val address = view.findViewById<EditText>(R.id.addressEditTextView).text

            Navigation.findNavController(view).navigate(R.id.action_login_screen_to_question)
        }

        return view
    }
}