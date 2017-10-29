package co.uk.kenkwok.tulipmania.ui.base

import io.reactivex.disposables.CompositeDisposable

/**
 * Created by kekwok on 18/09/2017.
 */

abstract class BaseViewModel {
    open var compositeDisposable: CompositeDisposable? = null

    open fun onCreate() {
        compositeDisposable = CompositeDisposable()
    }

    open fun onResume() {}

    open fun onPause() {}

    open fun onStop() {}

    open fun onDestroy() {
        compositeDisposable?.dispose()
    }
}
