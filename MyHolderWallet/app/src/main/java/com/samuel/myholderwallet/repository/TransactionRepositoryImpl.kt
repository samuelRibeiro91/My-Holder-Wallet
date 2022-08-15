package com.samuel.myholderwallet.repository

import com.samuel.myholdertransaction.db.dao.TransactionDAO
import com.samuel.myholderwallet.db.entity.TransactionEntity

class TransactionRepositoryImpl(private val transactionDAO: TransactionDAO): TransactionRepository {
    override suspend fun get(id: Long): TransactionEntity = transactionDAO.get(id)

    override suspend fun getAll(): List<TransactionEntity> = transactionDAO.getAll()

    override suspend fun getAllByBroker(brokerID: Long): List<TransactionEntity> = transactionDAO.getAllByBroker(brokerID)

    override suspend fun loadAllByIds(transactionsIds: LongArray): List<TransactionEntity> = transactionDAO.loadAllByIds(transactionsIds)

    override suspend fun insertAll(vararg transactions: TransactionEntity)  = transactionDAO.insertAll(*transactions)

    override suspend fun insert(transaction: TransactionEntity): Long  = transactionDAO.insert(transaction)

    override suspend fun update(transaction: TransactionEntity)  = transactionDAO.update(transaction)

    override suspend fun delete(transaction: TransactionEntity)  = transactionDAO.delete(transaction)
}