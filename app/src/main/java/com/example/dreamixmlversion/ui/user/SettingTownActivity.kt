package com.example.dreamixmlversion.ui.user

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.dreamixmlversion.databinding.ActivitySettingNewTownBinding
import com.example.dreamixmlversion.ui.login.LoginViewModel
import com.example.dreamixmlversion.ui.login.TownAdapter
import com.example.dreamixmlversion.ui.login.TownUiState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingTownActivity: AppCompatActivity() {

    private lateinit var binding: ActivitySettingNewTownBinding
    private val viewModel: TownViewModel by viewModels()
    private lateinit var adapter: TownAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingNewTownBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initAdapter()
        initRegionLayout()
        initRegisterButton()
        initTownLivedata()
    }

    private fun initAdapter() {
        adapter = TownAdapter { regionName ->
            binding.partTownRecyclerView.visibility = View.INVISIBLE
            inputRegionValueToTextView(regionName)
        }
        binding.partTownRecyclerView.adapter = adapter
    }

    private fun inputRegionValueToTextView(regionName: String) {
        if (isFilledFirst().not()) {
            binding.firstDestinationTextView.text = regionName
        } else if (isFilledSecond().not()) {
            binding.secondDestinationTextView.text = regionName
        } else if (isFilledThird().not()) {
            binding.thirdDestinationTextView.text = regionName
        } else {
            binding.fourthDestinationTextView.text = regionName

            binding.registerButton.visibility = View.VISIBLE
        }
    }

    private fun isFilledFirst() = binding.firstDestinationTextView.text?.length != 0
    private fun isFilledSecond() = binding.secondDestinationTextView.text?.length != 0
    private fun isFilledThird() = binding.thirdDestinationTextView.text?.length != 0
    private fun isFilledFourth() = binding.fourthDestinationTextView.text?.length != 0

    private fun initRegionLayout() {
        initFirstDestinationTextView()
        initSecondDestinationTextView()
        initThirdDestinationTextView()
        initFourthDestinationTextView()
    }

    private fun initFirstDestinationTextView() {
        binding.firstDestinationTextView.setOnClickListener {
            viewModel.requestTownList(region = LoginViewModel.TOWN_DO)
        }
    }

    private fun initSecondDestinationTextView() {
        binding.secondDestinationTextView.setOnClickListener {
            viewModel.requestTownList(
                region = LoginViewModel.TOWN_SI,
                binding.firstDestinationTextView.text.toString()
            )
        }
    }

    private fun initThirdDestinationTextView() {
        binding.thirdDestinationTextView.setOnClickListener {
            viewModel.requestTownList(
                region = LoginViewModel.TOWN_GUNGU,
                binding.firstDestinationTextView.text.toString(),
                binding.secondDestinationTextView.text.toString()
            )
        }
    }

    private fun initFourthDestinationTextView() {
        binding.fourthDestinationTextView.setOnClickListener {
            viewModel.requestTownList(
                region = LoginViewModel.TOWN_DONG,
                binding.firstDestinationTextView.text.toString(),
                binding.secondDestinationTextView.text.toString(),
                binding.thirdDestinationTextView.text.toString(),
            )
        }
    }

    private fun initRegisterButton() {

        binding.registerButton.apply {

            isVisible = isFilledFourth()

            setOnClickListener {
                binding.apply {
                    val town =
                        "${secondDestinationTextView.text} ${thirdDestinationTextView.text} ${fourthDestinationTextView.text}"
                    viewModel.updateTown(town)
                    finish()
                }
            }
        }
    }

    private fun initTownLivedata() {
        viewModel.queriedTown.observe(this) {
            when (it) {
                is TownUiState.SuccessGetTownList -> bindTownListInRecyclerView(it.townList)
                TownUiState.Uninitialized -> {}
            }
        }
    }

    private fun bindTownListInRecyclerView(townList: List<String>) {
        binding.partTownRecyclerView.visibility = View.VISIBLE
        adapter.submitList(townList)
    }
}