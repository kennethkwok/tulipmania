package co.uk.kenkwok.tulipmania.dagger

import android.app.Application
import co.uk.kenkwok.tulipmania.TestTulipManiaApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

/**
 * Root of the Dagger map
 * Created by kekwok on 11/09/2017.
 */

@Singleton
@Component(modules = arrayOf(AndroidInjectionModule::class, TestAppModule::class, TestBuildersModule::class, TestNetworkModule::class))
interface TestAppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): TestAppComponent
    }

    fun inject(application: TestTulipManiaApplication)
}
