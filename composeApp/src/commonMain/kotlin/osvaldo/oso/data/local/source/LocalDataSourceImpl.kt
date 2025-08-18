package osvaldo.oso.data.local.source

import osvaldo.oso.data.local.model.CharacterDB
import rick.and.morty.db.Database

class LocalDataSourceImpl(
    private val db: Database
) : LocalDataSource {

    private val queries = db.characterQueries

    override suspend fun getCharacters(): List<CharacterDB> {
        return queries.selectAll().executeAsList().map {
            CharacterDB(
                id = it.id.toInt(),
                name = it.name,
                status = it.status,
                species = it.species,
                gender = it.gender,
                originName = it.originName,
                locationName = it.locationName,
                urlImage = it.urlImage
            )
        }
    }

    override suspend fun insertCharacters(characters: List<CharacterDB>) {
        characters.map { character ->
            queries.insertCharacter(
                id = character.id.toLong(),
                name = character.name,
                status = character.status,
                species = character.species,
                gender = character.gender,
                originName = character.originName,
                locationName = character.locationName,
                urlImage = character.urlImage
            )
        }
    }

}