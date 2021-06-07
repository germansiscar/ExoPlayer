package com.example.exoplayerfunctionality

import android.app.Dialog
import android.content.Context
import android.view.Window
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.custom_dialog.*
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class Utils {

    private var pausedCount : Int = 0
    private var resumeCount : Int = 0
    private var elapsedTime : Int = 0
    private var allElapsedTime : Int = 0
    private var timer : Timer = Timer()
    private var firstPlay : Boolean = true
    private lateinit var data : Map<String, String>

    fun increasePauseCounter(){
        pausedCount++
        startTimer()
    }

    fun getPauseCounter(): Int {
        return pausedCount
    }

    fun increaseResumeCounter(){
        if (!firstPlay) {
            resumeCount++
            stopTimer()
        } else{
            firstPlay = false
        }
    }

    fun getResumeCounter(): Int {
        return resumeCount
    }

    fun startTimer(){
        timer = Timer()
        elapsedTime = 0
        timer.scheduleAtFixedRate(
            object : TimerTask() {
                override fun run() {
                    elapsedTime++
                    allElapsedTime++
                }
            },
            0,
            1000
        )
    }

    fun stopTimer(){
        timer!!.cancel()
    }

    fun getElapsedTime(): Int {
        return elapsedTime
    }

    fun getAllElapsedTime(): Int {
        return allElapsedTime
    }

    fun getAllData(): Map<String, String> {
        data = mapOf("pauseCount" to pausedCount.toString(), "resumeCount" to resumeCount.toString(), "elapsedTime" to  elapsedTime.toString(), "allElapsedTime" to  allElapsedTime.toString())
        return data
    }

    fun resetData(){
        pausedCount = 0
        resumeCount = 0
        elapsedTime = 0
        allElapsedTime = 0
    }

    fun startedVideo(context : Context){
        var map = mapOf("videoStart" to "true")
        makeHTTPRequest(context, map,connecionController.routes().urlFirstFrame)
    }

    fun firstFrame(context : Context){
        var map = mapOf("fistFrame" to "true")
        makeHTTPRequest(context, map,connecionController.routes().urlFirstFrame)
    }

    fun finishedVideo(context : Context){
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.custom_dialog)
        dialog.txtTitle.text = "Finished Video"

        dialog.txtBody.text = String.format("the video has ended with\n" +
                "times paused:%s \n" +
                "times resumed:%s \n" +
                "time between pause/resume:%s \n" +
                "all time between pause/resume:%s",pausedCount,resumeCount,elapsedTime,allElapsedTime)
        dialog.btnConfirm.setOnClickListener {
            dialog.dismiss()
            makeHTTPRequest(context, getAllData(), connecionController.routes().urlFinishVideo)
            resetData()
        }

        dialog.show()
    }

    fun makeHTTPRequest(context: Context, map : Map<String, String>, url : String){

        val stringRequest = object: StringRequest(
            Request.Method.GET,
            url,

            Response.Listener<String> { s ->
                try {
                    val obj = JSONObject(s)

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error: VolleyError? ->
                Toast.makeText(context, error?.toString(), Toast.LENGTH_LONG).show()
            })
        {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers.put("Content-Type", "application/json");
                headers.put("language", Locale.getDefault().language)
                return headers
            }

            override fun getParams(): MutableMap<String, String> {
                return map as MutableMap<String, String>
            }
        }

        val requestQueue = Volley.newRequestQueue(context)
        requestQueue.add<String>(stringRequest)
    }
}