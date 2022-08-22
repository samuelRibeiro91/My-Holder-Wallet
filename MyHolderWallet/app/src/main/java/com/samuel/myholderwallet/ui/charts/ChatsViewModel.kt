package com.samuel.myholderwallet.ui.charts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samuel.myholderwallet.db.entity.BrokerEntity
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

    fun getBrokers() = viewModelScope.launch {
        _allBrokersEvent.postValue(brokerRepository.getAll())
    }

    fun getData() = viewModelScope.launch {
        _accountBalance.postValue(transactionRepository.getAccountBalanceByBroker(brokerSelected.value!!.id))

        _totalStockValue.postValue(transactionRepository.getTotalStockByBroker(brokerSelected.value!!.id))

        _totalReitsValue.postValue(transactionRepository.getTotalReitsByBroker(brokerSelected.value!!.id))

        _totalValue.postValue(_accountBalance.value!! + _totalStockValue.value!! + _totalStockValue.value!! + _totalReitsValue.value!!)
    }

}