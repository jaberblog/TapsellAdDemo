package ir.jaber.tapselladdemo

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ir.tapsell.plus.AdHolder
import ir.tapsell.plus.AdRequestCallback
import ir.tapsell.plus.AdShowListener
import ir.tapsell.plus.TapsellPlus
import ir.tapsell.plus.model.TapsellPlusAdModel
import ir.tapsell.plus.model.TapsellPlusErrorModel

class MainActivity : AppCompatActivity() {

    private lateinit var adContainer: FrameLayout
    private lateinit var refreshButton: Button
    private lateinit var progress: ProgressBar
    private lateinit var statusText: TextView

    private var adHolder: AdHolder? = null
    private var responseId: String? = null
    private var isLoading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        adContainer = findViewById(R.id.adContainer)
        refreshButton = findViewById(R.id.refreshButton)
        progress = findViewById(R.id.progress)
        statusText = findViewById(R.id.statusText)

        refreshButton.setOnClickListener { loadAd() }

        // load the first ad automatically
        loadAd()
    }

    /** Destroys the current ad (if any) and requests a fresh one. */
    private fun loadAd() {
        if (isLoading) return
        isLoading = true
        setLoadingUi(true)
        statusText.text = getString(R.string.loading)

        destroyCurrentAd()
        adContainer.removeAllViews()

        requestAd(attempt = 1)
    }

    private fun requestAd(attempt: Int) {
        TapsellPlus.requestNativeAd(
            this,
            Config.NATIVE_ZONE_ID,
            object : AdRequestCallback() {
                override fun response(model: TapsellPlusAdModel) {
                    super.response(model)
                    runOnUiThread {
                        if (isFinishing || isDestroyed) return@runOnUiThread
                        responseId = model.responseId
                        showAd()
                    }
                }

                override fun error(message: String) {
                    Log.e(TAG, "requestNativeAd error (attempt $attempt): $message")
                    runOnUiThread {
                        if (isFinishing || isDestroyed) return@runOnUiThread
                        if (attempt < Config.MAX_REQUEST_ATTEMPTS) {
                            requestAd(attempt + 1)
                        } else {
                            onFailed()
                        }
                    }
                }
            }
        )
    }

    private fun showAd() {
        val id = responseId ?: return onFailed()

        // a fresh holder for every ad
        adContainer.removeAllViews()
        val holder: AdHolder? = TapsellPlus.createAdHolder(
            this, adContainer, R.layout.native_ad_layout
        )
        if (holder == null) {
            onFailed()
            return
        }
        adHolder = holder

        TapsellPlus.showNativeAd(
            this,
            id,
            holder,
            object : AdShowListener() {
                override fun onOpened(model: TapsellPlusAdModel) {
                    super.onOpened(model)
                    runOnUiThread { onShown() }
                }

                override fun onError(model: TapsellPlusErrorModel) {
                    super.onError(model)
                    Log.e(TAG, "showNativeAd error: $model")
                    runOnUiThread { onFailed() }
                }
            }
        )
    }

    private fun onShown() {
        isLoading = false
        setLoadingUi(false)
        statusText.text = getString(R.string.ad_shown)
    }

    private fun onFailed() {
        isLoading = false
        setLoadingUi(false)
        statusText.text = getString(R.string.ad_failed)
    }

    private fun setLoadingUi(loading: Boolean) {
        progress.visibility = if (loading) View.VISIBLE else View.GONE
        refreshButton.isEnabled = !loading
    }

    private fun destroyCurrentAd() {
        responseId?.let { TapsellPlus.destroyNativeBanner(this, it) }
        responseId = null
        adHolder = null
    }

    override fun onDestroy() {
        destroyCurrentAd()
        super.onDestroy()
    }

    companion object {
        private const val TAG = "TapsellAd"
    }
}
