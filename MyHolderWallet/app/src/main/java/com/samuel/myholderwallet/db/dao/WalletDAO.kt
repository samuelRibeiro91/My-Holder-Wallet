package com.samuel.myholderwallet.db.dao

import androidx.room.*
import com.samuel.myholderwallet.db.entity.WalletEntity

@Dao
interface WalletDAO {
    @Query("SELECT * FROM wallet where id = :id")
    suspend fun get(id: Long): WalletEntity

    @Query("SELECT * FROM wallet where fk_broker = :id")
    suspend fun getByBroker(id: Long): WalletEntity?

    @Query("SELECT * FROM wallet")
    suspend fun getAll(): List<WalletEntity>

    @Query("SELECT * FROM wallet WHERE id IN (:walletsIds)")
    suspend fun loadAllByIds(walletsIds: LongArray): List<WalletEntity>

    @Insert
    suspend fun insertAll(vararg wallets: WalletEntity)

    @Insert
    suspend fun insert(wallet: WalletEntity): Long

    @Update
    suspend fun update(wallet: WalletEntity)

    @Delete
    suspend fun delete(wallet: WalletEntity)
}