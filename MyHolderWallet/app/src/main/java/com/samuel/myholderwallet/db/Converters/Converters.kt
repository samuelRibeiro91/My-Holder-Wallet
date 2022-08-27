package com.samuel.myholderwallet.db.Converters

import androidx.room.TypeConverter
import com.samuel.myholderwallet.types.MovementTypes
import com.samuel.myholderwallet.types.PaperType

class Converters {
    @TypeConverter
    fun movementTypesToInt(value: MovementTypes?): Int?{
        return value?.ordinal
    }

    @TypeConverter
    fun intToMovementTypes(value: Int?): MovementTypes{
        return MovementTypes.values()[value!!]
    }

    @TypeConverter
    fun movementTypesToString(value: MovementTypes?): String?{
        return value?.toString()
    }

    @TypeConverter
    fun stringToMovementTypes(value: String):  MovementTypes{
        return MovementTypes.valueOf(value)
    }

    @TypeConverter
    fun paperTypeToInt(value: PaperType?): Int?{
        return value?.ordinal
    }

    @TypeConverter
    fun intToPaperType(value: Int?): PaperType{
        return PaperType.values()[value!!]
    }

    @TypeConverter
    fun paperTypeToString(value: PaperType?): String?{
        return value?.toString()
    }

    @TypeConverter
    fun stringToPaperType(value: String):  PaperType{
        return PaperType.valueOf(value)
    }
}