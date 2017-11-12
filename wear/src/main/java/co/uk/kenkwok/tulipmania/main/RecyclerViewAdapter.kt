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
    private val tickerList = ArrayList<PriceItem>()

    fun updateAnxTicker(priceItem: PriceItem) {
        isAnxArrowUp = false
        val index = getPriceItemWithExchange(priceItem.exchangeName)

        if (index >= 0) {
            val oldTicker = tickerList.get(index)

            if (oldTicker.exchangePrice.toDouble() <
                    priceItem.exchangePrice.toDouble()) {
                isAnxArrowUp = true
            }

            tickerList.set(index, priceItem)
            notifyItemChanged(index)
        } else {
            tickerList.add(0, priceItem)
            notifyItemChanged(0)
        }
    }

    override fun onBindViewHolder(holder: PriceViewHolder, position: Int) {
        holder.setData(tickerList.get(position),
                arrowIsUp = isAnxArrowUp)
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
}