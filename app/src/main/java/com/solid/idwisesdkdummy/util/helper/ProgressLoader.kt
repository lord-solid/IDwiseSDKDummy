package com.solid.idwisesdkdummy.util.helper

interface ProgressLoader {
    fun show(message: String? = null, cancellable: Boolean = false)
    fun hide()
    fun isShowing(): Boolean
}