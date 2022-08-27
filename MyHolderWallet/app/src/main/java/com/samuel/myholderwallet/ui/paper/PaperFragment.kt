package com.samuel.myholderwallet.ui.paper

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
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
import com.samuel.myholderwallet.types.PaperType

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

        requireView().findViewById<Spinner>(R.id.spinner_paper).adapter =  ArrayAdapter(requireContext(), R.layout.spinneritem, R.id.spinnerText, PaperType.values())

        args.paper?.let { paperEntity ->
            requireView().findViewById<TextInputEditText>(R.id.input_initial).setText(paperEntity.initial)
            requireView().findViewById<TextInputEditText>(R.id.input_description).setText(paperEntity.description)

            requireView().findViewById<Spinner>(R.id.spinner_paper).setSelection(paperEntity.type.ordinal)
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
            val initial      = requireView().findViewById<TextInputEditText>(R.id.input_initial)?.text.toString()
            val description  = requireView().findViewById<TextInputEditText>(R.id.input_description)?.text.toString()
            val type         = PaperType.values()[requireView().findViewById<Spinner>(R.id.spinner_paper).selectedItemPosition]

            if (initial.isEmpty()){
                requireView().findViewById<TextInputEditText>(R.id.input_initial).requestFocus()
                requireView().findViewById<TextInputEditText>(R.id.input_initial).error = "Preencha a Sigla"
            }
            else
            if (description.isEmpty()){
                requireView().findViewById<TextInputEditText>(R.id.input_description).requestFocus()
                requireView().findViewById<TextInputEditText>(R.id.input_description).error = "Preencha a descrição"
            }
            else
            {
                viewModel.addOrUpdatePaper(initial = initial, description = description, type = type, id =args.paper?.id ?: 0)
            }
        }
    }


}