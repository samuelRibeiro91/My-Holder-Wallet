package com.samuel.myholderwallet.ui.transactionlist

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.samuel.myholderwallet.R
import com.samuel.myholderwallet.extension.navigateWithAnimations

class TransactionListFragment : Fragment(R.layout.fragment_transaction_list) {

    private lateinit var viewModel: TransactionListViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configureViewListeners()
    }


    private fun configureViewListeners(){
        this.requireView().findViewById<FloatingActionButton>(R.id.faAddTransaction).setOnClickListener {
            findNavController().navigateWithAnimations(R.id.action_transactionListFragment_to_transactionFragment)
        }
    }

}