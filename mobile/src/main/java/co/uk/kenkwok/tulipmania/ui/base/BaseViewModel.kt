package co.uk.kenkwok.tulipmania.ui.base

import io.reactivex.disposables.CompositeDisposable

/**
 * Created by kekwok on 18/09/2017.
 */

abstract class BaseViewModel {
    open val TAG = "BaseViewModel"

    open lateinit var compositeDisposable: CompositeDisposable

    open fun onCreate() {
    }

    open fun onStart() {
        compositeDisposable = CompositeDisposable()
    }

    open fun onResume() {}

    open fun onPause() {}

    open fun onStop() {
        compositeDisposable.dispose()
    }

    open fun onDestroy() {}
}
