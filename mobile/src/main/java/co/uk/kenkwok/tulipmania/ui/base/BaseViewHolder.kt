package co.uk.kenkwok.tulipmania.ui.base

import android.support.v7.widget.RecyclerView
import android.view.View
import butterknife.ButterKnife

/**
 * Created by kwokk on 30/11/2017.
 */
open class BaseViewHolder(val view: View): RecyclerView.ViewHolder(view) {
    init {
        ButterKnife.bind(this, view)
    }
}