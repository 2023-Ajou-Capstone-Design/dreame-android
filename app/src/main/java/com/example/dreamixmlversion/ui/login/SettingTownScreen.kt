package com.example.dreamixmlversion.ui.login

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.dreamixmlversion.R
import com.example.dreamixmlversion.databinding.FragmentSettingTownBinding

class SettingTownScreen: LoginBaseFragment<FragmentSettingTownBinding>() {

    override fun getViewBinding(): FragmentSettingTownBinding = FragmentSettingTownBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding?.apply {
            questionScreenFragment = this@SettingTownScreen
            viewModel = loginViewModel
        }

        initRegisterTown()
    }

    private fun initRegisterTown() {
        _binding?.registerButton?.setOnClickListener {
            if (_binding?.thirdDestinationEditText.toString().isBlank()) {
                Toast.makeText(requireContext(), "동네를 선택해주세요.", Toast.LENGTH_SHORT).show()
            } else {
                val address =
                    "${_binding?.firstDestinationEditText?.text} ${_binding?.secondDestinationEditText?.text} ${_binding?.thirdDestinationEditText?.text}"
                loginViewModel.setTownAddress(address)
                findNavController().navigate(R.id.action_setting_town_to_register_nickname)
            }
        }
    }
}