package com.samuel.myholderwallet.ui.transactionlist

import android.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.samuel.myholdertransaction.db.dao.TransactionDAO
import com.samuel.myholderwallet.R
import com.samuel.myholderwallet.db.AppDatabase
import com.samuel.myholderwallet.db.dao.BrokerDAO
import com.samuel.myholderwallet.extension.navigateWithAnimations
import com.samuel.myholderwallet.repository.BrokerRepository
import com.samuel.myholderwallet.repository.BrokerRepositoryImpl
import com.samuel.myholderwallet.repository.TransactionRepository
import com.samuel.myholderwallet.repository.TransactionRepositoryImpl
import com.samuel.myholderwallet.ui.paperlist.PaperListAdapter
import com.samuel.myholderwallet.ui.paperlist.PaperListFragmentDirections

class TransactionListFragment : Fragment(R.layout.fragment_transaction_list) {

    private val viewModel: TransactionListViewModel by viewModels {
        object: ViewModelProvider.Factory{
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val transactionDAO: TransactionDAO = AppDatabase.getInstance(requireContext()).transactionDAO
                val transactionRepository: TransactionRepository = TransactionRepositoryImpl(transactionDAO)

                val brokerDAO: BrokerDAO = AppDatabase.getInstance(requireContext()).brokerDAO
                val brokerRepository: BrokerRepository = BrokerRepositoryImpl(brokerDAO)

                return TransactionListViewModel(transactionRepository, brokerRepository) as T
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
            requireView().findViewById<Spinner>(R.id.spinner_broker).adapter =  ArrayAdapter(requireContext(), R.layout.spinneritem, R.id.spinnerText, list)
        }

        viewModel.brokerSelected.observe(viewLifecycleOwner){
            viewModel.getTransactions()
        }

        viewModel.allTransactionsEvent.observe(viewLifecycleOwner) {
            val transactionListAdapter = TransactionListAdapter(it).apply {
                onItemClick = { transaction ->
                    val directions  = TransactionListFragmentDirections
                        .actionTransactionListFragmentToTransactionFragment(transaction)

                    findNavController().navigateWithAnimations(directions)

                }
                onDeleteClick = { paper ->

                    AlertDialog.Builder(context)
                        .setTitle("Excluir Papel")
                        .setMessage("Deseja realmente excluir esse papel?")
                        .setPositiveButton("Confirmar") { _, _ ->
                            viewModel.deleteTransaction(paper)
                            viewModel.getTransactions()
                        }
                        .setNegativeButton("Cancelar"
                        ) { _, _ ->
                            //
                        }
                        .create()
                        .show()

                }
            }

            this.requireView().findViewById<RecyclerView>(R.id.recycler_transactions).run {
                setHasFixedSize(true)
                adapter = transactionListAdapter
            }
        }

        viewModel.messageStateEventData.observe(viewLifecycleOwner){ stringResId ->
            Snackbar.make(requireView(), stringResId, Snackbar.LENGTH_LONG).show()
        }

    }


    private fun configureViewListeners(){
        this.requireView().findViewById<FloatingActionButton>(R.id.faAddTransaction).setOnClickListener {
            findNavController().navigateWithAnimations(R.id.action_transactionListFragment_to_transactionFragment)
        }

        requireView().findViewById<Spinner>(R.id.spinner_broker).onItemSelectedListener = object  : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {  }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                viewModel.brokerSelected.postValue(viewModel.allBrokersEvent.value?.get(position) ?: null)
            }
        }
    }

}