package com.samuel.myholderwallet.ui.paperlist

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
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
            }

            this.requireView().findViewById<RecyclerView>(R.id.recycler_papers).run {
                setHasFixedSize(true)
                adapter = paperListAdapter
            }
        }
    }

    private fun configureViewListeners() {
        this.requireView().findViewById<FloatingActionButton>(R.id.faAddPaper).setOnClickListener {
            findNavController().navigateWithAnimations(R.id.action_paperListFragment_to_paperFragment)
        }


    }


}