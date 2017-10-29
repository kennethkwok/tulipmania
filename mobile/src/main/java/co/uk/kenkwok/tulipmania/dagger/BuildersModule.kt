package co.uk.kenkwok.tulipmania.dagger

import co.uk.kenkwok.tulipmania.ui.main.MainActivity
import co.uk.kenkwok.tulipmania.ui.main.MainActivityModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by kekwok on 18/09/2017.
 */

@Module
abstract class BuildersModule {
    @ContributesAndroidInjector(modules = arrayOf(MainActivityModule::class))
    internal abstract fun bindMainActivity(): MainActivity
}
