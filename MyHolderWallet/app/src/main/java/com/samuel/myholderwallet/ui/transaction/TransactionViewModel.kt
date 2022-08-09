package com.samuel.myholderwallet.ui.transaction

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samuel.myholderwallet.db.entity.BrokerEntity
import com.samuel.myholderwallet.db.entity.PaperEntity
import com.samuel.myholderwallet.db.entity.TransactionEntity
import com.samuel.myholderwallet.repository.BrokerRepository
import com.samuel.myholderwallet.repository.PaperRepository
import com.samuel.myholderwallet.repository.TransactionRepository
import com.samuel.myholderwallet.types.MovementTypes
import kotlinx.coroutines.launch

class TransactionViewModel(
    private val transactionRepository: TransactionRepository,
    private val paperRepository: PaperRepository,
    private val brokerRepository: BrokerRepository
) : ViewModel() {
    private val _allPapersEvent = MutableLiveData<List<PaperEntity>>()

    val allPapersEvent: LiveData<List<PaperEntity>>
        get() = _allPapersEvent

    private val _allBrokersEvent = MutableLiveData<List<BrokerEntity>>()

    val allBrokersEvent: LiveData<List<BrokerEntity>>
        get() = _allBrokersEvent

    var brokerSelected = MutableLiveData<BrokerEntity>()

    var paperSelected = MutableLiveData<PaperEntity>()

    fun getPapers() = viewModelScope.launch {
        _allPapersEvent.postValue(paperRepository.getAll())
    }

    fun getBrokers() = viewModelScope.launch {
        _allBrokersEvent.postValue(brokerRepository.getAll())
    }

    fun insertOrUpdateTransaction(id: Long, quantity: Int, value: Float, cost: Float, type: MovementTypes, date: Double){
        if (id > 0){
           updateTransaction(TransactionEntity(
               id = id,
               quantity = quantity,
               value = value,
               cost = cost,
               type = type,
               date = date,
               fk_broker = brokerSelected.value?.id,
               fk_paper =  paperSelected.value?.id
           ))
        }
        else
        {
            insertTransaction(TransactionEntity(
                quantity = quantity,
                value = value,
                cost = cost,
                type = type,
                date = date,
                fk_broker = brokerSelected.value?.id,
                fk_paper =  paperSelected.value?.id
            ))
        }
    }

    private fun updateTransaction(transactionEntity: TransactionEntity){
        //
    }

    private fun insertTransaction(transactionEntity: TransactionEntity){
        //
    }
}