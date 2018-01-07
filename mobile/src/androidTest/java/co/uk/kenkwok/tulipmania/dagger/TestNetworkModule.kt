package co.uk.kenkwok.tulipmania.dagger

import co.uk.kenkwok.tulipmania.library.websocket.RxWebSocket
import co.uk.kenkwok.tulipmania.mock.MockHttpInterceptor
import co.uk.kenkwok.tulipmania.network.ANXApi
import co.uk.kenkwok.tulipmania.network.BitfinexAPI
import co.uk.kenkwok.tulipmania.network.BitstampAPI
import co.uk.kenkwok.tulipmania.service.BitfinexService
import co.uk.kenkwok.tulipmania.service.BitfinexWebSocket
import dagger.Module
import dagger.Provides
import io.reactivex.Completable
import io.reactivex.Flowable
import okhttp3.OkHttpClient
import org.mockito.Mockito
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

    @Singleton
    @Provides
    internal fun provideWebSocketService(): BitfinexService {
        return BitfinexService()
    }

    @Singleton
    @Provides
    internal fun provideWebSocket(@Named("bitfinexWebSocketUrl") websocketUrl: String): RxWebSocket {
        return RxWebSocket(OkHttpClient.Builder().addInterceptor(MockHttpInterceptor()).build(), websocketUrl)
    }

    @Singleton
    @Provides
    internal fun provideBitfinexWebSocket(): BitfinexWebSocket {
        val webSocket = Mockito.mock(BitfinexWebSocket::class.java)
        Mockito.`when`(webSocket.connectWebSocket()).thenReturn(Completable.complete())
        Mockito.`when`(webSocket.closeWebSocket()).thenReturn(Completable.complete())
        Mockito.`when`(webSocket.subscribeToBTCTicker()).thenReturn(
                Flowable.just("[ 2, 236.62, 9.0029, 236.88, 7.1138, -1.02, 0, 236.52, 5191.36754297, 250.01, 220.05 ]"))
        return webSocket
    }
}