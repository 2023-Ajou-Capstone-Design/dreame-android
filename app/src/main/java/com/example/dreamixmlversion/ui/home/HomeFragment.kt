package com.example.dreamixmlversion.ui.home

import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.dreamixmlversion.R
import com.example.dreamixmlversion.databinding.FragmentHomeBinding
import com.example.dreamixmlversion.ui.map.MapActivity
import com.example.dreamixmlversion.ui.sharing.SharingViewModel
import com.example.dreamixmlversion.ui.user.UserNicknameUiState
import com.example.dreamixmlversion.ui.user.UserRestPointUiState
import com.example.dreamixmlversion.ui.user.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment: Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: UserViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        initStoreButton()
        initFoodSharingButton()
        initCardInfo()
        initRestPointLiveData()
    }

    private fun initStoreButton() {
        with(binding) {
            openSpotDataButton.setOnClickListener {
                context?.let {
                    startActivity(Intent(it, MapActivity::class.java))
                }
            }
        }
    }

    private fun initFoodSharingButton() {
        with(binding) {
            openFoodShareButton.setOnClickListener {
                context?.let {
                    val action = HomeFragmentDirections.actionHomeScreenToSharingScreen(SharingViewModel.SHARING_TYPE_BY_TOWN)
                    findNavController().navigate(action)
                }
            }
        }
    }

    private fun initCardInfo() {
        with(binding) {
            if (viewModel.getUserType() == "99") {
                _binding?.cardInfoLayout?.visibility = View.INVISIBLE
            }

            updateCardNumberInfo()

            changeCardNumberButton.setOnClickListener {
                cardNumberInputConstraintLayout.visibility = View.VISIBLE

                hideCardNumberLayoutButton.isEnabled = true

                activateRegisterButton()
            }

            checkRestPointButton.setOnClickListener {
                restPointInquireLayout.visibility = View.VISIBLE

                viewModel.getRestPoint()
            }

            closeCheckRestPointLayout.setOnClickListener {
                restPointInquireLayout.visibility = View.INVISIBLE
            }

            registerCardNumberButton.setOnClickListener {
                val cardNumber =
                    "${_binding?.cardNumber1?.text}${_binding?.cardNumber2?.text}${_binding?.cardNumber3?.text}${_binding?.cardNumber4?.text}"
                viewModel.putCardNumber(cardNumber)
                cardNumberInputConstraintLayout.visibility = View.INVISIBLE
                updateCardNumberInfo()
            }

            postponeRegisterButton.setOnClickListener {
                cardNumberInputConstraintLayout.visibility = View.INVISIBLE
            }

            hideCardNumberLayoutButton.setOnClickListener {
                cardNumberInputConstraintLayout.visibility = View.INVISIBLE
            }
        }
    }

    private fun initRestPointLiveData() {
        viewModel.queriedRestPoint.observe(requireActivity()) {
            when (it) {
                is UserRestPointUiState.SuccessGetRestPoint -> bindRestPoint(it.restPoint)
                UserRestPointUiState.Uninitialized -> {}
            }
        }
    }

    private fun bindRestPoint(point: String) {
        _binding?.cardPointTextView?.text = point
    }

    private fun updateCardNumberInfo() {
        val cardNumber = viewModel.getCardNumber()
        if (cardNumber == null) {
            binding.cardNumberTextView.text = "등록된 정보가 없습니다"
        } else {
            binding.cardNumberTextView.text = cardNumber.chunked(4).joinToString("-")
        }
    }

    private fun activateRegisterButton() {
        _binding?.cardNumber1?.addTextChangedListener {
            _binding?.registerCardNumberButton?.isEnabled = isWrittenComplete() == true
        }
        _binding?.cardNumber2?.addTextChangedListener {
            _binding?.registerCardNumberButton?.isEnabled = isWrittenComplete() == true
        }
        _binding?.cardNumber3?.addTextChangedListener {
            _binding?.registerCardNumberButton?.isEnabled = isWrittenComplete() == true
        }
        _binding?.cardNumber4?.addTextChangedListener {
            _binding?.registerCardNumberButton?.isEnabled = isWrittenComplete() == true
        }
    }

    private fun isWrittenComplete(): Boolean {
        if (
            _binding?.cardNumber1?.text?.length == 4 &&
            _binding?.cardNumber2?.text?.length == 4 &&
            _binding?.cardNumber3?.text?.length == 4 &&
            _binding?.cardNumber4?.text?.length == 4
        ) {
            return true
        }
        return false
    }
}