# JARVIS APK'ni faqat Android telefon bilan yig'ish

Bu loyiha APK'ni telefonning o'zida emas, GitHub Actions bulut kompyuterida yig'adi. Siz jarayonni telefon brauzeridan boshqarasiz.

## 1. ZIP'ni ochish
1. `JARVIS_Android_Starter.zip` faylini telefoningizga yuklab oling.
2. ZArchiver yoki telefoningizdagi arxiv dasturida ZIP'ni oching.
3. Ichidagi `JARVIS_Android_Starter` papkasini toping. Unda `app`, `build.gradle.kts`, `settings.gradle.kts` va `.github` papkasi bo'lishi kerak.

## 2. GitHub'da repository yaratish
1. Chrome'da https://github.com saytiga kiring yoki bepul hisob yarating.
2. `+` → `New repository` ni bosing.
3. Nomi: `JARVIS-Android`; `Private` ni tanlang; `Create repository` ni bosing.

## 3. Fayllarni GitHub'ga yuklash
1. Yangi repository sahifasida `Add file` → `Upload files` ni tanlang.
2. Telefon fayl tanlagichida ochilgan loyiha papkasidagi fayl va papkalarni tanlang. ZIP faylning o'zini emas, ZIP ichidagi fayllarni yuklang.
3. Muhim: `.github` yashirin papka bo'lishi mumkin. Ko'rinmasa, ZArchiver sozlamasida yashirin fayllarni ko'rsatishni yoqing. `.github/workflows/build-apk.yml` ham yuklangan bo'lishi shart.
4. Yuklash tugagach `Commit changes` ni bosing.

## 4. APK'ni olish
1. Repository'da `Actions` bo'limini oching.
2. `Build JARVIS APK` workflow ishga tushishini kuting. Agar ishga tushmasa `Run workflow` ni bosing.
3. Yashil belgi chiqqach, workflow natijasini oching.
4. Pastdagi `Artifacts` qismidan `JARVIS-debug-APK` ni yuklab oling.
5. Yuklangan artifact ZIP'ini oching. Ichida `app-debug.apk` bo'ladi.
6. APK'ni bosing va Android so'rasa, faqat GitHub'dan o'zingiz olgan fayl uchun brauzer/fayl menejeriga `Install unknown apps` ruxsatini bering.

## Muhim
- Bu `debug APK`: sinov uchun. Hozircha Play Store uchun imzolangan release APK emas.
- GitHub Actions Android SDK/Gradle orqali build qiladi; telefoningizda Android Studio kerak emas.
- Birinchi versiyada “JARVIS” wake-word doimiy tinglashi va internet AI backend hali yo'q.
- Qo'ng'iroq boshqaruvi hozircha to'liq dialer sifatida sinovdan o'tmagan. Uni asosiy telefon ilovasi qilishdan oldin alohida test kerak.
- API kalitlari yoki shaxsiy ma'lumotlarni GitHub repository'ga yuklamang.
