package com.example.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

object IntentHelpers {

    const val PHONE_NUMBER = "+254785638731"
    const val TIKTOK_URL = "https://vm.tiktok.com/ZS9kLJ2fgyenf-KmMBF/"
    const val STORE_LOCATION_QUERY = "Athi River Opposite Mavuno Fertilizer"
    const val STORE_ADDRESS = "Athi River, Opposite Mavuno Fertilizer, Machakos County, Kenya"

    fun openWhatsApp(context: Context, customMessage: String? = null) {
        val defaultMsg = "Hi TREK Athi River, I want to enquire about bicycles."
        val messageToSend = customMessage ?: defaultMsg
        val encodedMsg = try {
            URLEncoder.encode(messageToSend, StandardCharsets.UTF_8.toString())
        } catch (_: Exception) {
            ""
        }
        val url = "https://wa.me/254785638731?text=$encodedMsg"

        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(url)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        try {
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "Could not open WhatsApp. Phone: $PHONE_NUMBER", Toast.LENGTH_LONG).show()
        }
    }

    fun openDialer(context: Context) {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$PHONE_NUMBER")
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        try {
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "Call: $PHONE_NUMBER", Toast.LENGTH_LONG).show()
        }
    }

    fun openTikTok(context: Context) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(TIKTOK_URL)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        try {
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "Opening TikTok...", Toast.LENGTH_SHORT).show()
        }
    }

    fun openGoogleMaps(context: Context) {
        val encoded = try {
            URLEncoder.encode(STORE_LOCATION_QUERY, StandardCharsets.UTF_8.toString())
        } catch (_: Exception) {
            "Athi+River"
        }
        val gmmIntentUri = Uri.parse("geo:0,0?q=$encoded")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri).apply {
            setPackage("com.google.android.apps.maps")
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        try {
            context.startActivity(mapIntent)
        } catch (e: Exception) {
            // Fallback to web browser maps
            val browserUri = Uri.parse("https://maps.google.com/?q=$encoded")
            val browserIntent = Intent(Intent.ACTION_VIEW, browserUri).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            try {
                context.startActivity(browserIntent)
            } catch (e2: Exception) {
                Toast.makeText(context, "Store is located at $STORE_ADDRESS", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun shareBike(context: Context, bikeName: String, price: String, specs: String) {
        val shareText = "Check out this $bikeName for $price at TREK Bicycle Stores Athi River (Opposite Mavuno Fertilizer). Price is negotiable!\nContact: $PHONE_NUMBER\nSpecs: $specs"
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shareText)
            type = "text/plain"
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        try {
            context.startActivity(Intent.createChooser(sendIntent, "Share Bike via").apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            })
        } catch (_: Exception) {
            // ignore
        }
    }
}
