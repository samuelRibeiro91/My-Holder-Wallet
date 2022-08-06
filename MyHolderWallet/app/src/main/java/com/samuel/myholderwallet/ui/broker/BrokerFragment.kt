package com.samuel.myholderwallet.ui.broker

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.samuel.myholderwallet.R
import com.samuel.myholderwallet.db.AppDatabase
import com.samuel.myholderwallet.db.dao.BrokerDAO
import com.samuel.myholderwallet.extension.hideKeyboard
import com.samuel.myholderwallet.repository.BrokerRepository
import com.samuel.myholderwallet.repository.BrokerRepositoryImpl

class BrokerFragment : Fragment(R.layout.fragment_broker) {

    private val viewModel: BrokerViewModel by viewModels {
        object: ViewModelProvider.Factory{
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val brokerDAO: BrokerDAO =
                    AppDatabase.getInstance(requireContext()).brokerDAO

                val repository: BrokerRepository = BrokerRepositoryImpl(brokerDAO)
                return BrokerViewModel(repository) as T
            }
        }
    }

    private val args: BrokerFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        args.broker?.let { brokerEntity ->
            requireView().findViewById<TextInputEditText>(R.id.input_name).setText(brokerEntity.name)
        }

        observeEvents()
        setListeners()
    }

    private fun observeEvents() {
        viewModel.brokerStateEventData.observe(viewLifecycleOwner){ brokerEvent ->
            when (brokerEvent){
                is BrokerViewModel.BrokerState.Inserted ->{
                    clearFields()
                    hideKeyBoard()
                    requireView().requestFocus()

                    findNavController().popBackStack()

                }

                is BrokerViewModel.BrokerState.Updated -> {
                    clearFields()
                    hideKeyBoard()

                    findNavController().popBackStack()
                }
            }

        }

        viewModel.messageStateEventData.observe(viewLifecycleOwner){ stringResId ->
            Snackbar.make(requireView(), stringResId, Snackbar.LENGTH_LONG).show()
        }
    }


    private fun clearFields() {
        requireView().findViewById<TextInputEditText>(R.id.input_name)?.text?.clear()
    }

    private fun hideKeyBoard() {
        val parentActivity = requireActivity()

        if (parentActivity is AppCompatActivity){
            parentActivity.hideKeyboard()
        }
    }

    private fun setListeners() {
        requireView().findViewById<FloatingActionButton>(R.id.faAddBroker).setOnClickListener {
            val name  = requireView().findViewById<TextInputEditText>(R.id.input_name).text.toString()

            if (name.isEmpty()){
                requireView().findViewById<TextInputEditText>(R.id.input_name).requestFocus()
                requireView().findViewById<TextInputEditText>(R.id.input_name).error = "Preencha o nome da corretora"
            }
            else
                viewModel.addOrUpdateBroker(name, args.broker?.id ?: 0)
        }
    }


}