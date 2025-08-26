package osvaldo.oso.data.local.source

import kotlinx.coroutines.flow.Flow
import osvaldo.oso.db.Character

interface LocalDataSource {

    fun getCharacters(): Flow<List<Character>>

    suspend fun insertCharacter(character: Character)

    suspend fun deleteCharacter(id: Long)

}