package co.uk.kenkwok.tulipmania

import android.app.Activity
import android.app.Application
import android.app.Service
import android.content.Intent
import co.uk.kenkwok.tulipmania.dagger.DaggerAppComponent
import co.uk.kenkwok.tulipmania.service.BitfinexService
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.HasServiceInjector
import javax.inject.Inject

/**
 * Created by kekwok on 11/09/2017.
 */

class TulipManiaApplication : Application(), HasActivityInjector, HasServiceInjector {
    @Inject lateinit var mActivityDispatchingAndroidInjector: DispatchingAndroidInjector<Activity>
    @Inject lateinit var mServiceDispatchingAndroidInjector: DispatchingAndroidInjector<Service>

    override fun activityInjector(): AndroidInjector<Activity>? {
        return mActivityDispatchingAndroidInjector
    }

    override fun serviceInjector(): AndroidInjector<Service> {
        return mServiceDispatchingAndroidInjector
    }

    override fun onCreate() {
        super.onCreate()
        DaggerAppComponent
                .builder()
                .application(this)
                .build()
                .inject(this)

        startService(Intent(this, BitfinexService::class.java))
    }


}
