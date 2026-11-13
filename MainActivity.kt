package uz.jarvis.assistant

import android.app.Activity
import android.app.role.RoleManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.widget.Toast
import uz.jarvis.assistant.databinding.ActivityMainBinding
import java.util.Locale

class MainActivity : android.app.Activity(), TextToSpeech.OnInitListener {
    private lateinit var binding: ActivityMainBinding
    private var tts: TextToSpeech? = null
    private var speechLanguage = "uz-UZ"
    private var ttsLocale = Locale("uz", "UZ")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        tts = TextToSpeech(this, this)
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            val needed = arrayOf(
                android.Manifest.permission.RECORD_AUDIO,
                android.Manifest.permission.READ_CONTACTS
            ).filter { checkSelfPermission(it) != android.content.pm.PackageManager.PERMISSION_GRANTED }
            if (needed.isNotEmpty()) requestPermissions(needed.toTypedArray(), 102)
        }

        binding.uzButton.setOnClickListener { setLanguage("uz-UZ", Locale("uz", "UZ"), "O‘zbekcha tanlandi") }
        binding.ruButton.setOnClickListener { setLanguage("ru-RU", Locale("ru", "RU"), "Выбран русский язык") }
        binding.enButton.setOnClickListener { setLanguage("en-US", Locale.US, "English selected") }
        binding.talkButton.setOnClickListener { startSpeechInput() }
        binding.testVoiceButton.setOnClickListener {
            speak("Salom! Men JARVISman. Sizga yordam berishga tayyorman.")
        }
        binding.dialerButton.setOnClickListener { requestDefaultDialer() }
    }

    private fun requestDefaultDialer() {
        val roleManager = getSystemService(Context.ROLE_SERVICE) as RoleManager
        if (roleManager.isRoleAvailable(RoleManager.ROLE_DIALER) &&
            !roleManager.isRoleHeld(RoleManager.ROLE_DIALER)) {
            startActivityForResult(
                roleManager.createRequestRoleIntent(RoleManager.ROLE_DIALER), REQUEST_DIALER
            )
        } else {
            Toast.makeText(this, "JARVIS allaqachon standart telefon ilovasi yoki bu imkoniyat mavjud emas.", Toast.LENGTH_LONG).show()
        }
    }

    private fun startSpeechInput() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, speechLanguage)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, speechLanguage)
            putExtra(RecognizerIntent.EXTRA_PROMPT, "JARVIS sizni tinglamoqda...")
            putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3)
        }
        try {
            startActivityForResult(intent, REQUEST_SPEECH)
        } catch (_: Exception) {
            speak("Ovoz tanish xizmati topilmadi. Google ilovasini yangilang.")
        }
    }

    @Deprecated("Activity result API retained for broad Android compatibility in starter")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_SPEECH && resultCode == Activity.RESULT_OK) {
            val words = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.firstOrNull().orEmpty()
            binding.answerText.text = "Siz: $words"
            handleCommand(words)
        }
    }

    private fun setLanguage(tag: String, locale: Locale, message: String) {
        speechLanguage = tag
        ttsLocale = locale
        tts?.language = locale
        binding.statusText.text = message
        speak(message)
    }

    private fun handleCommand(raw: String) {
        val text = raw.lowercase(Locale.ROOT)
        val reply = when {
            "salom" in text || "hello" in text || "привет" in text ->
                "Salom! Sizga qanday yordam beray?"
            "soat" in text || "time" in text || "время" in text ->
                "Hozirgi vaqt: " + java.text.SimpleDateFormat("HH:mm", Locale.getDefault()).format(java.util.Date())
            "youtube" in text -> {
                openApp("com.google.android.youtube")
                "YouTube ilovasini ochyapman."
            }
            "telegram" in text -> {
                openApp("org.telegram.messenger")
                "Telegram ilovasini ochyapman."
            }
            "sozlama" in text || "settings" in text || "настройки" in text -> {
                startActivity(Intent(android.provider.Settings.ACTION_SETTINGS))
                "Telefon sozlamalarini ochyapman."
            }
            else -> "Buyruqni tushundim: $raw. Internetdagi aqlli AI suhbatini keyingi bosqichda server orqali ulaymiz."
        }
        binding.answerText.append("\nJARVIS: $reply")
        speak(reply)
    }

    private fun openApp(packageName: String) {
        val launch = packageManager.getLaunchIntentForPackage(packageName)
        if (launch != null) startActivity(launch)
        else Toast.makeText(this, "Ilova telefonda o‘rnatilmagan.", Toast.LENGTH_SHORT).show()
    }

    private fun speak(text: String) {
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "jarvis_reply")
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts?.language = ttsLocale
        }
    }

    override fun onDestroy() {
        tts?.stop()
        tts?.shutdown()
        super.onDestroy()
    }

    companion object {
        private const val REQUEST_SPEECH = 100
        private const val REQUEST_DIALER = 101
    }
}
