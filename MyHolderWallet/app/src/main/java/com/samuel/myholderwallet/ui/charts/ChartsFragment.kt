package com.samuel.myholderwallet.ui.charts

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.samuel.myholderwallet.R
import com.samuel.myholderwallet.extension.navigateWithAnimations

class ChartsFragment : Fragment(R.layout.fragment_charts) {
    private lateinit var viewModel: ChatsViewModel


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configureViewListeners()
    }

    private fun configureViewListeners() {
        this.requireView().findViewById<FloatingActionButton>(R.id.faOpenOptions).setOnClickListener {
            findNavController().navigateWithAnimations(R.id.action_chatsFragment_to_optionsFragment)
        }
    }

}