package com.samuel.myholderwallet.repository

import com.samuel.myholderwallet.db.entity.WalletEntity

interface WalletRepository {

    suspend fun get(id: Long): WalletEntity

    suspend fun getAll(): List<WalletEntity>

    suspend fun loadAllByIds(walletsIds: LongArray): List<WalletEntity>

    suspend fun insertAll(vararg wallets: WalletEntity)

    suspend fun insert(wallet: WalletEntity): Long

    suspend fun update(wallet: WalletEntity)

    suspend fun delete(wallet: WalletEntity)
}