package com.example.exoplayerfunctionality

import android.app.Application
import android.text.TextUtils
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley

class connecionController : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    val requestQueue : RequestQueue? = null
        get() {
            if (field == null) {
                return Volley.newRequestQueue(applicationContext)
            }
            return field
        }

    fun <T> addToRequestQueue(request: Request<T>, tag: String) {
        request.tag = if (TextUtils.isEmpty(tag)) TAG else tag
        requestQueue?.add(request)
    }

    fun <T> addToRequestQueue(request: Request<T>) {
        request.tag = TAG
        requestQueue?.add(request)
    }
    fun cancelPendingRequests(tag: Any) {
        if (requestQueue != null) {
            requestQueue!!.cancelAll(tag)
        }
    }

    companion object {
        private val TAG = connecionController::class.java.simpleName
        @get:Synchronized var instance: connecionController? = null
            private set
    }


    class routes{
        val URL_ROOT : String ="http://10.0.3.2:3001"
        val urlFirstPlay= URL_ROOT + "/FirstPlay"
        val urlFirstFrame= URL_ROOT + "/firstFrame"
        val urlFinishVideo= URL_ROOT + "/finishVideo"
    }
}