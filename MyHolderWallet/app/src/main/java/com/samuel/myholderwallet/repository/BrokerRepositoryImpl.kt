package com.samuel.myholderwallet.repository

import androidx.lifecycle.LiveData
import com.samuel.myholderwallet.db.dao.BrokerDAO
import com.samuel.myholderwallet.db.entity.BrokerEntity

class BrokerRepositoryImpl(private val brokerDAO: BrokerDAO): BrokerRepository {
    override suspend fun get(id: Long): BrokerEntity
            = brokerDAO.get(id)

    override suspend fun getAll(): List<BrokerEntity>
            = brokerDAO.getAll()

    override suspend fun loadAllByIds(brokersIds: LongArray): List<BrokerEntity>
            = brokerDAO.loadAllByIds(brokersIds)

    override suspend fun insertAll(vararg brokers: BrokerEntity)
            = brokerDAO.insertAll(*brokers)

    override suspend fun insert(broker: BrokerEntity): Long
            = brokerDAO.insert(broker)

    override suspend fun update(broker: BrokerEntity)  = brokerDAO.update(broker)

    override suspend fun delete(broker: BrokerEntity)  = brokerDAO.delete(broker)
}