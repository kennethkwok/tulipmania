package co.uk.kenkwok.tulipmania.utils

import android.content.Context
import co.uk.kenkwok.tulipmania.models.ApiCredentials

/**
 * Created by kekwok on 18/09/2017.
 */

class ANXCredentialUtils {

    companion object {
        fun getApiCredentials(context: Context): ApiCredentials {
            return ApiCredentials(
                    apiKey = System.getenv("ANX_KEY"),
                    apiSecret = System.getenv("ANX_SECRET")
            )
        }
    }
}