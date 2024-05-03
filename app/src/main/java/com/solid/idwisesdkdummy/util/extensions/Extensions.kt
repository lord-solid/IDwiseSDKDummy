package com.solid.idwisesdkdummy.util.extensions

import android.content.Context
import android.view.View
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView

fun View.visibleIf(value: Boolean) {
    if (value) visible() else gone()
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun Context.inflateDialog(views: View, cancelable: Boolean = false): MaterialDialog {
    return MaterialDialog(this).show {
        customView(view = views)
        cornerRadius(16F)
        cancelOnTouchOutside(cancelable)
        cancelable(cancelable)
    }
}

fun Context.showDialog(
    title: String = "Error",
    message: String = "",
    positiveTitle: String = "Ok",
    negativeTitle: String? = "",
    dismiable: Boolean = false,
    negativeCallback: (() -> Unit)? = null,
    positiveCallback: (() -> Unit)? = null
) {

    MaterialDialog(this).show {


        cornerRadius(5F)
        title(text = title)
        message(text = message)
        cancelable(dismiable)

        positiveButton(text = positiveTitle) { dialog ->
            positiveCallback?.invoke()
        }

        negativeButton(text = negativeTitle) {
            negativeCallback?.invoke()
            dismiss()
        }
    }
}