package com.samuel.myholderwallet.db.dao

import androidx.room.*
import com.samuel.myholderwallet.db.entity.PaperEntity

@Dao
interface PaperDAO {
    @Query("SELECT * FROM paper where id = :id")
    suspend fun get(id: Long): PaperEntity

    @Query("SELECT * FROM paper")
    suspend fun getAll(): List<PaperEntity>

    @Query("SELECT * FROM paper WHERE id IN (:papersIds)")
    suspend fun loadAllByIds(papersIds: LongArray): List<PaperEntity>

    @Insert
    suspend fun insertAll(vararg papers: PaperEntity)

    @Insert
    suspend fun insert(paper: PaperEntity): Long

    @Update
    suspend fun update(paper: PaperEntity)

    @Delete
    suspend fun delete(paper: PaperEntity)
}