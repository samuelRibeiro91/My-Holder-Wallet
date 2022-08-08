package com.samuel.myholderwallet.db.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.samuel.myholderwallet.types.PaperType
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "paper")
class PaperEntity: Parcelable {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    var initial: String = ""

    var description: String = ""

    var type: PaperType = PaperType.STOCK

    override fun toString(): String  = "$initial - $description"
}