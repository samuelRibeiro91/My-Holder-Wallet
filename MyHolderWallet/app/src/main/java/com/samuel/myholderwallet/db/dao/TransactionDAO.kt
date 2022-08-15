package com.samuel.myholdertransaction.db.dao

import androidx.room.*
import com.samuel.myholderwallet.db.entity.TransactionEntity

@Dao
interface TransactionDAO {
    @Query("SELECT * FROM `transaction` where id = :id")
    suspend fun get(id: Long): TransactionEntity

    @Query("SELECT * FROM `transaction`")
    suspend fun getAll(): List<TransactionEntity>

    @Query("SELECT * FROM `transaction` where fk_broker = :brokerID order by date desc")
    suspend fun getAllByBroker(brokerID: Long): List<TransactionEntity>

    @Query("SELECT * FROM `transaction` WHERE id IN (:transactionsIds)")
    suspend fun loadAllByIds(transactionsIds: LongArray): List<TransactionEntity>

    @Insert
    suspend fun insertAll(vararg transactions: TransactionEntity)

    @Insert
    suspend fun insert(transaction: TransactionEntity): Long

    @Update
    suspend fun update(transaction: TransactionEntity)

    @Delete
    suspend fun delete(transaction: TransactionEntity)
}