package com.samuel.myholderwallet.repository

import com.samuel.myholderwallet.db.dao.WalletDAO
import com.samuel.myholderwallet.db.entity.WalletEntity

class WalletRepositoryImpl(private val walletDAO: WalletDAO): WalletRepository {
    override suspend fun get(id: Long): WalletEntity  = walletDAO.get(id)

    override suspend fun getByBroker(id: Long): WalletEntity? = walletDAO.getByBroker(id)

    override suspend fun getAll(): List<WalletEntity>  = walletDAO.getAll()

    override suspend fun loadAllByIds(walletsIds: LongArray): List<WalletEntity> = walletDAO.loadAllByIds(walletsIds)

    override suspend fun insertAll(vararg wallets: WalletEntity) = walletDAO.insertAll(*wallets)

    override suspend fun insert(wallet: WalletEntity): Long  = walletDAO.insert(wallet)

    override suspend fun update(wallet: WalletEntity)  = walletDAO.update(wallet)

    override suspend fun delete(wallet: WalletEntity)  = walletDAO.delete(wallet)
}