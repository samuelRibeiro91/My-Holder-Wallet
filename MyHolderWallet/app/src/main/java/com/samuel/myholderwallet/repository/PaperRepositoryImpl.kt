package com.samuel.myholderwallet.repository

import androidx.lifecycle.LiveData
import com.samuel.myholderwallet.db.dao.PaperDAO
import com.samuel.myholderwallet.db.entity.PaperEntity

class PaperRepositoryImpl(private val paperDAO: PaperDAO): PaperRepository {
    override suspend fun get(id: Long): PaperEntity
            = paperDAO.get(id)

    override suspend fun getAll(): List<PaperEntity>
            = paperDAO.getAll()

    override suspend fun loadAllByIds(papersIds: LongArray): List<PaperEntity>
            = paperDAO.loadAllByIds(papersIds)

    override suspend fun insertAll(vararg papers: PaperEntity)
            = paperDAO.insertAll(*papers)

    override suspend fun insert(paper: PaperEntity): Long
            = paperDAO.insert(paper)

    override suspend fun update(paper: PaperEntity)
            = paperDAO.update(paper)

    override suspend fun delete(paper: PaperEntity)
            = paperDAO.delete(paper)
}