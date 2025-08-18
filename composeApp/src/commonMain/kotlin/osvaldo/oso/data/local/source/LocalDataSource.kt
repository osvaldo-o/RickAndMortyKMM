package osvaldo.oso.data.local.source

import osvaldo.oso.data.local.model.CharacterDB

interface LocalDataSource {

    suspend fun getCharacters(): List<CharacterDB>

    suspend fun insertCharacters(characters: List<CharacterDB>)

}