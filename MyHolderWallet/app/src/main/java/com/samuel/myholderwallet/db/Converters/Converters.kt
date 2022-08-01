package com.samuel.myholderwallet.db.Converters

import androidx.room.TypeConverter
import com.samuel.myholderwallet.types.MovementTypes
import com.samuel.myholderwallet.types.PaperType

class Converters {
    @TypeConverter
    fun MovementTypesToInt(value: MovementTypes?): Int?{
        return value?.ordinal
    }

    @TypeConverter
    fun IntToMovementTypes(value: Int?): MovementTypes?{
        return MovementTypes.values()[value!!]
    }

    @TypeConverter
    fun MovementTypesToString(value: MovementTypes?): String?{
        return value?.toString()
    }

    @TypeConverter
    fun StringToMovementTypes(value: String):  MovementTypes?{
        return MovementTypes.valueOf(value)
    }

    @TypeConverter
    fun PaperTypeToInt(value: PaperType?): Int?{
        return value?.ordinal
    }

    @TypeConverter
    fun IntToPaperType(value: Int?): PaperType?{
        return PaperType.values()[value!!]
    }

    @TypeConverter
    fun PaperTypeToString(value: PaperType?): String?{
        return value?.toString()
    }

    @TypeConverter
    fun StringToPaperType(value: String):  PaperType?{
        return PaperType.valueOf(value)
    }
}