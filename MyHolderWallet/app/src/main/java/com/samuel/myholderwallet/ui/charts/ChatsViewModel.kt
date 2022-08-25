package com.samuel.myholderwallet.ui.charts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samuel.myholderwallet.db.entity.BrokerEntity
import com.samuel.myholderwallet.db.wrapper.PaperValueWrapperEntity
import com.samuel.myholderwallet.repository.BrokerRepository
import com.samuel.myholderwallet.repository.TransactionRepository
import kotlinx.coroutines.launch

class ChatsViewModel(
    private val brokerRepository: BrokerRepository,
    private val transactionRepository: TransactionRepository
) : ViewModel() {
    private val _allBrokersEvent = MutableLiveData<List<BrokerEntity>>()

    val allBrokersEvent: LiveData<List<BrokerEntity>>
        get() = _allBrokersEvent

    var brokerSelected = MutableLiveData<BrokerEntity>()

    private val _accountBalance = MutableLiveData<Float>()

    val accountBalance: LiveData<Float>
        get() = _accountBalance


    private val _totalStockValue = MutableLiveData<Float>()

    val totalStockValue: LiveData<Float>
        get() = _totalStockValue

    private val _totalReitsValue = MutableLiveData<Float>()

    val totalReitsValue: LiveData<Float>
        get() = _totalReitsValue

    private val _totalAdrsValue = MutableLiveData<Float>()

    val totalAdrsValue: LiveData<Float>
        get() = _totalAdrsValue

    private val _totalValue = MutableLiveData<Float>()

    val totalValue: LiveData<Float>
      get() = _totalValue

    private val _stockWithValues = MutableLiveData<List<PaperValueWrapperEntity>>()

    val stockWithValues: LiveData<List<PaperValueWrapperEntity>>
       get() = _stockWithValues

    private val _reitWithValues = MutableLiveData<List<PaperValueWrapperEntity>>()

    val reitWithValues: LiveData<List<PaperValueWrapperEntity>>
        get() = _reitWithValues

    private val _adrWithValues = MutableLiveData<List<PaperValueWrapperEntity>>()

    val adrWithValues: LiveData<List<PaperValueWrapperEntity>>
        get() = _adrWithValues

    fun getBrokers() = viewModelScope.launch {
        _allBrokersEvent.postValue(brokerRepository.getAll())
    }

    fun getData() = viewModelScope.launch {
        val accountBalance = transactionRepository.getAccountBalanceByBroker(brokerSelected.value!!.id)
        val totalAdrs      = transactionRepository.getTotalAdrsByBroker(brokerSelected.value!!.id)
        val totalStock     = transactionRepository.getTotalStockByBroker(brokerSelected.value!!.id)
        val totalReits     = transactionRepository.getTotalReitsByBroker(brokerSelected.value!!.id)

        _adrWithValues  .postValue(transactionRepository.getAdrsWithValues(brokerSelected.value!!.id))
        _stockWithValues.postValue(transactionRepository.getStocksWithValues(brokerSelected.value!!.id))
        _reitWithValues .postValue(transactionRepository.getReitsWithValues(brokerSelected.value!!.id))

        _accountBalance.postValue(transactionRepository.getAccountBalanceByBroker(brokerSelected.value!!.id))

        _totalAdrsValue.postValue(totalAdrs ?: 0.0f)

        _totalStockValue.postValue(totalStock ?: 0.0f)

        _totalReitsValue.postValue(totalReits ?: 0.0f)

        _totalValue.postValue((accountBalance ?: 0.0f ) + (totalStock ?: 0.0f )+ (totalAdrs ?: 0.0f ) + (totalReits ?: 0.0f ))
    }

}