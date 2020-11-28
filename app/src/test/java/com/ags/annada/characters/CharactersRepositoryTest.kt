package com.ags.annada.characters

import androidx.lifecycle.LiveData
import com.ags.annada.characters.datasource.network.api.ApiService
import com.ags.annada.characters.datasource.room.CharactersDatabase
import com.ags.annada.characters.datasource.room.daos.CharacterDao
import com.ags.annada.characters.datasource.room.entities.CharacterItem
import com.ags.annada.characters.main.CharactersRepository
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.junit.Before
import org.junit.Test

class CharactersRepositoryTest {
    lateinit var sut: CharactersRepository

    @MockK
    lateinit var mockDatabase: CharactersDatabase

    @MockK
    lateinit var mockApiService: ApiService

    @RelaxedMockK
    lateinit var mockDao: CharacterDao

    @MockK
    lateinit var mockLiveSingleItem: LiveData<CharacterItem>

    lateinit var mainDispatcher: CoroutineDispatcher

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        mainDispatcher = Dispatchers.Main
        every { mockDatabase.characterDao() } returns mockDao
        sut = CharactersRepository(mockDatabase, mockApiService, mainDispatcher)
    }

    @Test
    fun `given single item returns, when getCharacterWithId called, then dao getCharacterWithId called`() {
        //given
        every { mockDao.getCharacterWithId(0) } returns mockLiveSingleItem

        //when
        sut.getCharacterWithId(0)

        //then
        verify { mockDao.getCharacterWithId(0) }
    }
}