package com.firozelectrical.securepay

import android.content.ClipData
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import java.io.ByteArrayOutputStream
import android.util.Base64

class MainActivity : AppCompatActivity() {

    private lateinit var web: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        web = WebView(this)

        web.settings.javaScriptEnabled = true
        web.settings.domStorageEnabled = true
        web.settings.allowFileAccess = true
        web.settings.allowContentAccess = true

        web.webViewClient = WebViewClient()
        web.webChromeClient = WebChromeClient()

        web.addJavascriptInterface(AndroidBridge(), "Android")

        web.loadUrl("file:///android_asset/index.html")

        setContentView(web)
    }

    inner class AndroidBridge {

        @JavascriptInterface
        fun generateQR(payload: String, size: Int): String {

            val safeSize = size.coerceIn(180, 900)

            val matrix = MultiFormatWriter().encode(
                payload,
                BarcodeFormat.QR_CODE,
                safeSize,
                safeSize
            )

            val bitmap = Bitmap.createBitmap(
                safeSize,
                safeSize,
                Bitmap.Config.ARGB_8888
            )

            for (x in 0 until safeSize) {
                for (y in 0 until safeSize) {
                    bitmap.setPixel(
                        x,
                        y,
                        if (matrix[x, y]) Color.BLACK else Color.WHITE
                    )
                }
            }

            val output = ByteArrayOutputStream()

            bitmap.compress(
                Bitmap.CompressFormat.PNG,
                100,
                output
            )

            return Base64.encodeToString(
                output.toByteArray(),
                Base64.NO_WRAP
            )
        }

        @JavascriptInterface
        fun copy(text: String) {

            val clipboard =
                getSystemService(CLIPBOARD_SERVICE)
                        as android.content.ClipboardManager

            clipboard.setPrimaryClip(
                ClipData.newPlainText(
                    "SecurePay",
                    text
                )
            )
        }

        @JavascriptInterface
        fun share(text: String) {

            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, text)
            }

            startActivity(
                Intent.createChooser(
                    intent,
                    "Share payment details"
                )
            )
        }
    }

    @Deprecated("Deprecated in Android API")
    override fun onBackPressed() {

        if (web.canGoBack()) {
            web.goBack()
        } else {
            super.onBackPressed()
        }
    }
}
