package co.uk.kenkwok.tulipmania.ui.main

import android.content.Context
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import co.uk.kenkwok.tulipmania.R
import co.uk.kenkwok.tulipmania.models.RecyclerViewTickerItem
import co.uk.kenkwok.tulipmania.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : BaseActivity() {
    @Inject lateinit var context: Context
    @Inject lateinit var viewModel: MainViewModel

    private lateinit var adapter: RecyclerViewAdapter

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
        initObservables()
        viewModel.onCreate()
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

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onDestroy()
    }

    fun setTickerItem(item: RecyclerViewTickerItem) {
        item.tickerItem?.let {
            adapter.updatePriceItem(item)
        }

        item.error?.let { throwable ->
            displayError(throwable)
        }
    }

    fun displayError(t: Throwable) {
        t.message?.let { exchangeName ->
            Snackbar.make(coordinatorLayout, getString(R.string.network_error, exchangeName), Snackbar.LENGTH_LONG).show()
        }
    }

    companion object {
        val TAG = "MainActivity"
    }
}
