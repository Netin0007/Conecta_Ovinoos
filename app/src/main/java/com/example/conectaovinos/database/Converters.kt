package com.example.conectaovinos.database//package com.example.conectaovinos.database
//
//import androidx.room.TypeConverter
//import com.example.conectaovinos.database.enums.tipoManejo
//import com.example.conectaovinos.database.enums.tipoTransacao
//
//class Converters {
//
//    @TypeConverter
//    fun fromTipoManejo(v: tipoManejo): String = v.name
//
//    @TypeConverter
//    fun toTipoManejo(v: String): tipoManejo = tipoManejo.valueOf(v)
//
//    @TypeConverter
//    fun fromTipoTransacao(v: tipoTransacao): String = v.name
//
//    @TypeConverter
//    fun toTipoTransacao(v: String): tipoTransacao = tipoTransacao.valueOf(v)
//}