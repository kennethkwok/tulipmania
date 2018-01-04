package co.uk.kenkwok.tulipmania.ui.base

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

import butterknife.ButterKnife
import dagger.android.AndroidInjection
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by kekwok on 20/09/2017.
 */

abstract class BaseActivity : AppCompatActivity() {

    protected lateinit var compositeDisposable: CompositeDisposable

    open val layoutId: Int
        get() = 0

    override fun onStart() {
        super.onStart()
        compositeDisposable = CompositeDisposable()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(layoutId)
        ButterKnife.bind(this)
    }

    override fun onStop() {
        super.onStop()
        compositeDisposable.dispose()
    }
}
