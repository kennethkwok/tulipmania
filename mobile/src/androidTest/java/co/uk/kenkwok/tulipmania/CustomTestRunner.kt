package co.uk.kenkwok.tulipmania

import android.app.Application
import android.content.Context
import android.support.test.runner.AndroidJUnitRunner

/**
 * Created by kwokk on 29/11/2017.
 */
class CustomTestRunner: AndroidJUnitRunner() {

    override fun newApplication(cl: ClassLoader, className: String, context: Context): Application {
        return super.newApplication(cl, TestTulipManiaApplication::class.java.name, context)
    }
}