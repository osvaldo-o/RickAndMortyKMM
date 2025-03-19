package osvaldo.oso

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform