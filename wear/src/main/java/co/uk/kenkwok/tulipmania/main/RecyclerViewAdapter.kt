package co.uk.kenkwok.tulipmania.main

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import co.uk.kenkwok.tulipmania.R
import co.uk.kenkwok.tulipmania.models.ExchangeName
import co.uk.kenkwok.tulipmania.models.PriceItem

/**
 * Created by kwokk on 01/11/2017.
 */
class RecyclerViewAdapter() : RecyclerView.Adapter<PriceViewHolder>() {
    private var isAnxArrowUp = false
    private var isBitstampArrowUp = false
    private val tickerList = ArrayList<PriceItem>()

    fun updateAnxTicker(priceItem: PriceItem) {
        isAnxArrowUp = false
        val index = getPriceItemWithExchange(priceItem.exchangeName)

        if (index >= 0) {
            val oldTicker = tickerList.get(index)

            if (getExchangePrice(oldTicker.exchangePrice) <
                    getExchangePrice(priceItem.exchangePrice)) {
                isAnxArrowUp = true
            }

            tickerList.set(index, priceItem)
            notifyItemChanged(index)
        } else {
            tickerList.add(priceItem)
            notifyItemChanged(tickerList.size - 1)
        }
    }

    fun updateBitstampTicker(priceItem: PriceItem) {
        isBitstampArrowUp = false
        val index = getPriceItemWithExchange(priceItem.exchangeName)

        if (index >= 0) {
            val oldTicker = tickerList.get(index)

            if (getExchangePrice(oldTicker.exchangePrice) <
                    getExchangePrice(priceItem.exchangePrice)) {
                isBitstampArrowUp = true
            }

            tickerList.set(index, priceItem)
            notifyItemChanged(index)
        } else {
            tickerList.add(priceItem)
            notifyItemChanged(tickerList.size - 1)
        }
    }

    override fun onBindViewHolder(holder: PriceViewHolder, position: Int) {
        if (tickerList.get(position).exchangeName == ExchangeName.ANXPRO) {
            holder.setData(tickerList.get(position),
                    arrowIsUp = isAnxArrowUp)
        } else {
            holder.setData(tickerList.get(position),
                    arrowIsUp = isBitstampArrowUp)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PriceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_exchange_price_layout, parent, false)
        return PriceViewHolder(view)
    }

    override fun getItemCount(): Int {
        return tickerList.size
    }

    /**
     * Retrieves the PriceItem with the supplied exchange enum.
     * Returns the index of the item, or -1 if item does not exist.
     */
    private fun getPriceItemWithExchange(name: ExchangeName): Int {
        tickerList.forEachIndexed { index, priceItem ->
            if (priceItem.exchangeName == name) {
                return index
            }
        }
        return -1
    }

    /**
     * Removes the leading currency sign so price string can be converted in to a Double
     * for comparison purposes
     */
    private fun getExchangePrice(price: String): Double {
        val output = price.replace(",", "")
        return output.substring(1, price.lastIndex).toDouble()
    }
}