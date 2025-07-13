package osvaldo.oso

import android.app.Application
import org.koin.android.ext.koin.androidContext
import osvaldo.oso.di.initKoin

class AppRickAndMorty : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@AppRickAndMorty)
        }
    }
}