package co.uk.kenkwok.tulipmania.ui.main

import android.content.Context
import co.uk.kenkwok.tulipmania.network.NetworkService
import co.uk.kenkwok.tulipmania.network.NetworkServiceImpl
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Named

/**
 * Created by kekwok on 18/09/2017.
 */
@Module
class MainActivityModule {
    @Provides
    internal fun provideMainViewModel(networkService: NetworkService, context: Context): MainViewModel {
        return MainViewModel(networkService, context)
    }

    @Provides
    internal fun provideNetworkService(@Named("anxRetrofit") anxRetrofit: Retrofit,
                                       @Named("bitstampRetrofit") bitstampRetrofit: Retrofit,
                                       @Named("bitfinexRetrofit") bitfinexRetrofit: Retrofit): NetworkService {
        return NetworkServiceImpl(anxRetrofit, bitstampRetrofit, bitfinexRetrofit)
    }
}
