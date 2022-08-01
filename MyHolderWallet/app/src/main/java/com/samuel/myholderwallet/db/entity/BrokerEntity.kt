package com.samuel.myholderwallet.db.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.samuel.myholderwallet.types.PaperType
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "broker")
data class BrokerEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val name: String
): Parcelable