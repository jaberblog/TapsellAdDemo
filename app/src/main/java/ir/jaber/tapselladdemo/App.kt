package ir.jaber.tapselladdemo

import android.app.Application
import ir.tapsell.plus.TapsellPlus

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        TapsellPlus.initialize(this, Config.TAPSELL_KEY)
    }
}
