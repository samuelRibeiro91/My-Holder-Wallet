package com.samuel.myholderwallet.ui.paperlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samuel.myholderwallet.db.entity.PaperEntity
import com.samuel.myholderwallet.repository.PaperRepository
import kotlinx.coroutines.launch

class PaperListViewModel(private val repository: PaperRepository): ViewModel() {
    private val _allPapersEvent = MutableLiveData<List<PaperEntity>>()

    val allPapersEvent: LiveData<List<PaperEntity>>
        get() = _allPapersEvent

    fun getPapers() = viewModelScope.launch {
        _allPapersEvent.postValue(repository.getAll())
    }
}