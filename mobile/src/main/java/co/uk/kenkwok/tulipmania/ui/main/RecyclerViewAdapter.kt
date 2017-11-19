package co.uk.kenkwok.tulipmania.ui.main

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.ButterKnife
import co.uk.kenkwok.tulipmania.R
import co.uk.kenkwok.tulipmania.models.ExchangeName
import co.uk.kenkwok.tulipmania.models.PriceItem
import kotlinx.android.synthetic.main.price_item_viewholder.view.*

/**
 * Created by kwokk on 17/11/2017.
 */
class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerViewAdapter.PriceItemViewHolder>() {
    private var exchangeList = ArrayList<PriceItem>()

    fun updatePriceItem(item: PriceItem) {
        val index = containsExchangeName(item.exchangeName)

        if (index < 0) {
            exchangeList.add(0, item)
            notifyItemInserted(0)
        } else {
            exchangeList.set(index, item)
            notifyItemChanged(index)
        }
    }

    override fun onBindViewHolder(holder: PriceItemViewHolder, position: Int) {
        holder.setData(exchangeList.get(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PriceItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.price_item_viewholder, parent, false)
        return PriceItemViewHolder(parent.context, view)
    }

    override fun getItemCount(): Int {
        return exchangeList.size
    }

    private fun containsExchangeName(name: ExchangeName): Int {
        if (exchangeList.size == 0) {
            return -1
        }

        for ((i, item) in exchangeList.withIndex()) {
            if (item.exchangeName == name) {
                return i
            }
        }

        return -1
    }

    class PriceItemViewHolder(val context: Context, val view: View) : RecyclerView.ViewHolder(view) {
        init {
            ButterKnife.bind(this, view)
        }

        fun setData(priceItem: PriceItem) {
            view.itemExchangeName.text = priceItem.exchangeName.exchange
            view.itemPrice.text = priceItem.exchangePrice
            view.item24hourHigh.text = context.getString(R.string.twenty_four_hour_high, priceItem.twentyFourHourHigh)
            view.item24hourLow.text = context.getString(R.string.twenty_four_hour_low, priceItem.twentyFourHourLow)
        }
    }
}