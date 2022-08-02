package com.samuel.myholderwallet.ui.paper

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samuel.myholderwallet.R
import com.samuel.myholderwallet.db.entity.PaperEntity
import com.samuel.myholderwallet.repository.PaperRepository
import kotlinx.coroutines.launch

class PaperViewModel(
    private val repository: PaperRepository
) : ViewModel() {

    private val _paperStateEventData = MutableLiveData<PaperState>()
    val paperStateEventData : LiveData<PaperState>
        get() = _paperStateEventData

    private val _messageStateEventData = MutableLiveData<Int>()
    val messageStateEventData : LiveData<Int>
        get() = _messageStateEventData


    fun addOrUpdatePaper(initial: String, description: String, id: Long = 0) = viewModelScope.launch {
        if (id > 0){
            updatePaper(id, initial, description)
        }
        else
            insertPaper(initial, description)
    }

    private fun updatePaper(id: Long,initial: String, description: String) = viewModelScope.launch {
        try {
            repository.update(PaperEntity(id, initial, description))

            _paperStateEventData.value = PaperState.Updated
            _messageStateEventData.value = R.string.paper_updated_sucessfully

        } catch (ex: Exception){
            _messageStateEventData.value = R.string.paper_error_to_update
            Log.e(TAG, ex.toString())
        }
    }

    private fun insertPaper(initial: String, description: String) = viewModelScope.launch {
        try {
            val id = repository.insert(PaperEntity(initial = initial, description = description) )

            if (id > 0){
                _paperStateEventData.value = PaperState.Inserted
                _messageStateEventData.value = R.string.paper_inserted_sucessfully
            }

        } catch (ex: Exception){
            _messageStateEventData.value = R.string.paper_error_to_insert
            Log.e(TAG, ex.toString())
        }
    }

    sealed class PaperState{
        object Inserted: PaperState()
        object Updated: PaperState()
    }

    companion object{
        private val TAG = PaperViewModel::class.java.simpleName
    }
}