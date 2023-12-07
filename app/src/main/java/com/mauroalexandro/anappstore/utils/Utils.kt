package com.mauroalexandro.anappstore.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mauroalexandro.anappstore.R
import com.mauroalexandro.anappstore.models.App
import com.mauroalexandro.anappstore.views.DetailsFragment
import java.lang.reflect.Type

class Utils {
    companion object {
        private var INSTANCE: Utils? = null
        fun getInstance(): Utils {
            if (INSTANCE == null)
                INSTANCE = Utils()

            return INSTANCE as Utils
        }
    }

    /**
     * Show DetailsFragment
     */
    fun openDetailsFragment(activity: FragmentActivity, app: App, utils: Utils) {
        val detailsFragment = DetailsFragment(activity, app, utils)
        if(!activity.supportFragmentManager.isDestroyed)
            detailsFragment.show(
                activity.supportFragmentManager,
                "app_detail_dialog_fragment"
            )
    }

    /**
     * Show Alert Dialog that in demo mode Download Cannot be done
     */
    fun showAlertDialog(activity: FragmentActivity) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
        builder
            .setTitle(activity.resources.getString(R.string.app_detail_download_dialog_title_text))
            .setMessage(activity.resources.getString(R.string.app_detail_download_dialog_message_text))
            .setPositiveButton(activity.resources.getString(R.string.app_detail_download_dialog_positive_button_text)) { _, _ ->
                //dismiss
            }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}