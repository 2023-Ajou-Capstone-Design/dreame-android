package com.example.dreamixmlversion.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import com.example.dreamixmlversion.MainActivity
import com.example.dreamixmlversion.databinding.FragmentRegisterNicknameBinding
import com.example.dreamixmlversion.ui.sharing.uiState.SharingUiState
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

                loginViewModel.queriedRegisterProfileLiveData.observe(requireActivity()) {
                    when (it) {
                        LoginUiState.Uninitialized -> hideProgressBar()
                        LoginUiState.Loading -> showProgressBar()
                        is LoginUiState.RegisterProfile -> {
                            startActivity(Intent(requireActivity(), MainActivity::class.java))
                            requireActivity().finish()
                        }
                        LoginUiState.Error -> TODO()
                    }
                }
            }
        }
    }

    private fun showProgressBar() {
        _binding?.progressBar?.isVisible = true
    }

    private fun hideProgressBar() {
        _binding?.progressBar?.isVisible = false
    }

    private fun registerFinally() {
        _binding?.viewModel?.registerFinally()
    }
}