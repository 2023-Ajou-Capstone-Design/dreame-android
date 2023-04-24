package com.example.dreamixmlversion.ui.login

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.dreamixmlversion.R
import com.example.dreamixmlversion.databinding.FragmentLoginBinding

class LoginScreen : LoginBaseFragment<FragmentLoginBinding>() {
    override fun getViewBinding(): FragmentLoginBinding =
        FragmentLoginBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding?.apply {
            loginScreenFragment = this@LoginScreen
            viewModel = loginViewModel
        }

        _binding?.loginButton?.setOnClickListener {
            moveToQuestion(_binding?.addressEditTextView?.text.toString())
        }
    }

    private fun moveToQuestion(address: String?) {
        if (address?.isNotEmpty() == true) {
            loginViewModel.setEmailAddress(address)
            findNavController().navigate(R.id.action_login_screen_to_question)
        } else {
            Toast.makeText(requireContext(), "ID를 입력해주세요.", Toast.LENGTH_SHORT).show()
        }
    }
}