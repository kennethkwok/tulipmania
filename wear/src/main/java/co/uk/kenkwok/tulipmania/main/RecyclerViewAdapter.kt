package co.uk.kenkwok.tulipmania.main

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import co.uk.kenkwok.tulipmania.R
import co.uk.kenkwok.tulipmania.models.Ticker

/**
 * Created by kwokk on 01/11/2017.
 */
class RecyclerViewAdapter(val tickerList: MutableList<Ticker>) : RecyclerView.Adapter<PriceViewHolder>() {
    private var isArrowUp = false

    fun updateTicker(ticker: Ticker) {
        isArrowUp = false

        if (tickerList.size > 0) {
            val oldTicker = tickerList.get(0)

            if (oldTicker.data.btcusd.buy.value.toDouble() <
                    ticker.data.btcusd.buy.value.toDouble()) {
                isArrowUp = true
            }
        }

        tickerList.clear()
        tickerList.add(ticker)
    }

    override fun onBindViewHolder(holder: PriceViewHolder, position: Int) {
        holder.setData(exchangeLabel = "ANXPro",
                currencyPair = tickerList.get(0).data.btcusd,
                arrowIsUp = isArrowUp)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PriceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_exchange_price_layout, parent, false)
        return PriceViewHolder(view)
    }

    override fun getItemCount(): Int {
        return tickerList.size
    }
}