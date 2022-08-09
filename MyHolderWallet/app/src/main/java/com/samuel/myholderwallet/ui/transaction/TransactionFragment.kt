package com.samuel.myholderwallet.ui.transaction

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.samuel.myholdertransaction.db.dao.TransactionDAO
import com.samuel.myholderwallet.R
import com.samuel.myholderwallet.db.AppDatabase
import com.samuel.myholderwallet.db.dao.BrokerDAO
import com.samuel.myholderwallet.db.dao.PaperDAO
import com.samuel.myholderwallet.repository.*
import com.samuel.myholderwallet.types.MovementTypes


class TransactionFragment : Fragment(R.layout.fragment_transaction) {


    private val viewModel: TransactionViewModel by viewModels {
        object: ViewModelProvider.Factory{
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val transactionDAO: TransactionDAO = AppDatabase.getInstance(requireContext()).transactionDAO
                val transactionRepository: TransactionRepository = TransactionRepositoryImpl(transactionDAO)

                val paperDAO: PaperDAO = AppDatabase.getInstance(requireContext()).paperDAO
                val paperRepository: PaperRepository = PaperRepositoryImpl(paperDAO)

                val brokerDAO: BrokerDAO = AppDatabase.getInstance(requireContext()).brokerDAO
                val brokerRepository: BrokerRepository = BrokerRepositoryImpl(brokerDAO)

                return TransactionViewModel(transactionRepository = transactionRepository,paperRepository = paperRepository, brokerRepository = brokerRepository) as T
            }
        }
    }


    override fun onResume() {
        super.onResume()
        viewModel.getBrokers()
        viewModel.getPapers()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModelEvents()

        configureViewListeners()

        requireView().findViewById<Spinner>(R.id.spinner_transaction_type).adapter =  ArrayAdapter(requireContext(), R.layout.spinneritem, R.id.spinnerText, MovementTypes.values())
    }

    private fun observeViewModelEvents() {
        viewModel.allBrokersEvent.observe(viewLifecycleOwner) { list ->
            requireView().findViewById<Spinner>(R.id.spinner_broker).adapter =  ArrayAdapter(requireContext(), R.layout.spinneritem, R.id.spinnerText, list)
        }

        viewModel.allPapersEvent.observe(viewLifecycleOwner){ list ->
            requireView().findViewById<Spinner>(R.id.spinner_paper).adapter =  ArrayAdapter(requireContext(), R.layout.spinneritem, R.id.spinnerText, list)
        }

    }

    private fun configureViewListeners() {

        requireView().findViewById<Spinner>(R.id.spinner_paper).onItemSelectedListener = object  : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                viewModel.paperSelected.postValue(viewModel.allPapersEvent.value?.get(position) ?: null)
            }
        }

        requireView().findViewById<Spinner>(R.id.spinner_broker).onItemSelectedListener = object  : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {  }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                viewModel.brokerSelected.postValue(viewModel.allBrokersEvent.value?.get(position) ?: null)
            }
        }


        requireView().findViewById<Spinner>(R.id.spinner_transaction_type).onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                 when(MovementTypes.values()[position]){
                     MovementTypes.BUY_PAPERS ->{
                         requireView().findViewById<Spinner>(R.id.spinner_paper).visibility = View.VISIBLE
                         requireView().findViewById<TextInputEditText>(R.id.input_quantity).visibility = View.VISIBLE
                         requireView().findViewById<TextInputEditText>(R.id.input_costs).visibility = View.VISIBLE

                         requireView().findViewById<TextInputLayout>(R.id.input_layout_value).hint = "Valor Unitário"
                     }
                     MovementTypes.MONEY_DEPOSIT -> {
                         requireView().findViewById<Spinner>(R.id.spinner_paper).visibility = View.GONE
                         requireView().findViewById<TextInputEditText>(R.id.input_quantity).visibility = View.GONE
                         requireView().findViewById<TextInputEditText>(R.id.input_costs).visibility = View.GONE

                         requireView().findViewById<TextInputLayout>(R.id.input_layout_value).hint = "Valor Total"
                     }

                     MovementTypes.CASH_WITHDRAWAL -> {
                         requireView().findViewById<Spinner>(R.id.spinner_paper).visibility = View.GONE
                         requireView().findViewById<TextInputEditText>(R.id.input_quantity).visibility = View.GONE
                         requireView().findViewById<TextInputEditText>(R.id.input_costs).visibility = View.VISIBLE

                         requireView().findViewById<TextInputLayout>(R.id.input_layout_value).hint = "Valor Total"
                     }

                     MovementTypes.INFLOW_DIVIDENDS -> {
                         requireView().findViewById<Spinner>(R.id.spinner_paper).visibility = View.VISIBLE
                         requireView().findViewById<TextInputEditText>(R.id.input_quantity).visibility = View.GONE
                         requireView().findViewById<TextInputEditText>(R.id.input_costs).visibility = View.GONE

                         requireView().findViewById<TextInputLayout>(R.id.input_layout_value).hint = "Valor Total"
                     }

                     MovementTypes.SELL_PAPERS -> {
                         requireView().findViewById<Spinner>(R.id.spinner_paper).visibility = View.VISIBLE
                         requireView().findViewById<TextInputEditText>(R.id.input_quantity).visibility = View.VISIBLE
                         requireView().findViewById<TextInputEditText>(R.id.input_costs).visibility = View.VISIBLE

                         requireView().findViewById<TextInputLayout>(R.id.input_layout_value).hint = "Valor Unitário"
                     }
                 }
            }

        }
    }

}