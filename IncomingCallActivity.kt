package uz.jarvis.assistant

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import java.util.Locale

class IncomingCallActivity : Activity(), TextToSpeech.OnInitListener {
    private var tts: TextToSpeech? = null
    private lateinit var callerText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tts = TextToSpeech(this, this)

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(32, 64, 32, 32)
            setBackgroundColor(0xFF101827.toInt())
        }
        callerText = TextView(this).apply {
            text = "Kiruvchi qo‘ng‘iroq"
            textSize = 24f
            setTextColor(0xFFFFFFFF.toInt())
        }
        layout.addView(callerText)
        layout.addView(makeButton("🎙 Ovozli buyruq", ::listenForCallCommand))
        layout.addView(makeButton("📞 Qabul qilish", { JarvisInCallService.answer(); finish() }))
        layout.addView(makeButton("❌ Rad etish", { JarvisInCallService.reject(); finish() }))
        setContentView(layout)
        callerText.text = "Kiruvchi qo‘ng‘iroq\n${getCallerNumber()}"
    }

    private fun makeButton(label: String, action: () -> Unit): Button =
        Button(this).apply { text = label; setOnClickListener { action() } }

    private fun getCallerNumber(): String {
        val handle = JarvisInCallService.currentCall?.details?.handle
        val number = handle?.schemeSpecificPart ?: return "Noma’lum raqam"
        if (checkSelfPermission(android.Manifest.permission.READ_CONTACTS) != android.content.pm.PackageManager.PERMISSION_GRANTED) {
            return number
        }
        return try {
            val uri = android.net.Uri.withAppendedPath(
                ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
                android.net.Uri.encode(number)
            )
            contentResolver.query(
                uri,
                arrayOf(ContactsContract.PhoneLookup.DISPLAY_NAME),
                null, null, null
            )?.use { cursor ->
                if (cursor.moveToFirst()) cursor.getString(0) ?: number else number
            } ?: number
        } catch (_: SecurityException) {
            number
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts?.language = Locale("uz", "UZ")
            tts?.speak("Kiruvchi qo‘ng‘iroq. ${getCallerNumber()}", TextToSpeech.QUEUE_FLUSH, null, "caller")
        }
    }

    private fun listenForCallCommand() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "uz-UZ")
            putExtra(RecognizerIntent.EXTRA_PROMPT, "Qabul qil yoki rad et deb ayting")
        }
        try {
            startActivityForResult(intent, 201)
        } catch (_: Exception) {
            tts?.speak("Ovozli boshqaruv ishga tushmadi.", TextToSpeech.QUEUE_FLUSH, null, "error")
        }
    }

    @Deprecated("Activity result API retained for starter")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 201 && resultCode == Activity.RESULT_OK) {
            val phrase = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.firstOrNull()?.lowercase(Locale.ROOT).orEmpty()
            when {
                listOf("qabul", "javob ber", "answer", "прими", "ответь").any { it in phrase } -> {
                    JarvisInCallService.answer(); finish()
                }
                listOf("rad et", "bekor qil", "reject", "отклонить").any { it in phrase } -> {
                    JarvisInCallService.reject(); finish()
                }
                else -> tts?.speak("Buyruq tushunilmadi. Qabul qil yoki rad et deb ayting.", TextToSpeech.QUEUE_FLUSH, null, "repeat")
            }
        }
    }

    override fun onDestroy() {
        tts?.stop()
        tts?.shutdown()
        super.onDestroy()
    }
}
