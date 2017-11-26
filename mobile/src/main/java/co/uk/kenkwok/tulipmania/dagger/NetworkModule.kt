package co.uk.kenkwok.tulipmania.dagger

import co.uk.kenkwok.tulipmania.network.ANXApi
import co.uk.kenkwok.tulipmania.network.BitfinexAPI
import co.uk.kenkwok.tulipmania.network.BitstampAPI
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

/**
 * Created by kwokk on 24/11/2017.
 */
@Module
class NetworkModule {
    @Singleton
    @Provides
    internal fun provideAnxApi(okHttpClient: OkHttpClient, @Named("anxBaseUrl") baseUrl: String): ANXApi {
        return getRetrofit(okHttpClient, baseUrl).create(ANXApi::class.java)
    }

    @Singleton
    @Provides
    internal fun provideBitstampApi(okHttpClient: OkHttpClient, @Named("bitstampBaseUrl") baseUrl: String): BitstampAPI {
        return getRetrofit(okHttpClient, baseUrl).create(BitstampAPI::class.java)
    }

    @Singleton
    @Provides
    internal fun provideBitfinexApi(okHttpClient: OkHttpClient, @Named("bitfinexBaseUrl") baseUrl: String): BitfinexAPI {
        return getRetrofit(okHttpClient, baseUrl).create(BitfinexAPI::class.java)
    }

    internal fun getRetrofit(okHttpClient: OkHttpClient, baseUrl: String): Retrofit {
        return Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build()
    }

    @Singleton
    @Provides
    internal fun provideHttpClient(interceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(interceptor).build()
    }

    @Singleton
    @Provides
    internal fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }
}