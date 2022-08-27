package com.samuel.myholderwallet.db.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "broker")
class BrokerEntity: Parcelable {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    var name: String = ""

    override fun toString(): String = name
}