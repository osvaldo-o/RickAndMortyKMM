package osvaldo.oso.data.local.source

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import osvaldo.oso.db.Character
import rick.and.morty.db.Database

class LocalDataSourceImpl(
    val db: Database,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : LocalDataSource {

    private val queries = db.characterQueries

    override fun getCharacters(): Flow<List<Character>> {
        return queries.selectAll().asFlow().mapToList(dispatcher)
    }

    override suspend fun insertCharacter(character: Character) {
        queries.insertCharacter(
            character.id,
            character.name,
            character.status,
            character.species,
            character.gender,
            character.originName,
            character.locationName,
            character.urlImage
        )
    }

    override suspend fun deleteCharacter(id: Long) {
        queries.deleteById(id)
    }

}