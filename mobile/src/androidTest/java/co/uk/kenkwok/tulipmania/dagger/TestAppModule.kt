package co.uk.kenkwok.tulipmania.dagger

import android.app.Application
import android.content.Context
import co.uk.kenkwok.tulipmania.R
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

/**
 * Created by kekwok on 18/09/2017.
 */

@Module
class TestAppModule {

    @Singleton
    @Provides
    internal fun provideContext(application: Application): Context {
        return application
    }

    @Singleton
    @Provides
    @Named("anxBaseUrl")
    internal fun getAnxBaseUrl(context: Context): String {
        return context.getString(R.string.anxpro_base_url)
    }

    @Singleton
    @Provides
    @Named("bitstampBaseUrl")
    internal fun getBitstampBaseUrl(context: Context): String {
        return context.getString(R.string.bitstamp_base_url)
    }

    @Singleton
    @Provides
    @Named("bitfinexBaseUrl")
    internal fun getBitfinexBaseUrl(context: Context): String {
        return context.getString(R.string.bitfinex_base_url)
    }
}
