package com.example.dreamixmlversion.ui.login

import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import com.example.dreamixmlversion.R
import com.example.dreamixmlversion.databinding.FragmentQuestionBinding
import com.example.dreamixmlversion.ui.login.LoginBaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QuestionScreen: LoginBaseFragment<FragmentQuestionBinding>() {

    override fun getViewBinding(): FragmentQuestionBinding = FragmentQuestionBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding?.apply {
            questionScreenFragment = this@QuestionScreen
            viewModel = loginViewModel
        }

        initRadioNoButton()
    }

    fun showCardNumberLayout() {
        _binding?.cardNumberInputConstraintLayout?.visibility = View.VISIBLE
        activateRegisterButton()
        initPostponeButton()
    }

    fun hideCardNumberLayout() {
        _binding?.cardNumberInputConstraintLayout?.visibility = View.INVISIBLE
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

        _binding?.registerCardNumberButton?.setOnClickListener {
            val cardNumber =
                "${_binding?.cardNumber1?.text}${_binding?.cardNumber2?.text}${_binding?.cardNumber3?.text}${_binding?.cardNumber4?.text}"
            setCardNumberAndMoveToSettingTown(cardNumber)
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

    private fun moveToSettingTown() {
        findNavController().navigate(R.id.action_question_to_setting_town)
    }

    private fun setCardNumberAndMoveToSettingTown(cardNumber: String? = null) {
        _binding?.viewModel?.setIdentity("01")
        _binding?.viewModel?.setCardNumber(cardNumber)
        moveToSettingTown()
    }

    private fun initRadioNoButton() {
        _binding?.radioNo?.setOnClickListener {
            setCardNumberAndMoveToSettingTown()
        }
    }

    private fun initPostponeButton() {
        _binding?.postponeRegisterButton?.setOnClickListener {
            _binding?.viewModel?.setIdentity("99")
            setCardNumberAndMoveToSettingTown()
        }
    }
}