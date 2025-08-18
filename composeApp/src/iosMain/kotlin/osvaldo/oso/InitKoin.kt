package osvaldo.oso

import org.koin.core.context.startKoin
import osvaldo.oso.di.DatabaseDriverFactory
import osvaldo.oso.di.dataModule
import osvaldo.oso.di.presentationModule

fun initKoin() {
    startKoin {
        modules(
            dataModule(DatabaseDriverFactory()),
            presentationModule
        )
    }
}