package com.ags.annada.characters.datasource.room.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ags.annada.characters.datasource.room.entities.CharacterItem

@Dao
interface CharacterDao {

    @get:Query("SELECT * from character_item_table")
    val all: LiveData<List<CharacterItem>>

    @Query("SELECT * FROM character_item_table")
    fun observeCharacters(): LiveData<List<CharacterItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(characterItem: CharacterItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg characterItem: CharacterItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCharacters(vararg characterItem: CharacterItem)

    @Query("SELECT * from character_item_table WHERE char_id = :key")
    fun getCharacterWithId(key: Int): LiveData<CharacterItem>

    @Query("DELETE from character_item_table")
    suspend fun deleteAll()
}