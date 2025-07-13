package osvaldo.oso.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import osvaldo.oso.data.remote.ktorClient.KtorApiClient
import osvaldo.oso.data.remote.ktorClient.buildHttpClient
import osvaldo.oso.data.remote.ktorClient.getHttpEngine
import osvaldo.oso.data.repository.CharacterRepositoryImpl
import osvaldo.oso.domain.repository.CharacterRepository

val dataModule =  module {

    single<KtorApiClient> {
        KtorApiClient(buildHttpClient(getHttpEngine()))
    }

    singleOf(::CharacterRepositoryImpl).bind<CharacterRepository>()

}