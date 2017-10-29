package co.uk.kenkwok.tulipmania.dagger

import android.app.Application
import co.uk.kenkwok.tulipmania.TulipManiaApplication
import co.uk.kenkwok.tulipmania.utils.JsonUtils
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

/**
 * Root of the Dagger map
 * Created by kekwok on 11/09/2017.
 */

@Singleton
@Component(modules = arrayOf(AndroidInjectionModule::class, AppModule::class, BuildersModule::class, JsonUtils::class))
interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    fun inject(application: TulipManiaApplication)
}
