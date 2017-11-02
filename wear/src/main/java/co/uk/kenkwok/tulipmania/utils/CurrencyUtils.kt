package co.uk.kenkwok.tulipmania.utils

/**
 * Created by kwokk on 02/11/2017.
 */
class CurrencyUtils {
    companion object {
        fun convertDisplayCurrency(displayCurrency: String) : String {
            if (displayCurrency.endsWith(" usd", true) ||
                    displayCurrency.endsWith(" hkd", true)) {
                val strList = displayCurrency.split(" ")
                return "$".plus(strList[0])
            } else {
                return displayCurrency
            }
        }
    }
}