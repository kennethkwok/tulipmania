package co.uk.kenkwok.tulipmania.main

import co.uk.kenkwok.tulipmania.models.ApiCredentials
import co.uk.kenkwok.tulipmania.network.NetworkService
import co.uk.kenkwok.tulipmania.network.NetworkServiceImpl
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

/**
 * Created by kekwok on 18/09/2017.
 */

@Module
class MainActivityModule {
    @Provides
    internal fun provideMainViewModel(networkService: NetworkService, apiCredentials: ApiCredentials): MainViewModel {
        return MainViewModel(networkService, apiCredentials)
    }

    @Provides
    internal fun provideNetworkService(retrofit: Retrofit): NetworkService {
        return NetworkServiceImpl(retrofit)
    }
}
