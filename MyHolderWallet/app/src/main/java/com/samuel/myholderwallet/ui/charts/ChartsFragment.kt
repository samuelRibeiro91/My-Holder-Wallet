package com.samuel.myholderwallet.ui.charts

import android.graphics.Color
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
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.Legend.LegendForm
import com.github.mikephil.charting.components.LegendEntry
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.DefaultValueFormatter
import com.github.mikephil.charting.formatter.PercentFormatter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.samuel.myholderwallet.db.dao.TransactionDAO
import com.samuel.myholderwallet.R
import com.samuel.myholderwallet.db.AppDatabase
import com.samuel.myholderwallet.db.dao.BrokerDAO
import com.samuel.myholderwallet.db.wrapper.DataTransactionsWrapperEntity
import com.samuel.myholderwallet.db.wrapper.PaperValueWrapperEntity
import com.samuel.myholderwallet.extension.navigateWithAnimations
import com.samuel.myholderwallet.repository.BrokerRepository
import com.samuel.myholderwallet.repository.BrokerRepositoryImpl
import com.samuel.myholderwallet.repository.TransactionRepository
import com.samuel.myholderwallet.repository.TransactionRepositoryImpl
import java.lang.Float.NaN
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

        viewModel.dividendValues.observe(viewLifecycleOwner){
            val colors = ArrayList<Int>()
            colors.add(Color.parseColor("#81c784"))
            colors.add(Color.parseColor("#4dd0e1"))
            colors.add(Color.parseColor("#673ab7"))
            colors.add(Color.parseColor("#3f51b5"))
            colors.add(Color.parseColor("#b71c1c"))
            colors.add(Color.parseColor("#880e4f"))

            if (it.isNotEmpty())
                setDividendsChart(it, requireView().findViewById(R.id.cht_dividends), colors)
            else
                requireView().findViewById<BarChart>(R.id.cht_dividends).setNoDataText(getString(R.string.sem_dados_disponiveis))
        }

        viewModel.buysAndUsedDividends.observe(viewLifecycleOwner){
            val colors = ArrayList<Int>()
            colors.add(Color.parseColor("#880e4f"))
            colors.add(Color.parseColor("#b71c1c"))

            if (it.isNotEmpty())
                setBuysAndUsedDividendsChart(it, requireView().findViewById(R.id.cht_buy_and_dividends), colors)
            else
                requireView().findViewById<BarChart>(R.id.cht_buy_and_dividends).setNoDataText(getString(R.string.sem_dados_disponiveis))
        }

        viewModel.stockWithValues.observe(viewLifecycleOwner){
            val colors = ArrayList<Int>()

            colors.add(Color.parseColor("#673ab7"))
            colors.add(Color.parseColor("#1be7ff"))
            colors.add(Color.parseColor("#cddc39"))
            colors.add(Color.parseColor("#ff5722"))
            colors.add(Color.parseColor("#00bcd4"))
            colors.add(Color.parseColor("#009688"))
            colors.add(Color.parseColor("#4caf50"))
            colors.add(Color.parseColor("#8bc34a"))
            colors.add(Color.parseColor("#aed581"))
            colors.add(Color.parseColor("#ff5722"))

            if (it.isNotEmpty())
                setPaperChart(it, requireView().findViewById(R.id.cht_total_stock), colors)
            else
                requireView().findViewById<PieChart>(R.id.cht_total_stock).setNoDataText(getString(R.string.sem_dados_disponiveis))
        }

        viewModel.reitWithValues.observe(viewLifecycleOwner){
            val colors = ArrayList<Int>()

            colors.add(Color.parseColor("#90a4ae"))
            colors.add(Color.parseColor("#4db6ac"))
            colors.add(Color.parseColor("#81c784"))
            colors.add(Color.parseColor("#4dd0e1"))
            colors.add(Color.parseColor("#a1887f"))
            colors.add(Color.parseColor("#fff176"))
            colors.add(Color.parseColor("#aed581"))
            colors.add(Color.parseColor("#ffd54f"))
            colors.add(Color.parseColor("#81c784"))
            colors.add(Color.parseColor("#4dd0e1"))


            if (it.isNotEmpty())
                setPaperChart(it, requireView().findViewById(R.id.cht_total_reit), colors)
            else
                requireView().findViewById<PieChart>(R.id.cht_total_reit).setNoDataText(getString(R.string.sem_dados_disponiveis))
        }

        viewModel.adrWithValues.observe(viewLifecycleOwner){
            val colors = ArrayList<Int>()

            colors.add(Color.parseColor("#b71c1c"))
            colors.add(Color.parseColor("#880e4f"))
            colors.add(Color.parseColor("#33691e"))
            colors.add(Color.parseColor("#311b92"))
            colors.add(Color.parseColor("#01579b"))
            colors.add(Color.parseColor("#006064"))
            colors.add(Color.parseColor("#304ffe"))
            colors.add(Color.parseColor("#004d40"))
            colors.add(Color.parseColor("#827717"))
            colors.add(Color.parseColor("#33691e"))

            if (it.isNotEmpty())
                setPaperChart(it, requireView().findViewById(R.id.cht_total_adr), colors)
            else
                requireView().findViewById<PieChart>(R.id.cht_total_adr).setNoDataText(getString(R.string.sem_dados_disponiveis))
        }

    }

    private fun setBuysAndUsedDividendsChart(list: List<DataTransactionsWrapperEntity>?,  barChart: BarChart, colors: ArrayList<Int>) {
        val entriesBuy       = ArrayList<BarEntry>()
        val entriesDividends = ArrayList<BarEntry>()

        var x = 0
        list!!.forEach {
            entriesBuy.add(BarEntry(x.toFloat(), it.value))

            entriesDividends.add(BarEntry((x).toFloat(), it.credits))

            x += 3
        }

        val dataSetBuy = BarDataSet( entriesBuy, "")
        dataSetBuy.color = colors[0]
        dataSetBuy.valueTextColor = Color.WHITE
        dataSetBuy.valueFormatter = DefaultValueFormatter(2)

        val dataSetDividends = BarDataSet( entriesDividends, "")
        dataSetDividends.color = colors[1]
        dataSetDividends.valueTextColor = Color.WHITE
        dataSetDividends.setDrawValues(true)
        dataSetDividends.valueFormatter = DefaultValueFormatter(2)

        barChart.description.isEnabled = false
        barChart.description.textColor = Color.WHITE

        val barData = BarData()
        barData.setValueTextSize(10f)
        barData.barWidth = 0.9f

        barData.addDataSet(dataSetBuy)
        barData.addDataSet(dataSetDividends)

        barData.addDataSet(dataSetDividends)

        barChart.data = barData

        barChart.setDrawBarShadow(false)
        barChart.setDrawValueAboveBar(true)

        barChart.description.isEnabled = false

        barChart.setMaxVisibleValueCount(60)

        barChart.setPinchZoom(false)

        barChart.setDrawGridBackground(false)
        barChart.setDrawMarkers(false)

        val l: Legend = barChart.legend
        l.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
        l.orientation = Legend.LegendOrientation.HORIZONTAL
        l.setDrawInside(false)
        l.form = LegendForm.SQUARE
        l.textColor = Color.WHITE
        l.formSize = 7f
        l.textSize = 7f
        l.xEntrySpace = 10f
        l.yEntrySpace = 10f

        val legends = ArrayList<LegendEntry>()

        legends.add(LegendEntry("Compras", LegendForm.DEFAULT, NaN, NaN, null, colors[0]))
        legends.add(LegendEntry("Dividendos", LegendForm.DEFAULT, NaN, NaN, null, colors[1]))

        l.setCustom(legends)

        barChart.setDrawBorders(false)

        barChart.setBorderColor(Color.WHITE)
        barChart.setFitBars(false)
        barChart.setScaleEnabled(false)

        barChart.data.setValueTextColor(Color.WHITE)

        barChart.invalidate()
        barChart.notifyDataSetChanged()
    }

    private fun setDividendsChart(list: List<DataTransactionsWrapperEntity>?, barChart: BarChart, colors: ArrayList<Int>){
        val entries = ArrayList<BarEntry>()

        var x = 0
        list!!.forEach {
            entries.add(BarEntry(x.toFloat(), it.value, it.date))

            x += 3
        }


        val dataSet = BarDataSet( entries, "")

        dataSet.colors = colors
        dataSet.valueTextColor = Color.WHITE
        dataSet.valueFormatter = DefaultValueFormatter(2)

        barChart.description.isEnabled = false
        barChart.description.textColor = Color.WHITE

        val barData = BarData()
        barData.setValueTextSize(10f)
        barData.barWidth = 0.9f

        barData.addDataSet(dataSet)

        barChart.data = barData

        barChart.setDrawBarShadow(false)
        barChart.setDrawValueAboveBar(true)

        barChart.description.isEnabled = false

        barChart.setMaxVisibleValueCount(60)

        barChart.setPinchZoom(false)

        barChart.setDrawGridBackground(false)
        barChart.setDrawMarkers(false)

        val l: Legend = barChart.legend
        l.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
        l.orientation = Legend.LegendOrientation.HORIZONTAL
        l.setDrawInside(false)
        l.form = LegendForm.SQUARE
        l.textColor = Color.WHITE
        l.formSize = 7f
        l.textSize = 7f
        l.xEntrySpace = 10f
        l.yEntrySpace = 10f


        val legends = ArrayList<LegendEntry>()

        var int = 0

        list.forEach {
            legends.add(LegendEntry(it.date, LegendForm.DEFAULT, NaN, NaN, null, colors[int]))

            int += 1
        }


        l.setCustom(legends)//setEntries()

        barChart.setDrawBorders(false)

        barChart.setBorderColor(Color.WHITE)
        barChart.setFitBars(false)
        barChart.setScaleEnabled(false)

        barChart.data.setValueTextColor(Color.WHITE)

        barChart.invalidate()
        barChart.notifyDataSetChanged()
    }

    private fun setPaperChart(list: List<PaperValueWrapperEntity>?, pieChart: PieChart, colors: ArrayList<Int>) {
        val entries = ArrayList<PieEntry>()


        if (list!!.size <=5){
            list.forEach {
                if (it.value > 0.0f)
                    entries.add(PieEntry(it.value, it.initial))
            }
        }
        else
        {
            var count = 0
            var othersValue = 0.0f

            list!!.forEach {
                if ((it.value > 0.0f) && (count < 5)) {
                    entries.add(PieEntry(it.value, it.initial))
                    count += 1
                } else
                {
                    othersValue = it.value
                }
            }

            if (othersValue > 0){
                entries.add(PieEntry(othersValue, "Outros"))
            }
        }


        val dataSet = PieDataSet( entries, "")

        dataSet.colors = colors

        pieChart.description.isEnabled = false
        pieChart.description.textColor = Color.WHITE

        val pieData = PieData(dataSet)

        val formart = PercentFormatter()
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
        pieChart.setHoleColor(Color.TRANSPARENT)

        pieChart.setTransparentCircleColor(Color.WHITE)
        pieChart.setTransparentCircleAlpha(110)


        pieChart.setEntryLabelColor(Color.WHITE)
        pieChart.setEntryLabelTextSize(14f)

        pieChart.invalidate()
        pieChart.notifyDataSetChanged()
    }

    private fun setInvestmentsChart(){
        val pieChart = requireView().findViewById<PieChart>(R.id.cht_total_by_types)

        val entries = ArrayList<PieEntry>()

        if  ((viewModel.totalStockValue.value ?: 0.0f) != 0.0f) entries.add(PieEntry(viewModel.totalStockValue.value  ?: 0.0f, "Ações"))
        if  ((viewModel.totalReitsValue.value ?: 0.0f) != 0.0f) entries.add(PieEntry(viewModel.totalReitsValue.value  ?: 0.0f, "FIIs"))
        if  ((viewModel.totalAdrsValue.value ?: 0.0f) != 0.0f) entries.add(PieEntry(viewModel.totalAdrsValue.value  ?: 0.0f, "Bdrs"))

        val dataSet = PieDataSet( entries, "")

        val colors = ArrayList<Int>()

        colors.add(Color.parseColor("#6eeb83"))
        colors.add(Color.parseColor("#1be7ff"))
        colors.add(Color.parseColor("#f49f0a"))

        dataSet.colors = colors

        pieChart.description.isEnabled = false

        val pieData = PieData(dataSet)

        val formart = PercentFormatter()
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
        pieChart.setHoleColor(Color.TRANSPARENT)

        pieChart.setTransparentCircleColor(Color.WHITE)
        pieChart.setTransparentCircleAlpha(110)


        pieChart.setEntryLabelColor(Color.WHITE)
        pieChart.setEntryLabelTextSize(14f)

        pieChart.invalidate()
        pieChart.notifyDataSetChanged()
    }

    private fun configureViewListeners() {
        requireView().findViewById<FloatingActionButton>(R.id.faOpenOptions).setOnClickListener {
            findNavController().navigateWithAnimations(R.id.action_chatsFragment_to_optionsFragment)
        }

        requireView().findViewById<Spinner>(R.id.spinner_broker).onItemSelectedListener = object  : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {  /*Somente para compatibilidade*/  }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                viewModel.brokerSelected.postValue(viewModel.allBrokersEvent.value?.get(position) ?: null)
            }
        }
    }

}