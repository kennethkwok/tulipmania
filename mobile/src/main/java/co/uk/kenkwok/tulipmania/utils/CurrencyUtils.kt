package co.uk.kenkwok.tulipmania.utils

import java.text.DecimalFormat

/**
 * Created by kwokk on 17/11/2017.
 */
class CurrencyUtils {
    companion object {
        fun convertDisplayCurrency(displayCurrency: String) : String {
            if (displayCurrency.endsWith(" usd", true) ||
                    displayCurrency.endsWith(" hkd", true)) {
                val strList = displayCurrency.split(" ")
                return "$".plus(formatSeparatorsAndDecimals(strList[0]))
            } else {
                return formatSeparatorsAndDecimals(displayCurrency)
            }
        }

        /**
         * Converts a string numerical amount such as 1234 to 1,234.00
         */
        private fun formatSeparatorsAndDecimals(amount: String): String {
            var amount = amount.replace(",", "")
            return DecimalFormat("###,###.00").format(amount.toDouble())
        }
    }
}