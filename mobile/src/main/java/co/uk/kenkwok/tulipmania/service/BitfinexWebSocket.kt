package co.uk.kenkwok.tulipmania.service

import io.reactivex.Completable
import io.reactivex.Flowable

/**
 * Created by kwokk on 03/01/2018.
 */
interface BitfinexWebSocket {
    fun connectWebSocket(): Completable
    fun closeWebSocket(): Completable
    fun subscribeToTicker(): Flowable<String>
    fun unsubscribeFromTicker(channelId: String)
}