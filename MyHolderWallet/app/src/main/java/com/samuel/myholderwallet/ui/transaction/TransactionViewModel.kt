package com.samuel.myholderwallet.ui.transaction

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samuel.myholderwallet.R
import com.samuel.myholderwallet.db.entity.BrokerEntity
import com.samuel.myholderwallet.db.entity.PaperEntity
import com.samuel.myholderwallet.db.entity.TransactionEntity
import com.samuel.myholderwallet.repository.BrokerRepository
import com.samuel.myholderwallet.repository.PaperRepository
import com.samuel.myholderwallet.repository.TransactionRepository
import com.samuel.myholderwallet.types.MovementTypes
import com.samuel.myholderwallet.ui.paper.PaperViewModel
import com.samuel.myholderwallet.usecases.TransactionCreditsValidateUseCase
import kotlinx.coroutines.launch

class TransactionViewModel(
    private val transactionRepository: TransactionRepository,
    private val paperRepository: PaperRepository,
    private val brokerRepository: BrokerRepository,
    private val transactionCreditsValidateUseCase: TransactionCreditsValidateUseCase
) : ViewModel() {

    private val _transactionStateEventData = MutableLiveData<TransactionState>()
    val transactionStateEventData : LiveData<TransactionState>
        get() = _transactionStateEventData

    private val _messageStateEventData = MutableLiveData<Int>()
    val messageStateEventData : LiveData<Int>
        get() = _messageStateEventData

    private val _allPapersEvent = MutableLiveData<List<PaperEntity>>()

    val allPapersEvent: LiveData<List<PaperEntity>>
        get() = _allPapersEvent

    private val _allBrokersEvent = MutableLiveData<List<BrokerEntity>>()

    val allBrokersEvent: LiveData<List<BrokerEntity>>
        get() = _allBrokersEvent

    var brokerSelected = MutableLiveData<BrokerEntity>()

    var paperSelected = MutableLiveData<PaperEntity>()

    var movementTypeSelected = MutableLiveData<MovementTypes>()

    var oldValue = MutableLiveData<Float>()

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

    private fun updateTransaction(transactionEntity: TransactionEntity) = viewModelScope.launch{
        try {
            transactionCreditsValidateUseCase.validateTransactionUpdate(transactionEntity, oldValue.value ?: 0.0f)

            transactionRepository.update(transactionEntity)

            _transactionStateEventData.value = TransactionState.Updated
            _messageStateEventData.value = R.string.transaction_updated_sucessfully
        }
        catch (ex: Exception){
            _messageStateEventData.value = R.string.transaction_error_to_update
            Log.e(TAG, ex.toString())
        }
    }

    private fun insertTransaction(transactionEntity: TransactionEntity) = viewModelScope.launch{
        try {
            transactionCreditsValidateUseCase.validateTransactionInsert(transactionEntity)

            transactionRepository.insert( transactionEntity)

            _transactionStateEventData.value = TransactionState.Inserted
            _messageStateEventData.value = R.string.transaction_inserted_sucessfully
        } catch (ex: Exception){
            _messageStateEventData.value = R.string.transaction_error_to_insert
            Log.e(TAG, ex.toString())
        }
    }


    sealed class TransactionState{
        object Inserted: TransactionState()
        object Updated: TransactionState()
    }

    companion object{
        private val TAG = TransactionViewModel::class.java.simpleName
    }
}