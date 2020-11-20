package com.ags.annada.characters.datasource.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ags.annada.characters.datasource.room.daos.CharacterDao
import com.ags.annada.characters.datasource.room.entities.CharacterItem

@Database(entities = [CharacterItem::class], version = 1)
@TypeConverters(RoomTypeConverters::class)
abstract class CharactersDatabase : RoomDatabase() {
    abstract fun characterDao(): CharacterDao
}