package com.samuel.myholderwallet.ui.charts

import android.R.attr.data
import android.graphics.Color
import android.graphics.Typeface.NORMAL
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
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
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
import java.text.DecimalFormat


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

            setInvestmentsChart()
        }

    }

    private fun setInvestmentsChart(){
        val pieChart = requireView().findViewById<PieChart>(R.id.cht_total_by_types)

        val entries = ArrayList<PieEntry>()

        if  (viewModel.totalStockValue.value  ?: 0.0f != 0.0f) entries.add(PieEntry(viewModel.totalStockValue.value  ?: 0.0f, "Ações"))
        if  (viewModel.totalReitsValue.value  ?: 0.0f != 0.0f) entries.add(PieEntry(viewModel.totalReitsValue.value  ?: 0.0f, "FIIs"))
        if  (viewModel.totalAdrsValue.value  ?: 0.0f != 0.0f) entries.add(PieEntry(viewModel.totalAdrsValue.value  ?: 0.0f, "Bdrs"))

        val dataSet = PieDataSet( entries, "")

        val colors = ArrayList<Int>()

        colors.add(Color.parseColor("#6eeb83"))
        colors.add(Color.parseColor("#1be7ff"))
        colors.add(Color.parseColor("#f49f0a"))

        dataSet.colors = colors

        pieChart.description.isEnabled = false

        val pieData = PieData(dataSet)

        var formart = PercentFormatter()
        formart.mFormat = DecimalFormat("###,###,##0.00")

        pieData.setValueFormatter(formart)
        pieChart.data = pieData


        val l: Legend = pieChart.legend
        l.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
        l.orientation = Legend.LegendOrientation.VERTICAL
        l.setDrawInside(false)
        l.textColor = Color.WHITE
        l.xEntrySpace = 7f
        l.yEntrySpace = 0f
        l.yOffset = 0f

        pieChart.holeRadius = 58f
        pieChart.transparentCircleRadius =61f

        pieChart.setUsePercentValues(true)

        pieChart.isDrawHoleEnabled =true
        pieChart.setHoleColor(Color.TRANSPARENT);

        pieChart.setTransparentCircleColor(Color.WHITE)
        pieChart.setTransparentCircleAlpha(110)


        pieChart.setEntryLabelColor(Color.BLACK)
        pieChart.setEntryLabelTextSize(12f)

        pieChart.invalidate()
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