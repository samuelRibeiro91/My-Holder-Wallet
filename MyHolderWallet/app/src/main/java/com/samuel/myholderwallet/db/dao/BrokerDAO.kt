package com.samuel.myholderwallet.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.samuel.myholderwallet.db.entity.BrokerEntity

@Dao
interface BrokerDAO {
    @Query("SELECT * FROM broker where id = :id")
    suspend fun get(id: Long): BrokerEntity

    @Query("SELECT * FROM broker")
    suspend fun getAll(): List<BrokerEntity>

    @Query("SELECT * FROM broker WHERE id IN (:brokersIds)")
    suspend fun loadAllByIds(brokersIds: LongArray): List<BrokerEntity>

    @Insert
    suspend fun insertAll(vararg brokers: BrokerEntity)

    @Insert
    suspend fun insert(broker: BrokerEntity): Long

    @Update
    suspend fun update(broker: BrokerEntity)

    @Delete
    suspend fun delete(broker: BrokerEntity)
}