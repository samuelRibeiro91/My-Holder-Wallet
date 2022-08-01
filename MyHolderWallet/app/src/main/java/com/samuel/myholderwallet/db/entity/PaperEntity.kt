package com.samuel.myholderwallet.db.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.samuel.myholderwallet.types.PaperType
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "paper")
data class PaperEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val initial: String,

    val description: String,

    val type: PaperType = PaperType.STOCK
): Parcelable