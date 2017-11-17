package co.uk.kenkwok.tulipmania.ui.main

import android.support.v7.widget.RecyclerView
import android.view.View
import butterknife.ButterKnife
import co.uk.kenkwok.tulipmania.models.PriceItem
import kotlinx.android.synthetic.main.price_item_viewholder.view.*

/**
 * Created by kwokk on 01/11/2017.
 */
class PriceItemViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    init {
        ButterKnife.bind(this, view)
    }

    fun setData(priceItem: PriceItem) {
        view.itemExchangeName.text = priceItem.exchangeName.exchange
        view.itemPrice.text = priceItem.exchangePrice
        view.item24hourHigh.text = priceItem.twentyFourHourHigh
        view.item24hourLow.text = priceItem.twentyFourHourLow
    }
}