package com.samuel.myholderwallet.repository

import com.samuel.myholderwallet.db.entity.BrokerEntity


const val FAKE_BROKER_INSERTED: Long = 1

val FAKE_BROKER = BrokerEntity().apply {
    name = "Broker Test"
}

class BrokerRepositoryTest: BrokerRepository{
    override suspend fun get(id: Long): BrokerEntity = TODO("Not yet implemented")

    override suspend fun getAll(): List<BrokerEntity> = listOf(
        BrokerEntity().apply {
            name = "Teste 1"
            id   = 1
        },
        BrokerEntity().apply {
            name = "Teste 2"
            id   = 2
        }

    )

    override suspend fun loadAllByIds(brokersIds: LongArray): List<BrokerEntity>  = TODO("Not yet implemented")

    override suspend fun insertAll(vararg brokers: BrokerEntity)  = TODO("Not yet implemented")

    override suspend fun insert(broker: BrokerEntity): Long  =
        if (broker.name == FAKE_BROKER.name) FAKE_BROKER_INSERTED
        else 0

    override suspend fun update(broker: BrokerEntity) {
        if (broker.name == "Broker Test Error") throw Exception("Erro ao atualizar!")
    }

    override suspend fun delete(broker: BrokerEntity)  {}

}


