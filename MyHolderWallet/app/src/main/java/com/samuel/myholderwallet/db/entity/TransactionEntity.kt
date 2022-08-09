package com.samuel.myholderwallet.db.entity

import android.os.Parcelable
import androidx.room.*
import com.samuel.myholderwallet.types.MovementTypes
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "transaction",
        foreignKeys = [
                        ForeignKey(
                            entity = BrokerEntity::class,
                            parentColumns = arrayOf("id"),
                            childColumns = arrayOf("fk_broker")),
                        ForeignKey(
                            entity = PaperEntity::class,
                            parentColumns = arrayOf("id"),
                            childColumns = arrayOf("fk_paper"))])

data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(index = true)
    var fk_broker: Long? = null,

    @ColumnInfo(index = true)
    var fk_paper: Long? = null,

    var quantity: Int = 0,

    var value: Float = 0.0f,

    var cost: Float = 0.0f,

    var type: MovementTypes? = MovementTypes.BUY_PAPERS,

    var credit: Float = 0.0f,

    var date: Double = 0.0
) : Parcelable