package osvaldo.oso.data.mapper

import osvaldo.oso.domain.model.Character
import osvaldo.oso.domain.model.Status


fun Character.toCharacterDB(): osvaldo.oso.db.Character {
    val status = if (this.status == Status.ALIVE) "Alive" else "Dead"
    return osvaldo.oso.db.Character(
        id = this.id.toLong(),
        name = this.name,
        status = status,
        species = this.species,
        gender = this.gender,
        originName = this.originName,
        locationName = this.locationName,
        urlImage = this.urlImage
    )
}

fun osvaldo.oso.db.Character.toCharacter(): Character {
    val status = if (this.status == "Alive") Status.ALIVE else Status.DEAD
    return Character(
        id = this.id.toInt(),
        name = this.name,
        status = status,
        species = this.species,
        gender = this.gender,
        originName = this.originName,
        locationName = this.locationName,
        urlImage = this.urlImage
    )
}