package co.uk.kenkwok.tulipmania.ui.main

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.Toast
import co.uk.kenkwok.tulipmania.R
import co.uk.kenkwok.tulipmania.models.PriceItem
import co.uk.kenkwok.tulipmania.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : BaseActivity() {
    @Inject
    lateinit var context: Context

    @Inject
    lateinit var viewModel: MainViewModel

    private val adapter = RecyclerViewAdapter()

    override val layoutId = R.layout.activity_main

    override fun onResume() {
        super.onResume()
        viewModel.onResume()
    }

    override fun onPause() {
        super.onPause()
        viewModel.onPause()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViews()
        initObservables()
        viewModel.onCreate()
    }

    private fun initViews() {
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
    }

    private fun initObservables() {
        compositeDisposable.addAll(
                viewModel.anxTickerObservable
                        .subscribe({ priceItem ->
                            setPriceItem(priceItem)
                            hideLoadingSpinner()
                        }, {
                            t -> displayError(t)
                        }),
                viewModel.bitstampTickerObservable
                        .subscribe({ priceItem ->
                            setPriceItem(priceItem)
                            hideLoadingSpinner()
                        }, {
                            t -> displayError(t)
                        }),
                viewModel.bitfinexTickerObservable
                        .subscribe({ priceItem ->
                            setPriceItem(priceItem)
                            hideLoadingSpinner()
                        }, {
                            t -> displayError(t)
                        })
        )
    }

    private fun hideLoadingSpinner() {
        if (loadingSpinner.visibility == View.VISIBLE) {
            loadingSpinner.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onDestroy()
    }

    fun setPriceItem(item: PriceItem) {
        adapter.updatePriceItem(item)
    }

    fun displayError(t: Throwable) {
        Log.e(TAG, t.message)
        Toast.makeText(this, "Error getting Ticker data", Toast.LENGTH_LONG).show()
    }

    companion object {
        val TAG = "MainActivity"
    }
}
