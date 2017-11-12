package co.uk.kenkwok.tulipmania.main

import android.support.v7.widget.RecyclerView
import android.view.View
import butterknife.ButterKnife
import co.uk.kenkwok.tulipmania.R
import co.uk.kenkwok.tulipmania.models.CurrencyPair
import co.uk.kenkwok.tulipmania.utils.CurrencyUtils
import kotlinx.android.synthetic.main.item_exchange_price_layout.view.*

/**
 * Created by kwokk on 01/11/2017.
 */
class PriceViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    init {
        ButterKnife.bind(this, view)
    }

    fun setData(exchangeLabel: String, currencyPair: CurrencyPair, arrowIsUp: Boolean) {
        view.exchangeName.text = exchangeLabel
        view.exchangeBuyPrice.text = CurrencyUtils.convertDisplayCurrency(currencyPair.buy.displayShort)
        view.highPrice.text = CurrencyUtils.convertDisplayCurrency(currencyPair.high.displayShort)
        view.lowPrice.text = CurrencyUtils.convertDisplayCurrency(currencyPair.low.displayShort)

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