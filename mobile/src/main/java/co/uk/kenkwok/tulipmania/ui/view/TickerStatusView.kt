package co.uk.kenkwok.tulipmania.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import co.uk.kenkwok.tulipmania.R
import co.uk.kenkwok.tulipmania.models.ConnectionStatus
import kotlinx.android.synthetic.main.ticker_status_view.view.*

/**
 * Created by kekwok on 23/01/2018.
 */
class TickerStatusView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyle: Int = 0,
        defStyleRes: Int = 0) : LinearLayout(context, attrs, defStyle, defStyleRes) {

    var connectionStatus = ConnectionStatus.DISCONNECTED
    set(value) {
        field = value
        updateTickerStatus()
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.ticker_status_view, this, true)
        orientation = VERTICAL

        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.ConnectionStatus, 0, 0)
            val state = typedArray.getInteger(R.styleable.ConnectionStatus_connection_state, 2)

            connectionStatus = when (state) {
                0 -> ConnectionStatus.CONNECTED
                1 -> ConnectionStatus.CONNECTING
                else -> ConnectionStatus.DISCONNECTED
            }

            typedArray.recycle()
        }

        updateTickerStatus()
    }

    private fun updateTickerStatus() {
        when (connectionStatus) {
            ConnectionStatus.CONNECTED -> {
                tickerStatusCircle.setBackgroundResource(R.drawable.ticker_status_connected)
                tickerStatusText.text = context.getText(R.string.status_connected)
            }
            ConnectionStatus.CONNECTING -> {
                tickerStatusCircle.setBackgroundResource(R.drawable.ticker_status_connecting)
                tickerStatusText.text = context.getText(R.string.status_connecting)
            }
            ConnectionStatus.DISCONNECTED -> {
                tickerStatusCircle.setBackgroundResource(R.drawable.ticker_status_disconnected)
                tickerStatusText.text = context.getText(R.string.status_disconnected)
            }
        }
    }
}