package com.example.dreamixmlversion.ui.login

import android.os.Bundle
import android.view.View
import com.example.dreamixmlversion.databinding.FragmentRegisterNicknameBinding

class RegisterNicknameScreen : LoginBaseFragment<FragmentRegisterNicknameBinding>() {

    override fun getViewBinding(): FragmentRegisterNicknameBinding =
        FragmentRegisterNicknameBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    private fun isDuplicateNickName(): Boolean {
        return false
    }
}