package com.samuel.myholderwallet.ui.transaction

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.samuel.myholderwallet.db.dao.TransactionDAO
import com.samuel.myholderwallet.R
import com.samuel.myholderwallet.db.AppDatabase
import com.samuel.myholderwallet.db.dao.BrokerDAO
import com.samuel.myholderwallet.db.dao.PaperDAO
import com.samuel.myholderwallet.db.dao.WalletDAO
import com.samuel.myholderwallet.extension.hideKeyboard
import com.samuel.myholderwallet.extension.transformIntoDatePicker
import com.samuel.myholderwallet.repository.*
import com.samuel.myholderwallet.types.MovementTypes
import com.samuel.myholderwallet.usecases.TransactionCreditsValidateUseCase
import java.text.SimpleDateFormat
import java.util.*


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

                val walletDAO : WalletDAO = AppDatabase.getInstance(requireContext()).walletDAO
                val walletRepository: WalletRepository = WalletRepositoryImpl(walletDAO)

                val transactionCreditsValidateUseCase = TransactionCreditsValidateUseCase(walletRepository, requireContext())

                return TransactionViewModel(
                    transactionRepository = transactionRepository,
                    paperRepository = paperRepository,
                    brokerRepository = brokerRepository,
                    transactionCreditsValidateUseCase = transactionCreditsValidateUseCase) as T
            }
        }
    }


    override fun onResume() {
        super.onResume()
        viewModel.getBrokers()
        viewModel.getPapers()
    }

    private val args: TransactionFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModelEvents()

        configureViewListeners()

        observeEvents()

        requireView().findViewById<Spinner>(R.id.spinner_transaction_type).adapter =  ArrayAdapter(requireContext(), R.layout.spinneritem, R.id.spinnerText, MovementTypes.values())


        args.transaction?.let { transactionEntity ->
            //Broker
            viewModel.allBrokersEvent.value?.forEach { brokerEntity ->
                if (brokerEntity.id == transactionEntity.fk_broker){
                    requireView().findViewById<Spinner>(R.id.spinner_broker).setSelection(viewModel.allBrokersEvent.value!!.indexOf(brokerEntity))
                }
            }
            requireView().findViewById<Spinner>(R.id.spinner_broker).isEnabled = false

            requireView().findViewById<Spinner>(R.id.spinner_transaction_type).setSelection(transactionEntity.type!!.ordinal)
            requireView().findViewById<Spinner>(R.id.spinner_transaction_type).isEnabled = false

            //Paper
            viewModel.allPapersEvent.value?.forEach { paperEntity ->
                if (paperEntity.id == transactionEntity.fk_paper){
                    requireView().findViewById<Spinner>(R.id.spinner_paper).setSelection(viewModel.allPapersEvent.value!!.indexOf(paperEntity))
                }
            }

            viewModel.oldValue.value = transactionEntity.value
            viewModel.oldQuantity.value = transactionEntity.quantity

            requireView().findViewById<TextInputEditText>(R.id.input_quantity)  .setText(transactionEntity.quantity.toString())
            requireView().findViewById<TextInputEditText>(R.id.input_value)     .setText(transactionEntity.value.toString())
            requireView().findViewById<TextInputEditText>(R.id.input_costs)     .setText(transactionEntity.cost.toString())

            //Date
            requireView().findViewById<TextInputEditText>(R.id.input_date)     .setText(SimpleDateFormat("dd/MM/yyyy").format(Date(transactionEntity.date.toLong())))
        }

    }

    private fun observeViewModelEvents() {
        viewModel.allBrokersEvent.observe(viewLifecycleOwner) { list ->
            requireView().findViewById<Spinner>(R.id.spinner_broker).adapter =  ArrayAdapter(requireContext(), R.layout.spinneritem, R.id.spinnerText, list)

            args.transaction?.let { transactionEntity ->
                //Broker
                viewModel.allBrokersEvent.value?.forEach { brokerEntity ->
                    if (brokerEntity.id == transactionEntity.fk_broker) {
                        requireView().findViewById<Spinner>(R.id.spinner_broker)
                            .setSelection(viewModel.allBrokersEvent.value!!.indexOf(brokerEntity))
                    }
                }
                requireView().findViewById<Spinner>(R.id.spinner_broker).isEnabled = false
            }

        }

        viewModel.allPapersEvent.observe(viewLifecycleOwner){ list ->
            requireView().findViewById<Spinner>(R.id.spinner_paper).adapter =  ArrayAdapter(requireContext(), R.layout.spinneritem, R.id.spinnerText, list)

            args.transaction?.let { transactionEntity ->
                viewModel.allPapersEvent.value?.forEach { paperEntity ->
                    if (paperEntity.id == transactionEntity.fk_paper){
                        requireView().findViewById<Spinner>(R.id.spinner_paper).setSelection(viewModel.allPapersEvent.value!!.indexOf(paperEntity))
                    }
                }
            }
        }

    }

    private fun configureViewListeners() {

        requireView().findViewById<TextInputEditText>(R.id.input_date).transformIntoDatePicker(requireContext(), "dd/MM/yyyy", Date())


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

                viewModel.movementTypeSelected.postValue(MovementTypes.values()[position])

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

        requireView().findViewById<FloatingActionButton>(R.id.faAddTransaction).setOnClickListener {

            //Validations
            var error = false

            val inputValue = requireView().findViewById<TextInputEditText>(R.id.input_value)
            var value = 0.0f

            if (inputValue.text!!.isEmpty()){
                inputValue.requestFocus()
                inputValue.error = "Preencha o Valor"

                error = true
            }
            else
            {
                if (inputValue.text!!.toString().toFloat() <= 0){
                    inputValue.requestFocus()
                    inputValue.error = "Valor não pode ser menor ou igual a zero!"

                    error = true
                }


                value = inputValue.text!!.toString().toFloat()
            }

            val inputDate = requireView().findViewById<TextInputEditText>(R.id.input_date)
            var date = 0.0

            if (inputDate.text!!.isEmpty()){
                inputDate.requestFocus()
                inputDate.error = "Preencha a Data"

                error = true //SimpleDateFormat("dd/MM/yyyy").parse(requireView().findViewById<TextInputEditText>(R.id.input_date).text.toString()).time.toDouble()
            }
            else
            {
                date = SimpleDateFormat("dd/MM/yyyy").parse(inputDate.text.toString()).time.toDouble()
            }


            val inputQuantity = requireView().findViewById<TextInputEditText>(R.id.input_quantity)
            var quantity = 1

            val inputCost = requireView().findViewById<TextInputEditText>(R.id.input_costs)
            var cost = 0.0f

            when (viewModel.movementTypeSelected.value!!){
                MovementTypes.MONEY_DEPOSIT,
                MovementTypes.INFLOW_DIVIDENDS,
                MovementTypes.CASH_WITHDRAWAL -> {

                    cost = 0.0f
                    quantity = 1
                }
                MovementTypes.BUY_PAPERS,
                MovementTypes.SELL_PAPERS -> {

                    if (inputQuantity.text!!.isEmpty()){
                        inputQuantity.requestFocus()
                        inputQuantity.error = "Preencha a Quantidade"
                        error = true
                    }
                    else
                    {
                      if (inputQuantity.text!!.toString().toInt() <= 0){
                          inputQuantity.requestFocus()
                          inputQuantity.error = "Quantidade não pode ser menor ou igual a zero!"

                          error = true
                      }
                      quantity = inputQuantity.text!!.toString().toInt()
                    }


                    if (inputCost.text!!.isEmpty()){
                        inputCost.requestFocus()
                        inputCost.error = "Preencha o Custo"
                        error = true
                    }
                    else
                    {
                        if (inputCost.text!!.toString().toFloat() < 0){
                            inputCost.requestFocus()
                            inputCost.error = "Custo não pode ser menor que zero!"

                            error = true
                        }
                        cost = inputCost.text!!.toString().toFloat()
                    }


                }



            }


            if (!error) {
                viewModel.insertOrUpdateTransaction(
                    id =       args.transaction?.id ?: 0,
                    quantity = quantity,
                    value =    value,
                    cost =     cost,
                    type =     viewModel.movementTypeSelected.value!!,
                    date =     date
                )
            }
        }
    }

    private fun observeEvents() {
        viewModel.transactionStateEventData.observe(viewLifecycleOwner){ transactionEvent ->
            when (transactionEvent){
                is TransactionViewModel.TransactionState.Inserted ->{
                    clearFields()
                    hideKeyBoard()
                    requireView().requestFocus()

                    findNavController().popBackStack()

                }

                is TransactionViewModel.TransactionState.Updated ->{
                    clearFields()
                    hideKeyBoard()

                    findNavController().popBackStack()
                }
            }

        }

        viewModel.messageStateEventData.observe(viewLifecycleOwner){ stringResId ->
            Snackbar.make(requireView(), stringResId, Snackbar.LENGTH_LONG).show()
        }

        viewModel.errorMessageEventData.observe(viewLifecycleOwner){ message ->
            AlertDialog.Builder(context)
                .setTitle("Erro")
                .setMessage(message)
                .setPositiveButton("Ok") { _, _ ->
                    //
                }
                .create()
                .show()

        }
    }


    private fun clearFields() {
        requireView().findViewById<TextInputEditText>(R.id.input_quantity)?.text?.clear()
        requireView().findViewById<TextInputEditText>(R.id.input_value)?.text?.clear()
        requireView().findViewById<TextInputEditText>(R.id.input_costs)?.text?.clear()
    }

    private fun hideKeyBoard() {
        val parentActivity = requireActivity()

        if (parentActivity is AppCompatActivity){
            parentActivity.hideKeyboard()
        }
    }

}