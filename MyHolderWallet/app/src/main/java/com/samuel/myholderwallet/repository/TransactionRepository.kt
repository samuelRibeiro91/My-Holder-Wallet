package com.samuel.myholderwallet.repository

import com.samuel.myholderwallet.db.entity.TransactionEntity

interface TransactionRepository {
    suspend fun get(id: Long): TransactionEntity

    suspend fun getAll(): List<TransactionEntity>

    suspend fun loadAllByIds(transactionsIds: LongArray): List<TransactionEntity>

    suspend fun insertAll(vararg transactions: TransactionEntity)

    suspend fun insert(transaction: TransactionEntity): Long

    suspend fun update(transaction: TransactionEntity)

    suspend fun delete(transaction: TransactionEntity)
}