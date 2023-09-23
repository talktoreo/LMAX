package com.test.lmaxtest.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.google.android.material.snackbar.Snackbar

fun goToNextPage(
    from: Activity,
    to: Class<*>?,
    finish: Boolean,
    affinity: Boolean,
    bundle: Bundle?
) {
    val intent = Intent(from, to)
    if (bundle != null) {
        intent.putExtra("data", bundle)
    }
    from.startActivity(intent)
    if (finish) {
        from.finish()
    }
    if (affinity) {
        from.finishAffinity()
    }
}

fun isNetworkAvailable(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val networkInfo = connectivityManager.activeNetwork
    val networkCapabilities = connectivityManager.getNetworkCapabilities(networkInfo)

    return networkCapabilities != null &&
            (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                    networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
}

fun hideKeyboard(view: View?) {
    if (view == null) {
        return
    }
    try {
        val imm =
            view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (!imm.isActive) {
            return
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    } catch (e: Exception) {

    }

}

fun showNetworkErrorSnackbar(
    view: View,
    context: Context,
    retryAction: () -> Unit
) {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val networkInfo = connectivityManager.activeNetwork
    val networkCapabilities = connectivityManager.getNetworkCapabilities(networkInfo)

    val isNetworkAvailable = networkCapabilities != null &&
            (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                    networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))

    if (!isNetworkAvailable) {
        val snackbar = Snackbar.make(view, "No network connection.", Snackbar.LENGTH_INDEFINITE)
        snackbar.setAction("Retry") {
            // Retry action when the user clicks the "Retry" button
            retryAction.invoke()
        }
        snackbar.show()
    }
}