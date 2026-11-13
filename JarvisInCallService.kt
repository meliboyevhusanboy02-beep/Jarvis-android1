package uz.jarvis.assistant

import android.content.Intent
import android.telecom.Call
import android.telecom.InCallService

/**
 * Android only binds this service when the user explicitly selects JARVIS
 * as the default phone app. A real dialer must also provide a complete,
 * tested in-call experience before public release.
 */
class JarvisInCallService : InCallService() {
    override fun onCallAdded(call: Call) {
        super.onCallAdded(call)
        currentCall = call
        val intent = Intent(this, IncomingCallActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }
        startActivity(intent)
    }

    override fun onCallRemoved(call: Call) {
        if (currentCall == call) currentCall = null
        super.onCallRemoved(call)
    }

    companion object {
        @Volatile var currentCall: Call? = null
            private set

        fun answer() {
            currentCall?.answer(android.telecom.VideoProfile.STATE_AUDIO_ONLY)
        }

        fun reject() {
            currentCall?.disconnect()
        }
    }
}
