package com.samuel.myholderwallet.repository

import com.samuel.myholderwallet.db.dao.TransactionDAO
import com.samuel.myholderwallet.db.entity.TransactionEntity
import com.samuel.myholderwallet.db.wrapper.PaperValueWrapperEntity

class TransactionRepositoryImpl(private val transactionDAO: TransactionDAO): TransactionRepository {
    override suspend fun get(id: Long): TransactionEntity = transactionDAO.get(id)

    override suspend fun getAll(): List<TransactionEntity> = transactionDAO.getAll()

    override suspend fun getAllByBroker(brokerID: Long): List<TransactionEntity> = transactionDAO.getAllByBroker(brokerID)

    override suspend fun loadAllByIds(transactionsIds: LongArray): List<TransactionEntity> = transactionDAO.loadAllByIds(transactionsIds)

    override suspend fun insertAll(vararg transactions: TransactionEntity)  = transactionDAO.insertAll(*transactions)

    override suspend fun insert(transaction: TransactionEntity): Long  = transactionDAO.insert(transaction)

    override suspend fun update(transaction: TransactionEntity)  = transactionDAO.update(transaction)

    override suspend fun delete(transaction: TransactionEntity)  = transactionDAO.delete(transaction)

    override suspend fun getQuantitiesOfPaperByBroker(brokerID: Long, paperID: Long) = transactionDAO.getQuantitiesOfPaperByBroker(brokerID, paperID)

    override suspend fun getAccountBalanceByBroker(brokerID: Long) = transactionDAO.getAccountBalanceByBroker(brokerID) //Saldo em conta

    override suspend fun getTotalStockByBroker(brokerID: Long): Float  = transactionDAO.getTotalStockByBroker(brokerID)

    override suspend fun getTotalReitsByBroker(brokerID: Long): Float = transactionDAO.getTotalReitsByBroker(brokerID)

    override suspend fun getTotalAdrsByBroker(brokerID: Long): Float  = transactionDAO.getTotalAdrsByBroker(brokerID)

    override suspend fun getStocksWithValues(brokerID: Long): List<PaperValueWrapperEntity>  = transactionDAO.getStocksWithValues(brokerID)

    override suspend fun getReitsWithValues(brokerID: Long): List<PaperValueWrapperEntity>  = transactionDAO.getReitsWithValues(brokerID)

    override suspend fun getAdrsWithValues(brokerID: Long): List<PaperValueWrapperEntity>  = transactionDAO.getAdrsWithValues(brokerID)

    override suspend fun getDividendsByDate(brokerID: Long, initialValue:Long, endValue: Long) = transactionDAO.getDividendsByDate(brokerID, initialValue, endValue)

    override suspend fun getBuysAndUsedDividendsByDate(brokerID: Long, initialValue:Long, endValue: Long)= transactionDAO.getBuysAndUsedDividendsByDate(brokerID, initialValue, endValue)
}