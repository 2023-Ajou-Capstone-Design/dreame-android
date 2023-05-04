package com.example.dreamixmlversion.ui.user

import android.os.Bundle
import android.view.View
import com.example.dreamixmlversion.databinding.FragmentUserBinding

class UserFragment : UserBaseFragment<FragmentUserBinding>() {

    companion object {
        const val TAG = "UserFragment"
    }

    override fun getViewBinding(): FragmentUserBinding =
        FragmentUserBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkUserRole()
        initViews()
    }

    private fun checkUserRole() {
        val userType = userViewModel.getUserType()

        if (userType == "01") { // 결식아동
            _binding?.checkMyPostingButton?.visibility = View.GONE
        } else if (userType == "99") { // 푸드셰어러(기타)
            _binding?.checkMyPostingButton?.visibility = View.VISIBLE
        }
    }

    private fun initViews() {
        _binding?.let {
            it.profileNickNameTextView.text = userViewModel.getNickname()

            it.profileImageView.setOnClickListener {

            }

            it.changeNicknameButton.setOnClickListener {
                userViewModel
            }

            it.checkMyPostingButton.setOnClickListener {

            }

            it.townSettingButton.setOnClickListener {

            }

            it.secessionButton.setOnClickListener {

            }
        }
    }

    fun showCardNumberLayout() {
        _binding?.changeNicknameLayout?.visibility = View.VISIBLE
    }

    fun hideCardNumberLayout() {
        _binding?.changeNicknameLayout?.visibility = View.INVISIBLE
    }
}