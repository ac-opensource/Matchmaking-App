package com.youniversals.playupgo.util

import android.content.Context
import android.content.Intent
import android.net.Uri


/**
 *
 *
 * YOYO HOLDINGS
 * @author A-Ar Andrew Concepcion
 * @version
 * @since 08/02/2017
 */

class SocialUtils {
    companion object {
        fun viewFacebookProfile(context: Context, profileId: String) {
            try {
                context.packageManager.getPackageInfo("com.facebook.katana", 0) //Checks if FB is even installed.
                val versionCode = context.packageManager.getPackageInfo("com.facebook.katana", 0).versionCode
                val activated = context.packageManager.getApplicationInfo("com.facebook.katana", 0).enabled
                if (activated) {
                    var url = "fb://profile/$profileId"
                    if ((versionCode >= 3002850)) {
                        url = "fb://facewebmodal/f?href=https://www.facebook.com/$profileId"
                    }
                    context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url))) //Trys to make intent with FB's URI)
                    return
                }
                context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/$profileId"))) //catches and opens a url to the desired page
            } catch (e: Exception) {
                context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/$profileId"))) //catches and opens a url to the desired page
            }
        }
    }
}

