package co.uk.kenkwok.tulipmania.utils

import junit.framework.Assert.assertEquals
import org.junit.Test

/**
 * Created by kwokk on 23/11/2017.
 */
class CurrencyUtilsTest {
    @Test
    fun testCurrencyDisplayANX() {
        assertEquals(CurrencyUtils.convertDisplayCurrency("1.00 USD"), "$1.00")
        assertEquals(CurrencyUtils.convertDisplayCurrency("1.23 USD"), "$1.23")
        assertEquals(CurrencyUtils.convertDisplayCurrency("1,000.34 USD"), "$1,000.34")
        assertEquals(CurrencyUtils.convertDisplayCurrency("30,000.34 HKD"), "$30,000.34")
    }

    @Test
    fun testCurrencyDisplayBitstamp() {
        assertEquals(CurrencyUtils.convertDisplayCurrency("1.00"), "$1.00")
        assertEquals(CurrencyUtils.convertDisplayCurrency("2000.12"), "$2,000.12")
        assertEquals(CurrencyUtils.convertDisplayCurrency("1234.1"), "$1,234.10")
        assertEquals(CurrencyUtils.convertDisplayCurrency("234"), "$234.00")
    }
}