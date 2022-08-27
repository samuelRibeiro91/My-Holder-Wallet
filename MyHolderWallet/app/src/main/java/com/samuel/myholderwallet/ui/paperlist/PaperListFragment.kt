package com.samuel.myholderwallet.ui.paperlist

import android.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.samuel.myholderwallet.R
import com.samuel.myholderwallet.db.AppDatabase
import com.samuel.myholderwallet.db.dao.PaperDAO
import com.samuel.myholderwallet.extension.navigateWithAnimations
import com.samuel.myholderwallet.repository.PaperRepository
import com.samuel.myholderwallet.repository.PaperRepositoryImpl

class PaperListFragment : Fragment(R.layout.fragment_paper_list) {
    
    private val viewModel: PaperListViewModel by viewModels {
        object: ViewModelProvider.Factory{
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val paperDAO: PaperDAO =
                    AppDatabase.getInstance(requireContext()).paperDAO

                val repository: PaperRepository = PaperRepositoryImpl(paperDAO)
                return PaperListViewModel(repository) as T
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

        viewModel.getPapers()
    }

    private fun observeViewModelEvents() {
        viewModel.allPapersEvent.observe(viewLifecycleOwner) {
            val paperListAdapter = PaperListAdapter(it).apply {
                onItemClick = { paper ->
                    val directions  = PaperListFragmentDirections
                        .actionPaperListFragmentToPaperFragment(paper)

                    findNavController().navigateWithAnimations(directions)

                }
                onDeleteClick = { paper ->

                    AlertDialog.Builder(context)
                        .setTitle("Excluir Papel")
                        .setMessage("Deseja realmente excluir esse papel?")
                        .setPositiveButton("Confirmar") { _, _ ->
                            viewModel.deletePaper(paper)
                            viewModel.getPapers()
                        }
                        .setNegativeButton("Cancelar"
                        ) { _, _ ->
                            //
                        }
                        .create()
                        .show()

                }
            }

            this.requireView().findViewById<RecyclerView>(R.id.recycler_papers).run {
                setHasFixedSize(true)
                adapter = paperListAdapter
            }
        }

        viewModel.messageStateEventData.observe(viewLifecycleOwner){ stringResId ->
            Snackbar.make(requireView(), stringResId, Snackbar.LENGTH_LONG).show()
        }
    }

    private fun configureViewListeners() {
        this.requireView().findViewById<FloatingActionButton>(R.id.faAddPaper).setOnClickListener {
            findNavController().navigateWithAnimations(R.id.action_paperListFragment_to_paperFragment)
        }


    }


}