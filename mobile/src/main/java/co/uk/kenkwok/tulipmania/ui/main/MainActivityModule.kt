package co.uk.kenkwok.tulipmania.ui.main

import android.content.Context
import co.uk.kenkwok.tulipmania.network.*
import dagger.Module
import dagger.Provides

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
    internal fun provideNetworkService(anxApi: ANXApi, bitstampAPI: BitstampAPI, bitfinexAPI: BitfinexAPI): NetworkService {
        return NetworkServiceImpl(anxApi, bitstampAPI, bitfinexAPI)
    }
}
