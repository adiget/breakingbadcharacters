package com.ags.annada.characters.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.ags.annada.characters.datasource.network.api.ApiService
import com.ags.annada.characters.datasource.room.CharactersDatabase
import com.ags.annada.characters.datasource.room.entities.CharacterItem
import com.ags.annada.characters.utils.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CharactersRepository @Inject constructor(
    private val database: CharactersDatabase,
    private val apiService: ApiService,
    private val ioDispatcher: CoroutineDispatcher
) {
    private val _items = database.characterDao().observeCharacters()
    val item: LiveData<Result<List<CharacterItem>>> = Transformations.map(_items) {
        Result.Success(it)
    }

    fun getCharacterWithId(charId: Int): LiveData<CharacterItem> {
        return database.characterDao().getCharacterWithId(charId)
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