package com.samuel.myholderwallet.repository

import androidx.lifecycle.LiveData
import com.samuel.myholderwallet.db.entity.PaperEntity

interface PaperRepository {

    suspend fun get(id: Long): PaperEntity

    suspend fun getAll(): List<PaperEntity>

    suspend fun loadAllByIds(papersIds: LongArray): List<PaperEntity>

    suspend fun insertAll(vararg papers: PaperEntity)

    suspend fun insert(paper: PaperEntity): Long

    suspend fun update(paper: PaperEntity)

    suspend fun delete(paper: PaperEntity)
}