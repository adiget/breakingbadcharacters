package com.ags.annada.characters.characters

import androidx.lifecycle.LiveData
import com.ags.annada.characters.datasource.network.api.ApiService
import com.ags.annada.characters.datasource.room.CharactersDatabase
import com.ags.annada.characters.datasource.room.entities.CharacterItem
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CharactersRepository @Inject constructor(
    private val database: CharactersDatabase,
    private val apiService: ApiService,
    private val ioDispatcher: CoroutineDispatcher) {

    fun getCharacterWithId(charId: Int): LiveData<CharacterItem> {
        return database.characterDao().getCharacterWithId(charId)
    }

    fun observeCharacters(): LiveData<List<CharacterItem>> {
        return database.characterDao().observeCharacters()
    }

    suspend fun refreshCharacters() {
        updateCharactersFromRemoteDataSource()
    }

    private suspend fun updateCharactersFromRemoteDataSource() {
        withContext(ioDispatcher) {
            val characterItems = apiService.getCharacters()
            database.characterDao().insertAll(characterItem = characterItems.toTypedArray())
        }
    }
}