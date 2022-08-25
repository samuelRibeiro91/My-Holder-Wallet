package com.samuel.myholderwallet.repository

import com.samuel.myholderwallet.db.entity.TransactionEntity
import com.samuel.myholderwallet.db.wrapper.PaperValueWrapperEntity

interface TransactionRepository {
    suspend fun get(id: Long): TransactionEntity

    suspend fun getAll(): List<TransactionEntity>

    suspend fun getAllByBroker(brokerID: Long): List<TransactionEntity>

    suspend fun loadAllByIds(transactionsIds: LongArray): List<TransactionEntity>

    suspend fun insertAll(vararg transactions: TransactionEntity)

    suspend fun insert(transaction: TransactionEntity): Long

    suspend fun update(transaction: TransactionEntity)

    suspend fun delete(transaction: TransactionEntity)

    suspend fun getQuantitiesOfPaperByBroker(brokerID: Long, paperID: Long): Float

    suspend fun getAccountBalanceByBroker(brokerID: Long): Float

    suspend fun getTotalStockByBroker(brokerID: Long): Float

    suspend fun getTotalReitsByBroker(brokerID: Long): Float

    suspend fun getTotalAdrsByBroker(brokerID: Long): Float

    suspend fun getStocksWithValues(brokerID: Long): List<PaperValueWrapperEntity>

    suspend fun getReitsWithValues(brokerID: Long): List<PaperValueWrapperEntity>

    suspend fun getAdrsWithValues(brokerID: Long): List<PaperValueWrapperEntity>
}