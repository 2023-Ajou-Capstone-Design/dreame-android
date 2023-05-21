package com.example.dreamixmlversion.ui.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.dreamixmlversion.databinding.FragmentUserBinding
import com.example.dreamixmlversion.ui.sharing.SharingViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserFragment : Fragment() {

    private var _binding: FragmentUserBinding? = null
    private val binding get() = _binding
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindViews()
    }

    private fun bindViews() {

        bindProfileImageView()
        bindNicknameTextView()
        initChangingNicknameButton()
        initChangeNicknameLayout()
        initCheckMySharingButton()
        initTownSettingButton()
        initSecessionButton()
    }

    private fun bindProfileImageView() {
        _binding?.profileImageView?.setOnClickListener {

        }
    }

    private fun bindNicknameTextView() {
        _binding?.profileNickNameTextView?.text = userViewModel.getNickname()
    }

    private fun initChangingNicknameButton() {
        _binding?.changeNicknameButton?.setOnClickListener {
            showChangingNicknameLayout()
        }
    }

    private fun processChangingNickname(isSuccess: Boolean) {
        if (isSuccess) {
            _binding?.changeNicknameEditTextView?.setText("")
            bindNicknameTextView()
            hideChangingNicknameLayout()
        } else {
            Toast.makeText(requireContext(), "닉네임 변경에 실패했습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initChangeNicknameLayout() {
        _binding?.hideChangingNicknameLayoutButton?.setOnClickListener {
            hideChangingNicknameLayout()
        }

        _binding?.changeNicknameEditTextView?.addTextChangedListener {
            _binding?.requestChangingNicknameButton?.isEnabled = checkNicknameLength()
        }

        _binding?.requestChangingNicknameButton?.setOnClickListener {
            processChangingNickname(
                userViewModel.changeNickname(
                    _binding?.changeNicknameEditTextView?.text.toString()
                )
            )
        }
    }

    private fun initCheckMySharingButton() {

        val userType = userViewModel.getUserType()

        if (userType == "01") { // 결식아동
            _binding?.checkMyPostingButton?.visibility = View.GONE
        } else if (userType == "99") { // 푸드셰어러(기타)
            _binding?.checkMyPostingButton?.visibility = View.VISIBLE

            _binding?.checkMyPostingButton?.setOnClickListener {
                val action =
                    UserFragmentDirections.actionUserScreenToSharingScreen(SharingViewModel.SHARING_TYPE_BY_MY)
                findNavController().navigate(action)
            }
        }
    }

    private fun checkNicknameLength(): Boolean {
        if (_binding?.changeNicknameEditTextView?.text?.length!! > 1) {
            return true
        }
        return false
    }

    private fun initTownSettingButton() {
        _binding?.townSettingButton?.setOnClickListener {

        }
    }

    private fun initSecessionButton() {
        _binding?.secessionButton?.setOnClickListener {

        }
    }

    private fun showChangingNicknameLayout() {
        _binding?.apply {
            changeNicknameLayout.visibility = View.VISIBLE
        }
    }

    private fun hideChangingNicknameLayout() {
        _binding?.apply {
            changeNicknameLayout.visibility = View.INVISIBLE
        }
    }
}