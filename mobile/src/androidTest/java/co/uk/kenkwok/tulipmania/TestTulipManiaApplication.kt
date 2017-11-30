package co.uk.kenkwok.tulipmania

import android.app.Activity
import android.app.Application
import co.uk.kenkwok.tulipmania.dagger.DaggerTestAppComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

/**
 * Created by kekwok on 11/09/2017.
 */

class TestTulipManiaApplication : Application(), HasActivityInjector {
    @Inject
    lateinit var mActivityDispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun activityInjector(): AndroidInjector<Activity>? {
        return mActivityDispatchingAndroidInjector
    }

    override fun onCreate() {
        super.onCreate()
        DaggerTestAppComponent.builder()
                .application(this)
                .build()
                .inject(this)
    }
}