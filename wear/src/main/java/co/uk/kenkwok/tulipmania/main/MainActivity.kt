package co.uk.kenkwok.tulipmania.main

import android.content.Context
import android.os.Bundle
import android.support.wearable.activity.WearableActivity
import android.util.Log
import android.widget.Toast
import butterknife.ButterKnife
import co.uk.kenkwok.tulipmania.R
import co.uk.kenkwok.tulipmania.models.Ticker
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.wearable.Wearable
import dagger.android.AndroidInjection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MainActivity : WearableActivity() {

    @Inject
    lateinit var viewModel: MainViewModel

    @Inject
    lateinit var context: Context

    private var googleApiClient: GoogleApiClient? = null
    private var nodeId: String? = null
    private var compositeDisposable: CompositeDisposable? = null

    /*
    Get reference to Wear
     */
    private fun getGoogleApiClient(context: Context): GoogleApiClient {
        return GoogleApiClient.Builder(context)
                .addApi(Wearable.API)
                .build()
    }

    /*
    Devices that are connected over Bluetooth are identified in the Wear API as “nodes”.

    Since our Wear device is already connected to our phone, we need to get a list of the nodes
    that are connected to it.

    Now that we have a GoogleApiClient, we’ll use it to get the node list and pull the ID of the
    first node we find (since we’re assuming only one connection).
     */
    private fun retrieveDeviceNode() {
        val client = getGoogleApiClient(this)
        Thread(Runnable {
            client.blockingConnect(CONNECTION_TIME_OUT_MS, TimeUnit.MILLISECONDS)
            val result = Wearable.NodeApi.getConnectedNodes(client).await()
            val nodes = result.nodes
            if (nodes.size > 0) {
                nodeId = nodes[0].id
            }

            Log.d(MainActivity::class.java.simpleName, "Node ID of phone: " + nodeId!!)

            client.disconnect()
        }).start()
    }

    private fun initGoogleApiClient() {
        googleApiClient = getGoogleApiClient(this)
        retrieveDeviceNode()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)

        viewModel.onCreate()
        compositeDisposable = CompositeDisposable()

        initGoogleApiClient()

        compositeDisposable?.add(viewModel!!
                .tickerObservable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe({ ticker -> setPrice(ticker) }) { Toast.makeText(context, "Network Error", Toast.LENGTH_LONG).show() }
        )
    }

    private fun setPrice(ticker: Ticker) {
        buy_price.text = ticker.data.buy.displayShort
        sell_price.text = ticker.data.sell.displayShort
        avr_price.text = ticker.data.avg.displayShort
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onDestroy()
        compositeDisposable?.dispose()
    }

    companion object {

        private val CONNECTION_TIME_OUT_MS: Long = 1000
    }
}
