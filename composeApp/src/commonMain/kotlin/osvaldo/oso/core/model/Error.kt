package osvaldo.oso.core.model

sealed interface Error {
    enum class Remote: Error {
        CLIENT_ERROR,
        REDIRECTION_ERROR,
        SERVER_ERROR,
        NO_INTERNET_ERROR,
        SERIALIZATION_ERROR,
        UNKNOWN_ERROR
    }

    enum class Local: Error {
        NOT_FOUNT,
        UNKNOWN
    }
}

data class RemoteErrorWithCode(
    val error: Error,
    val code: Int = 0
): Error
