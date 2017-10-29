package co.uk.kenkwok.tulipmania.ui.main

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import butterknife.OnCheckedChanged
import co.uk.kenkwok.tulipmania.R
import co.uk.kenkwok.tulipmania.models.CurrencyPair
import co.uk.kenkwok.tulipmania.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : BaseActivity() {
    @Inject
    lateinit var context: Context

    @Inject
    lateinit var viewModel: MainViewModel

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

    private fun initObservables() {
        compositeDisposable.add(
                viewModel.btcTickerObservable
                        .subscribe({ btcTicker -> setTicker(btcTicker) }) { displayError() })
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onDestroy()
    }

    fun setTicker(btcTicker: CurrencyPair) {
        buyTextView.text = btcTicker.buy.displayShort
        sellTextView.text = btcTicker.sell.displayShort
        lastTransactionTextView.text = btcTicker.last.displayShort
        dailyLowTextView.text = btcTicker.low.displayShort
        dailyHighTextView.text = btcTicker.high.displayShort
    }

    fun displayError() {
        Toast.makeText(this, "Error getting Ticker data", Toast.LENGTH_LONG).show()
    }

    @OnCheckedChanged(R.id.hkdSwitch)
    fun hkdSwitchToggled(isChecked: Boolean) {
        viewModel.displayHkdToggled(isChecked)
    }

    companion object {
        val NOTIFICATION_ID = 100
    }

}
