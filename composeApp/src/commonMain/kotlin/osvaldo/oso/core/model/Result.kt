package osvaldo.oso.core.model

sealed class Result<D, E: Error>(
    val data: D? = null,
    val error: E? = null
) {

    class Success<D>(
        data: D?
    ): Result<D, Error>(data = data)

    class Failed<D>(
        error: Error?
    ): Result<D, Error>(error = error)

}