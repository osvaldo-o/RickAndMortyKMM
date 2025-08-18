package osvaldo.oso.data.mappers

import osvaldo.oso.data.local.model.CharacterDB
import osvaldo.oso.data.remote.model.CharacterApi

fun CharacterApi.toCharacterDB(): CharacterDB {
    return CharacterDB(
        id = id,
        name = name,
        status = status,
        species = species,
        gender = gender,
        originName = origin.name,
        locationName = location.name,
        urlImage = image
    )
}