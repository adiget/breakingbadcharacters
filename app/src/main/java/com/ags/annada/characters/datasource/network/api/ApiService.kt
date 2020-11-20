package com.ags.annada.characters.datasource.network.api

import com.ags.annada.characters.datasource.room.entities.CharacterItem
import retrofit2.http.GET

interface ApiService {
    @GET("characters")
    suspend fun getCharacters(): List<CharacterItem>
}