package com.samuel.myholderwallet.db.entity

import androidx.room.*

@Entity(tableName = "wallet",
    foreignKeys = [
        ForeignKey(
            entity = BrokerEntity::class,
            parentColumns = ["id"],
            childColumns = ["fk_broker"])]
)
class WalletEntity{
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0

    @ColumnInfo(name = "fk_broker")
    var broker : Int = 0

    @ColumnInfo(name = "balance")
    var balance : Float = 0.0f

    @ColumnInfo(name = "credit")
    var credit : Float = 0.0f

    @Ignore
    val totalbalance: Float = balance + credit
}