package com.samuel.myholderwallet.ui.charts

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.samuel.myholdertransaction.db.dao.TransactionDAO
import com.samuel.myholderwallet.R
import com.samuel.myholderwallet.db.AppDatabase
import com.samuel.myholderwallet.db.dao.BrokerDAO
import com.samuel.myholderwallet.extension.navigateWithAnimations
import com.samuel.myholderwallet.repository.BrokerRepository
import com.samuel.myholderwallet.repository.BrokerRepositoryImpl
import com.samuel.myholderwallet.repository.TransactionRepository
import com.samuel.myholderwallet.repository.TransactionRepositoryImpl

class ChartsFragment : Fragment(R.layout.fragment_charts) {
    private val viewModel: ChatsViewModel by viewModels {
        object: ViewModelProvider.Factory{
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val brokerDAO: BrokerDAO = AppDatabase.getInstance(requireContext()).brokerDAO
                val brokerRepository: BrokerRepository = BrokerRepositoryImpl(brokerDAO)

                val transactionDAO: TransactionDAO = AppDatabase.getInstance(requireContext()).transactionDAO
                val transactionRepository: TransactionRepository = TransactionRepositoryImpl(transactionDAO)

                return ChatsViewModel(brokerRepository, transactionRepository) as T
            }
        }
    }

    override fun onResume() {
        super.onResume()

        viewModel.getBrokers()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModelEvents()

        configureViewListeners()
    }

    private fun observeViewModelEvents() {
        viewModel.allBrokersEvent.observe(viewLifecycleOwner) { list ->
            requireView().findViewById<Spinner>(R.id.spinner_broker).adapter =  ArrayAdapter(requireContext(), R.layout.spinnerbolditem, R.id.spinnerText, list)
        }

        viewModel.brokerSelected.observe(viewLifecycleOwner){
            viewModel.getData()
        }

        viewModel.totalStockValue.observe(viewLifecycleOwner){
            requireView().findViewById<TextView>(R.id.tv_total_stock).text = "R$ ${String.format("%.2f",it)}"
        }

        viewModel.totalReitsValue.observe(viewLifecycleOwner){
            requireView().findViewById<TextView>(R.id.tv_total_reit).text = "R$ ${String.format("%.2f",it)}"
        }

        viewModel.totalAdrsValue.observe(viewLifecycleOwner){
            requireView().findViewById<TextView>(R.id.tv_total_adr).text = "R$ ${String.format("%.2f",it)}"
        }

        viewModel.accountBalance.observe(viewLifecycleOwner){
            requireView().findViewById<TextView>(R.id.tv_account_ballance).text = "R$ ${String.format("%.2f",it)}"
        }

        viewModel.totalValue.observe(viewLifecycleOwner){
            requireView().findViewById<TextView>(R.id.tv_total_value).text = "R$ ${String.format("%.2f",it)}"
        }

    }

    private fun configureViewListeners() {
        requireView().findViewById<FloatingActionButton>(R.id.faOpenOptions).setOnClickListener {
            findNavController().navigateWithAnimations(R.id.action_chatsFragment_to_optionsFragment)
        }

        requireView().findViewById<Spinner>(R.id.spinner_broker).onItemSelectedListener = object  : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {  }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                viewModel.brokerSelected.postValue(viewModel.allBrokersEvent.value?.get(position) ?: null)
            }
        }
    }

}