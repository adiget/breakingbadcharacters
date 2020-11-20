package com.ags.annada.characters

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateHandle
import com.ags.annada.characters.characters.CharactersRepository
import com.ags.annada.characters.characters.CharactersViewModel
import com.ags.annada.characters.datasource.room.entities.CharacterItem
import io.mockk.MockKAnnotations
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class CharactersViewModelTest {
    @ExperimentalCoroutinesApi
    @get:Rule
    var testCoroutineRule = TestCoroutineRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    lateinit var sut: CharactersViewModel

    @Mock
    lateinit var mockRepository: CharactersRepository

    @Mock
    lateinit var mockObserver: Observer<List<CharacterItem>>

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        sut = CharactersViewModel(mockRepository, SavedStateHandle())
    }

    @Test
    fun `given observer set for live data items, when item changes, then observer notified`() {
        val sampleLiveData: MutableLiveData<List<CharacterItem>> = MutableLiveData()
        sampleLiveData.observeForever(mockObserver)

        sampleLiveData.value = ArgumentMatchers.anyList()

        verify(mockObserver, times(1)).onChanged(ArgumentMatchers.anyList())
    }
}