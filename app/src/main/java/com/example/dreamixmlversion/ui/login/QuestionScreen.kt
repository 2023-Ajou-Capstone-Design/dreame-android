package com.example.dreamixmlversion.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.dreamixmlversion.R

class QuestionScreen: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_question, container, false)

        view.findViewById<Button>(R.id.radio_yes).setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_question_to_register_nickname)
        }

        view.findViewById<Button>(R.id.radio_no).setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_question_to_register_nickname)
        }

        return view
    }

    fun onRadioButtonClicked(view: View) {
        if (view is RadioButton) {
            val checked = view.isChecked

            when (view.getId()) {
                R.id.radio_yes -> {
                    Toast.makeText(requireContext(), "yes", Toast.LENGTH_LONG).show()
                }
                R.id.radio_no -> {
                    Toast.makeText(requireContext(), "no", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}