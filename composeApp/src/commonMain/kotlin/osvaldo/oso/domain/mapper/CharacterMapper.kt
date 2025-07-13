package osvaldo.oso.domain.mapper

import osvaldo.oso.data.remote.model.CharacterApi
import osvaldo.oso.domain.model.Character
import osvaldo.oso.domain.model.Status

fun CharacterApi.toCharacter() = Character(
    name = this.name,
    status = toStatus(this.status),
    species = this.species,
    gender = this.gender,
    originName = this.origin.name,
    locationName = this.location.name,
    urlImage = this.image
)

fun toStatus(status: String) = if (status == "Alive") {
    Status.ALIVE
} else {
    Status.DEAD
}