package com.samuel.myholderwallet.ui.paper

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.samuel.myholderwallet.db.dao.PaperDAO
import com.samuel.myholderwallet.extension.hideKeyboard
import com.samuel.myholderwallet.repository.PaperRepository
import com.samuel.myholderwallet.repository.PaperRepositoryImpl
import com.samuel.myholderwallet.ui.broker.BrokerViewModel

class PaperFragment : Fragment(R.layout.fragment_paper) {

    private val viewModel: PaperViewModel by viewModels {
        object: ViewModelProvider.Factory{
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val paperDAO: PaperDAO =
                    AppDatabase.getInstance(requireContext()).paperDAO

                val repository: PaperRepository = PaperRepositoryImpl(paperDAO)
                return PaperViewModel(repository) as T
            }
        }
    }

    private val args: PaperFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        args.paper?.let { paperEntity ->
            requireView().findViewById<TextInputEditText>(R.id.input_initial).setText(paperEntity.initial)
            requireView().findViewById<TextInputEditText>(R.id.input_description).setText(paperEntity.description)
        }

        observeEvents()
        setListeners()
    }

    private fun observeEvents() {
        viewModel.paperStateEventData.observe(viewLifecycleOwner){ paperEvent ->
            when (paperEvent){
                is PaperViewModel.PaperState.Inserted ->{
                    clearFields()
                    hideKeyBoard()
                    requireView().requestFocus()

                    findNavController().popBackStack()

                }

                is PaperViewModel.PaperState.Updated ->{
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
        requireView().findViewById<TextInputEditText>(R.id.input_initial)?.text?.clear()
        requireView().findViewById<TextInputEditText>(R.id.input_description)?.text?.clear()
    }

    private fun hideKeyBoard() {
        val parentActivity = requireActivity()

        if (parentActivity is AppCompatActivity){
            parentActivity.hideKeyboard()
        }
    }

    private fun setListeners() {
        requireView().findViewById<FloatingActionButton>(R.id.faAddPaper).setOnClickListener {
            val initial  = requireView().findViewById<TextInputEditText>(R.id.input_initial)?.text.toString()
            val description  = requireView().findViewById<TextInputEditText>(R.id.input_description)?.text.toString()
            
            viewModel.addOrUpdatePaper(initial = initial, description = description, args.paper?.id ?: 0)
        }
    }


}