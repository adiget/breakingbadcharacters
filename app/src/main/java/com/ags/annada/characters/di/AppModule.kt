package com.ags.annada.characters.di

import android.content.Context
import androidx.room.Room
import com.ags.annada.characters.datasource.room.CharactersDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): CharactersDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            CharactersDatabase::class.java,
            "Characters.db"
        ).build()
    }

    @Singleton
    @Provides
    fun provideIoDispatcher() = Dispatchers.IO
}