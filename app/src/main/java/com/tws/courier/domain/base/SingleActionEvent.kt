package com.tws.courier.domain.base

/**
 * Used as a wrapper for plotData that is exposed via a LiveData that represents an event.
 */
open class SingleActionEvent<out T>(private val content: T) {

    var hasBeenHandled = false

        private set // Allow external read but not write

    /**
     * Returns the content and prevents its use again.
     */
    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    /**
     * Returns the content, even if it's already been handled.
     */
    fun peekContent(): T = content
}