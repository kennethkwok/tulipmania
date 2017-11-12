package co.uk.kenkwok.tulipmania.main

import android.support.v7.widget.RecyclerView
import android.view.View
import butterknife.ButterKnife
import co.uk.kenkwok.tulipmania.R
import co.uk.kenkwok.tulipmania.models.PriceItem
import kotlinx.android.synthetic.main.item_exchange_price_layout.view.*

/**
 * Created by kwokk on 01/11/2017.
 */
class PriceViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    init {
        ButterKnife.bind(this, view)
    }

    fun setData(priceItem: PriceItem, arrowIsUp: Boolean) {
        view.exchangeName.text = priceItem.exchangeName.exchange
        view.exchangeBuyPrice.text = priceItem.exchangePrice
        view.highPrice.text = priceItem.twentyFourHourHigh
        view.lowPrice.text = priceItem.twentyFourHourLow

        var arrowImageResource: Int
        if (arrowIsUp) {
            arrowImageResource = R.drawable.up_arrow
        } else {
            arrowImageResource = R.drawable.down_arrow
        }
        view.priceIndicatorImageView.setImageResource(arrowImageResource)
        view.priceIndicatorImageView.visibility = View.VISIBLE
    }
}