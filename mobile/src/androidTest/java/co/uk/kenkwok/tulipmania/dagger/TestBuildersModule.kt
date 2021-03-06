package co.uk.kenkwok.tulipmania.dagger

import co.uk.kenkwok.tulipmania.service.BitfinexService
import co.uk.kenkwok.tulipmania.ui.main.MainActivity
import co.uk.kenkwok.tulipmania.ui.main.MainActivityTestModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by kekwok on 18/09/2017.
 */

@Module
abstract class TestBuildersModule {
    @ContributesAndroidInjector(modules = arrayOf(MainActivityTestModule::class))
    internal abstract fun bindMainActivity(): MainActivity

    @ContributesAndroidInjector
    internal abstract fun bindWebSocketService(): BitfinexService
}
