package com.ags.annada.characters.datasource.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "character_item_table")
data class CharacterItem(
    @PrimaryKey @ColumnInfo(name = "char_id") val char_id: Int,
    @ColumnInfo(name = "appearance") val appearance: List<Int>,
    @ColumnInfo(name = "better_call_saul_appearance") val better_call_saul_appearance: List<Int>,
    @ColumnInfo(name = "birthday") val birthday: String,
    @ColumnInfo(name = "category") val category: String,
    @ColumnInfo(name = "img") val img: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "nickname") val nickname: String,
    @ColumnInfo(name = "occupation") val occupation: List<String>,
    @ColumnInfo(name = "portrayed") val portrayed: String,
    @ColumnInfo(name = "status") val status: String
)