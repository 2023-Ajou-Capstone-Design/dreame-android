package com.example.dreamixmlversion.ui.login

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.dreamixmlversion.databinding.FragmentRegisterNicknameBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterNicknameScreen : LoginBaseFragment<FragmentRegisterNicknameBinding>() {

    override fun getViewBinding(): FragmentRegisterNicknameBinding =
        FragmentRegisterNicknameBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding?.apply {
            registerNicknameFragment = this@RegisterNicknameScreen
            viewModel = loginViewModel

            registerButton.setOnClickListener {
                val nickname = nicknameEditTextView.text.toString()
                val result = loginViewModel.isDuplicateNickname(nickname)

                if (result) {
                    Toast.makeText(requireContext(), "닉네임이 중복됩니다.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                loginViewModel.setNickname(nickname)
                registerFinally()
            }
        }
    }

    private fun registerFinally() {
        _binding?.viewModel?.registerFinally()
        Log.d("aaaaa", _binding?.viewModel?.getRegistrationInfo().toString())
    }
}