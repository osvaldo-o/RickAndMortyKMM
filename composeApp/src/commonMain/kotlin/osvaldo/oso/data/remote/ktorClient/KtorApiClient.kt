package osvaldo.oso.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.serialization.SerializationException
import osvaldo.oso.core.model.Error
import osvaldo.oso.core.model.RemoteErrorWithCode
import osvaldo.oso.core.model.Result
import kotlin.coroutines.cancellation.CancellationException

class KtorApiClient(
    val httpClient: HttpClient
) {

    val baseUrl = "https://rickandmortyapi.com/api"

    suspend inline fun <reified R: Any> get(
        route: String,
        parameters: Map<String, String> = emptyMap(),
        headers: Map<String, String> = emptyMap()
    ): Result<R, Error>  {
        return safeCall {
            httpClient.get {
                url("$baseUrl/$route")

                parameters.forEach { (key, value) ->
                    parameter(key, value)
                }

                headers.forEach { (key, value) ->
                    header(key, value)
                }
            }
        }
    }

    suspend inline fun <reified R: Any> safeCall(
        execute: () -> HttpResponse
    ): Result<R, Error> {
        val response = try {
            execute()
        } catch (e: UnresolvedAddressException) {
            e.printStackTrace()
            return Result.Failed(
                RemoteErrorWithCode(Error.Remote.NO_INTERNET_ERROR)
            )
        } catch (e: SerializationException) {
            e.printStackTrace()
            return Result.Failed(
                RemoteErrorWithCode(Error.Remote.SERIALIZATION_ERROR)
            )
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            return Result.Failed(
                RemoteErrorWithCode(Error.Remote.UNKNOWN_ERROR)
            )
        }
        return responseToResult(response)
    }

    suspend inline fun <reified R: Any> responseToResult(
        response: HttpResponse
    ): Result<R, Error> {
        return when (response.status.value) {
            in 200..299 -> Result.Success(response.body<R>())
            in 300..308 -> Result.Failed(
                RemoteErrorWithCode(
                    Error.Remote.REDIRECTION_ERROR,
                    response.status.value
                )
            )
            in 400..499 -> Result.Failed(
                RemoteErrorWithCode(
                    Error.Remote.CLIENT_ERROR,
                    response.status.value
                )
            )
            in 500..599 -> Result.Failed(
                RemoteErrorWithCode(
                    Error.Remote.SERVER_ERROR,
                    response.status.value
                )
            )
            else -> Result.Failed(
                RemoteErrorWithCode(
                    Error.Remote.UNKNOWN_ERROR,
                    response.status.value
                )
            )
        }
    }

}