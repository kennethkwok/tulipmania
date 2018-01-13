package co.uk.kenkwok.tulipmania.ui.main

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.uk.kenkwok.tulipmania.R
import co.uk.kenkwok.tulipmania.models.CryptoType
import co.uk.kenkwok.tulipmania.models.ExchangeName
import co.uk.kenkwok.tulipmania.models.PriceItem
import co.uk.kenkwok.tulipmania.models.RecyclerViewTickerItem
import co.uk.kenkwok.tulipmania.ui.base.BaseViewHolder
import kotlinx.android.synthetic.main.price_item_viewholder.view.*
import kotlinx.android.synthetic.main.section_heading_viewholder.view.*

/**
 * Created by kwokk on 17/11/2017.
 */
class RecyclerViewAdapter(private var exchangeList: ArrayList<RecyclerViewTickerItem>) : RecyclerView.Adapter<BaseViewHolder>() {

    fun updatePriceItem(item: RecyclerViewTickerItem) {
        val index = containsExchangeName(item.tickerItem?.exchangeName, item.tickerItem?.cryptoType)

        if (index < 0) {
            exchangeList.add(item)
            notifyItemInserted(itemCount - 1)
        } else {
            exchangeList[index] = item
            notifyItemChanged(index)
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        if (holder is PriceItemViewHolder) {
            exchangeList[position].tickerItem?.let { ticker -> holder.setData(ticker) }
        } else if (holder is SectionHeadingViewHolder) {
            exchangeList[position].sectionHeading?.let { heading -> holder.setData(heading) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return if (viewType == 1) {
            PriceItemViewHolder(parent.context, LayoutInflater.from(parent.context).inflate(R.layout.price_item_viewholder, parent, false))
        } else {
            SectionHeadingViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.section_heading_viewholder, parent, false))
        }
    }

    override fun getItemCount(): Int {
        return exchangeList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (exchangeList[position].sectionHeading.isNotEmpty()) { 0 } else { 1 }
    }

    private fun containsExchangeName(name: ExchangeName?, type: CryptoType?): Int {
        if (exchangeList.size == 0) {
            return -1
        }

        for ((i, item) in exchangeList.withIndex()) {
            if ((item.tickerItem?.exchangeName == name) &&
                    (item.tickerItem?.cryptoType == type)) {
                return i
            }
        }

        return -1
    }

    class PriceItemViewHolder(val context: Context, view: View): BaseViewHolder(view) {
        fun setData(priceItem: PriceItem) {
            view.itemExchangeName.text = priceItem.exchangeName.exchange
            view.itemPrice.text = priceItem.exchangePrice
            view.item24hourHigh.text = context.getString(R.string.twenty_four_hour_high, priceItem.twentyFourHourHigh)
            view.item24hourLow.text = context.getString(R.string.twenty_four_hour_low, priceItem.twentyFourHourLow)
        }
    }

    class SectionHeadingViewHolder(view: View): BaseViewHolder(view) {
        fun setData(heading: String) {
            view.sectionHeading.text = heading
        }
    }
}