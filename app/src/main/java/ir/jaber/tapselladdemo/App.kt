package ir.jaber.tapselladdemo

import android.app.Application
import android.util.Log
import ir.tapsell.plus.TapsellPlus
import ir.tapsell.plus.TapsellPlusInitListener
import ir.tapsell.plus.model.AdNetworkError
import ir.tapsell.plus.model.AdNetworks

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        
        TapsellPlus.initialize(this, Config.TAPSELL_KEY, object : TapsellPlusInitListener {
            override fun onInitializeSuccess(adNetworks: AdNetworks) {
                Log.d("Tapsell", "onInitializeSuccess: " + adNetworks.name)
            }

            override fun onInitializeFailed(adNetworks: AdNetworks, adNetworkError: AdNetworkError) {
                Log.e("Tapsell", "onInitializeFailed: " + adNetworks.name + ", error: " + adNetworkError.errorMessage)
            }
        })
    }
}
