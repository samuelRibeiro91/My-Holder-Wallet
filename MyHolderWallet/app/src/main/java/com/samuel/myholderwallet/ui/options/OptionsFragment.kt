package com.samuel.myholderwallet.ui.options

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.samuel.myholderwallet.R
import com.samuel.myholderwallet.extension.navigateWithAnimations


class OptionsFragment : Fragment(R.layout.fragment_options) {
    private lateinit var viewModel: OptionsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configureViewListeners()
    }

    private fun configureViewListeners() {
        this.requireView().findViewById<FloatingActionButton>(R.id.faCloseOptions).setOnClickListener {
            findNavController().navigateWithAnimations(R.id.chartsFragment)
        }

        this.requireView().findViewById<FloatingActionButton>(R.id.faOpenPapers).setOnClickListener {
            findNavController().navigateWithAnimations(R.id.action_optionsFragment_to_paperListFragment)
        }

        this.requireView().findViewById<FloatingActionButton>(R.id.faOpenTransactions).setOnClickListener {
            findNavController().navigateWithAnimations(R.id.action_optionsFragment_to_transactionListFragment)
        }

        this.requireView().findViewById<FloatingActionButton>(R.id.faOpenBrokers).setOnClickListener {
            findNavController().navigateWithAnimations(R.id.action_optionsFragment_to_brokerListFragment)
        }
    }




}