package com.samuel.myholderwallet.repository

import com.samuel.myholderwallet.db.entity.BrokerEntity

interface BrokerRepository {


    suspend fun get(id: Long): BrokerEntity

    suspend fun getAll(): List<BrokerEntity>

    suspend fun loadAllByIds(brokersIds: LongArray): List<BrokerEntity>

    suspend fun insertAll(vararg brokers: BrokerEntity)

    suspend fun insert(broker: BrokerEntity): Long

    suspend fun update(broker: BrokerEntity)

    suspend fun delete(broker: BrokerEntity)
}