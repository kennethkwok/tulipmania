package co.uk.kenkwok.tulipmania.ui.main

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import co.uk.kenkwok.tulipmania.R
import co.uk.kenkwok.tulipmania.models.RecyclerViewTickerItem
import co.uk.kenkwok.tulipmania.service.BitfinexService
import co.uk.kenkwok.tulipmania.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : BaseActivity() {
    @Inject lateinit var context: Context
    @Inject lateinit var viewModel: MainViewModel

    private lateinit var adapter: RecyclerViewAdapter
    private lateinit var webSocketService: BitfinexService

    private val connection = object: ServiceConnection {
        override fun onServiceConnected(p0: ComponentName, binder: IBinder) {
            webSocketService = (binder as BitfinexService.WebSocketBinder).getService()
            viewModel.subscribeWebSocketUpdates(webSocketService)
        }

        override fun onServiceDisconnected(p0: ComponentName) {}
    }

    override val layoutId = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.onCreate()
    }

    override fun onResume() {
        super.onResume()
        viewModel.onResume()
    }

    override fun onPause() {
        super.onPause()
        viewModel.onPause()
    }

    override fun onStart() {
        super.onStart()
        bindService(Intent(this, BitfinexService::class.java), connection, Context.BIND_AUTO_CREATE)

        initObservables()
        viewModel.onStart()
    }

    private fun initRecyclerView(list: ArrayList<RecyclerViewTickerItem>) {
        adapter = RecyclerViewAdapter(list)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
    }

    private fun initObservables() {
        compositeDisposable.addAll(
                viewModel.initTickerList()
                        .subscribe({ list -> initRecyclerView(list) }),
                viewModel.tickerObservable
                        .subscribe { item ->
                            setTickerItem(item)
                            hideLoadingSpinner()
                        }
        )
    }

    private fun hideLoadingSpinner() {
        if (loadingSpinner.visibility == View.VISIBLE) {
            loadingSpinner.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
        }
    }

    override fun onStop() {
        super.onStop()
        viewModel.onStop()
        unbindService(connection)
    }

    fun setTickerItem(item: RecyclerViewTickerItem) {
        adapter.updatePriceItem(item)
    }

    companion object {
        val TAG = "MainActivity"
    }
}
