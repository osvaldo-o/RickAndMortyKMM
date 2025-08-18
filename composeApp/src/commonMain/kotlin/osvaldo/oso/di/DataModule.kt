package osvaldo.oso.di

import app.cash.sqldelight.db.SqlDriver
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import osvaldo.oso.data.local.source.LocalDataSource
import osvaldo.oso.data.local.source.LocalDataSourceImpl
import osvaldo.oso.data.remote.ktorClient.KtorApiClient
import osvaldo.oso.data.remote.ktorClient.buildHttpClient
import osvaldo.oso.data.remote.ktorClient.getHttpEngine
import osvaldo.oso.data.repository.CharacterRepositoryImpl
import osvaldo.oso.domain.repository.CharacterRepository
import rick.and.morty.db.Database

fun dataModule(driverFactory: DatabaseDriverFactory) =  module {

    single<KtorApiClient> {
        KtorApiClient(buildHttpClient(getHttpEngine()))
    }

    single<LocalDataSource> {
        LocalDataSourceImpl(db = Database(driver = driverFactory.createDriver()))
    }

    singleOf(::CharacterRepositoryImpl).bind<CharacterRepository>()

}

expect class DatabaseDriverFactory {
    fun createDriver(): SqlDriver
}