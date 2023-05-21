package com.example.dreamixmlversion.ui.login

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import com.example.dreamixmlversion.R
import com.example.dreamixmlversion.databinding.FragmentSettingTownBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingTownScreen : LoginBaseFragment<FragmentSettingTownBinding>() {

    private lateinit var adapter: TownAdapter

    override fun getViewBinding(): FragmentSettingTownBinding =
        FragmentSettingTownBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding?.apply {
            settingTownFragment = this@SettingTownScreen
            viewModel = loginViewModel
        }

        initAdapter()
        initRegionLayout()
        initRegisterButton()
        initTownLivedata()
    }

    private fun initAdapter() {
        adapter = TownAdapter { regionName ->
            _binding?.partTownRecyclerView?.visibility = View.INVISIBLE
            inputRegionValueToTextView(regionName)
        }
        _binding?.partTownRecyclerView?.adapter = adapter
    }

    private fun inputRegionValueToTextView(regionName: String) {
        if (isFilledFirst().not()) {
            _binding?.firstDestinationTextView?.text = regionName
        } else if (isFilledSecond()) {
            _binding?.secondDestinationTextView?.text = regionName
        } else if (isFilledThird()) {
            _binding?.thirdDestinationTextView?.text = regionName
        } else {
            _binding?.fourthDestinationTextView?.text = regionName
        }
    }

    private fun isFilledFirst() = _binding?.firstDestinationTextView?.text?.length != 0
    private fun isFilledSecond() = _binding?.secondDestinationTextView?.text?.length != 0
    private fun isFilledThird() = _binding?.thirdDestinationTextView?.text?.length != 0
    private fun isFilledFourth() = _binding?.fourthDestinationTextView?.text?.length != 0

    private fun initRegionLayout() {
        initFirstDestinationTextView()
        initSecondDestinationTextView()
        initThirdDestinationTextView()
        initFourthDestinationTextView()
    }

    private fun initFirstDestinationTextView() {
        _binding?.firstDestinationTextView?.setOnClickListener {
            loginViewModel.requestTownList(region = LoginViewModel.TOWN_DO)
        }
    }

    private fun initSecondDestinationTextView() {
        _binding?.secondDestinationTextView?.setOnClickListener {
            loginViewModel.requestTownList(
                region = LoginViewModel.TOWN_SI,
                _binding?.firstDestinationTextView?.text.toString()
            )
        }
    }

    private fun initThirdDestinationTextView() {
        _binding?.thirdDestinationTextView?.setOnClickListener {
            loginViewModel.requestTownList(
                region = LoginViewModel.TOWN_DO,
                _binding?.firstDestinationTextView?.text.toString(),
                _binding?.secondDestinationTextView?.text.toString()
            )
        }
    }

    private fun initFourthDestinationTextView() {
        _binding?.fourthDestinationTextView?.setOnClickListener {
            loginViewModel.requestTownList(
                region = LoginViewModel.TOWN_DO,
                _binding?.firstDestinationTextView?.text.toString(),
                _binding?.secondDestinationTextView?.text.toString(),
                _binding?.thirdDestinationTextView?.text.toString(),
                )
        }
    }

    private fun initTownLivedata() {
        loginViewModel.queriedTown.observe(requireActivity()) {
            when (it) {
                is TownUiState.SuccessGetTownList -> bindTownListInRecyclerView(it.townList)
                TownUiState.Uninitialized -> {}
            }
        }
    }

    private fun bindTownListInRecyclerView(townList: List<String>) {
        adapter.submitList(townList)
    }

    private fun initRegisterButton() {

        _binding?.registerButton?.apply {

            isVisible = isFilledFourth()

            setOnClickListener {
                _binding?.apply {
                    val town =
                        "${firstDestinationTextView.text} ${secondDestinationTextView.text} ${thirdDestinationTextView.text} ${fourthDestinationTextView.text}"
                    loginViewModel.setTownAddress(town)
                    findNavController().navigate(R.id.action_setting_town_to_register_nickname)
                }
            }
        }
    }
}