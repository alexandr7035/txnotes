package by.alexandr7035.txnotes.core.extensions

import timber.log.Timber

fun Timber.Forest.debug(message: String) {
    tag("DEBUG_TAG").d(message)
}