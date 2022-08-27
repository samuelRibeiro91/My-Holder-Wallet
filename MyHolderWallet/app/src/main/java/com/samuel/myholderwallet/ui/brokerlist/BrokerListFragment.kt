package com.samuel.myholderwallet.ui.brokerlist

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.samuel.myholderwallet.R
import com.samuel.myholderwallet.db.AppDatabase
import com.samuel.myholderwallet.db.dao.BrokerDAO
import com.samuel.myholderwallet.extension.navigateWithAnimations
import com.samuel.myholderwallet.repository.BrokerRepository
import com.samuel.myholderwallet.repository.BrokerRepositoryImpl

class BrokerListFragment : Fragment(R.layout.fragment_broker_list) {

    private val viewModel: BrokerListViewModel by viewModels {
        object: ViewModelProvider.Factory{
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val brokerDAO: BrokerDAO =
                    AppDatabase.getInstance(requireContext()).brokerDAO

                val repository: BrokerRepository = BrokerRepositoryImpl(brokerDAO)
                return BrokerListViewModel(repository) as T
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModelEvents()
        configureViewListeners()
    }

    override fun onResume() {
        super.onResume()

        viewModel.getBrokers()
    }

    private fun observeViewModelEvents() {
        viewModel.allBrokersEvent.observe(viewLifecycleOwner) {
            val brokerListAdapter = BrokerListAdapter(it).apply {
                onItemClick = { broker ->
                    val directions  = BrokerListFragmentDirections
                        .actionBrokerListFragmentToBrokerFragment(broker)

                    findNavController().navigateWithAnimations(directions)

                }

                onDeleteClick = { broker ->

                    AlertDialog.Builder(context)
                        .setTitle("Excluir Corretora")
                        .setMessage("Deseja realmente excluir essa corretora?")
                        .setPositiveButton("Confirmar") { _, _ ->
                            viewModel.deleteBroker(broker)
                            viewModel.getBrokers()
                        }
                        .setNegativeButton("Cancelar"
                        ) { _, _ ->
                            //
                        }
                        .create()
                        .show()


                }
            }

            this.requireView().findViewById<RecyclerView>(R.id.recycler_brokers).run {
                setHasFixedSize(true)
                adapter = brokerListAdapter
            }
        }

        viewModel.messageStateEventData.observe(viewLifecycleOwner){ stringResId ->
            Snackbar.make(requireView(), stringResId, Snackbar.LENGTH_LONG).show()
        }
    }

    private fun configureViewListeners() {
        this.requireView().findViewById<FloatingActionButton>(R.id.faAddBroker).setOnClickListener {
            findNavController().navigateWithAnimations(R.id.action_brokerListFragment_to_brokerFragment)
        }


    }




}