package co.uk.kenkwok.tulipmania.dagger

import co.uk.kenkwok.tulipmania.mock.MockHttpInterceptor
import co.uk.kenkwok.tulipmania.network.ANXApi
import co.uk.kenkwok.tulipmania.network.BitfinexAPI
import co.uk.kenkwok.tulipmania.network.BitstampAPI
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

/**
 * Created by kwokk on 24/11/2017.
 */
@Module
class TestNetworkModule {
    @Singleton
    @Provides
    internal fun provideAnxApi(@Named("anxBaseUrl") baseUrl: String): ANXApi {
        return getRetrofit(baseUrl).create(ANXApi::class.java)
    }

    @Singleton
    @Provides
    internal fun provideBitstampApi(@Named("bitstampBaseUrl") baseUrl: String): BitstampAPI {
        return getRetrofit(baseUrl).create(BitstampAPI::class.java)
    }

    @Singleton
    @Provides
    internal fun provideBitfinexApi(@Named("bitfinexBaseUrl") baseUrl: String): BitfinexAPI {
        return getRetrofit(baseUrl).create(BitfinexAPI::class.java)
    }

    internal fun getRetrofit(baseUrl: String): Retrofit {
        return Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(OkHttpClient.Builder().addInterceptor(MockHttpInterceptor()).build())
                .build()
    }
}