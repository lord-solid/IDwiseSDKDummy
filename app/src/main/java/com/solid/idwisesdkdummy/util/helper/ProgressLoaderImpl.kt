package com.solid.idwisesdkdummy.util.helper

import android.app.ProgressDialog
import android.content.Context
import com.solid.idwisesdkdummy.R
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject

class ProgressLoaderImpl @Inject constructor(@ActivityContext context: Context) :
    ProgressLoader {

    private var mProgressDialog = ProgressDialog(
        context,
        R.style.customDialog,
    )

    override fun show(message: String?, cancellable: Boolean) {
        message?.let {
            mProgressDialog.setMessage(message)
        }
        mProgressDialog.isIndeterminate = true
        mProgressDialog.setCancelable(cancellable)
        mProgressDialog.setCanceledOnTouchOutside(cancellable)
        mProgressDialog.show()
    }

    override fun hide() {
        if (mProgressDialog.isShowing) {
            mProgressDialog.dismiss()
        }
    }

    override fun isShowing(): Boolean = mProgressDialog.isShowing
}