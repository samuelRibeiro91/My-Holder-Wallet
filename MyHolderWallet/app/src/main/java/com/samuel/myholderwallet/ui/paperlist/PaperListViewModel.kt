package com.samuel.myholderwallet.ui.paperlist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samuel.myholderwallet.R
import com.samuel.myholderwallet.db.entity.PaperEntity
import com.samuel.myholderwallet.repository.PaperRepository
import kotlinx.coroutines.launch

class PaperListViewModel(private val repository: PaperRepository): ViewModel() {
    private val _allPapersEvent = MutableLiveData<List<PaperEntity>>()

    val allPapersEvent: LiveData<List<PaperEntity>>
        get() = _allPapersEvent

    private val _messageStateEventData = MutableLiveData<Int>()
    val messageStateEventData : LiveData<Int>
        get() = _messageStateEventData

    fun getPapers() = viewModelScope.launch {
        _allPapersEvent.postValue(repository.getAll())
    }

    fun deletePaper(paper: PaperEntity?) = viewModelScope.launch {
        try{
            if (paper == null) throw Exception("Necessário selecionar o papel para excluir")

            paper.let {
                repository.delete(paper)
                _messageStateEventData.value = R.string.paper_deleted_sucessfully
            }

        } catch (ex: Exception){
            _messageStateEventData.value = R.string.paper_error_to_delete
            Log.e(TAG, ex.toString())
        }
    }

    companion object{
        private val TAG = PaperListViewModel::class.java.simpleName
    }

}