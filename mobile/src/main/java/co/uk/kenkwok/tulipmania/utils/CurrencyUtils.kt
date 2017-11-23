package co.uk.kenkwok.tulipmania.utils

import java.text.DecimalFormat

/**
 * Created by kwokk on 17/11/2017.
 */
object CurrencyUtils {
    private val df = DecimalFormat("###,###.00")

    /**
     * Converts amount returned by ticker API to preferred DecimalFormat string.
     * Currently only supports USD or HKD.
     */
    fun convertDisplayCurrency(displayCurrency: String) : String {
        if (displayCurrency.endsWith(" usd", true) ||
                displayCurrency.endsWith(" hkd", true)) {
            val strList = displayCurrency.split(" ")
            return formatSeparatorsAndDecimals(strList[0])
        } else {
            return formatSeparatorsAndDecimals(displayCurrency)
        }
    }

    /**
     * Converts a string numerical amount such as 1234 to 1,234.00
     */
    private fun formatSeparatorsAndDecimals(amount: String): String {
        var amount = amount.replace(",", "")
        return "$".plus(df.format(amount.toDouble()))
    }
}