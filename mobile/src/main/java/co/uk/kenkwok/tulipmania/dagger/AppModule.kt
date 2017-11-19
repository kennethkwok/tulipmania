package co.uk.kenkwok.tulipmania.dagger

import android.app.Application
import android.content.Context
import co.uk.kenkwok.tulipmania.R
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
 * Created by kekwok on 18/09/2017.
 */

@Module
class AppModule {

    @Singleton
    @Provides
    internal fun provideContext(application: Application): Context {
        return application
    }

    @Singleton
    @Provides
    @Named("anxRetrofit")
    internal fun provideAnxRetrofit(okHttpClient: OkHttpClient, @Named("anxBaseUrl") baseUrl: String): Retrofit {
        return Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build()
    }

    @Singleton
    @Provides
    @Named("bitstampRetrofit")
    internal fun provideBitstampRetrofit(okHttpClient: OkHttpClient, @Named("bitstampBaseUrl") baseUrl: String): Retrofit {
        return Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build()
    }

    @Singleton
    @Provides
    @Named("bitfinexRetrofit")
    internal fun provideBitfinexRetrofit(okHttpClient: OkHttpClient, @Named("bitfinexBaseUrl") baseUrl: String): Retrofit {
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
