package com.alexetnico.rxvscoroutines.utils

import android.util.Log
import com.google.gson.Gson
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy

const val TAG = "MyAppRx"

fun Completable.subscribeBy(
    onError: (Throwable) -> Unit,
    onComplete: () -> Unit
): Disposable = this
    .doOnError { it.logRxOnError() }
    .subscribeBy(onError, onComplete)

fun <T : Any> Single<T>.subscribeBy(
    onError: (Throwable) -> Unit,
    onSuccess: (T) -> Unit
): Disposable = this
    .doOnSuccess { it.logRxOnSuccess() }
    .doOnError { it.logRxOnError() }
    .subscribeBy(onError, onSuccess)

fun <T : Any> Observable<T>.subscribeBy(
    onError: (Throwable) -> Unit,
    onComplete: () -> Unit,
    onNext: (T) -> Unit
): Disposable = this
    .doOnNext { it.logRxOnNext() }
    .doOnError { it.logRxOnError() }
    .subscribeBy(onError, onComplete, onNext)

fun <T : Any> Maybe<T>.subscribeBy(
    onError: (Throwable) -> Unit,
    onComplete: () -> Unit,
    onSuccess: (T) -> Unit
): Disposable = this
    .doOnSuccess { it.logRxOnSuccess() }
    .doOnError { it.logRxOnError() }
    .subscribeBy(onError, onComplete, onSuccess)


private fun Any.logRxOnSuccess() = Log.d("$TAG onSuccess", toReadableResult())

private fun Any.logRxOnNext() = Log.d("$TAG onNext", toReadableResult())

private fun Throwable.logRxOnError() = Log.d("$TAG OnError", message, cause)

private fun Any.toReadableResult() =
    "${if (this is List<*> && this.isNotEmpty()) "${javaClass.simpleName}<${this.first()?.javaClass?.simpleName}>"
    else javaClass.simpleName} - ${Gson().toJson(this)}"
