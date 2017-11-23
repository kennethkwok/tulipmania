package co.uk.kenkwok.tulipmania.utils

import android.content.Context
import co.uk.kenkwok.tulipmania.BuildConfig
import co.uk.kenkwok.tulipmania.models.ApiCredentials

/**
 * Created by kekwok on 18/09/2017.
 */

object ANXCredentialUtils {
    fun getApiCredentials(context: Context): ApiCredentials {
        return ApiCredentials(
                apiKey = BuildConfig.ANX_KEY,
                apiSecret = BuildConfig.ANX_SECRET
        )
    }
}