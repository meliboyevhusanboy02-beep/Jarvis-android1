# JARVIS — Android starter (Uzbek / Russian / English)

Bu Android Studio uchun boshlang‘ich loyiha. U tayyor, tekshirilgan APK emas.

## Hozirgi starter ichida
- Ovozli kiritish uchun o‘zbek, rus va ingliz tilini tanlash tugmalari (Android Speech Recognizer va qurilmadagi til paketlariga bog‘liq).
- Ovozli javob (Android TextToSpeech; o‘zbekcha ovoz qurilmada bo‘lmasligi mumkin).
- Oddiy sinov buyruqlari: salomlashish, vaqtni aytish, YouTube/Telegram/Sozlamalarni ochish.
- Android ROLE_DIALER so‘rovi va InCallService skeleti.
- Kontaktlarda saqlangan raqam bo‘lsa, qo‘ng‘iroq qiluvchining ismini ko‘rsatish va aytish.
- Kiruvchi qo‘ng‘iroq oynasida ovozli “qabul qil / rad et” buyruqlari.

## Muhim cheklovlar
1. Android’da “JARVIS” deb doimiy, ekran o‘chiq paytda uyg‘onish hali yo‘q. Buning uchun wake-word SDK/model (masalan, Porcupine yoki maxsus Vosk pipeline), foreground service, doimiy bildirishnoma, mikrofon ruxsati va batareya optimizatsiyasi sozlamalari kerak.
2. Qo‘ng‘iroqlarni boshqarish uchun foydalanuvchi JARVIS’ni standart telefon ilovasi qilib tanlashi kerak. Telefon ishlab chiqaruvchisi va Android versiyasi bo‘yicha xatti-harakatlar farq qiladi. Ushbu minimal InCallService to‘liq dialer UI o‘rnini bosishga hali tayyor emas; kundalik foydalanishdan oldin real qurilmada sinov va to‘liq qo‘ng‘iroq interfeysi kerak.
3. Android Speech Recognizer va TTS tillari telefonning til paketlari/Google xizmatlariga bog‘liq. Uch tilni ishonchli ishlatish uchun keyingi bosqichda til tanlash va server tomondagi STT/TTS qo‘shiladi.
4. Internet AI uchun backend kerak. API kalitini APK ichiga yozmang. Keyingi bosqichda xavfsiz backend manzili va serverdagi API kalit bilan ulaymiz.
5. Bu loyiha telefonning kontaktlari, mikrofon va qo‘ng‘iroqlariga tegishli sezgir ruxsatlarni so‘rashi mumkin. Faqat o‘zingizning qurilmangizda, tushungan holda ruxsat bering.

## Android Studio’da ochish
1. Kompyuterga Android Studio (JDK 17 bilan) o‘rnating.
2. ZIP’ni oching va `JARVIS_Android_Starter` papkasini Android Studio’da `Open` qiling.
3. Gradle sync tugashini kuting.
4. Telefonni USB debugging bilan ulang va Run bosing.
5. Avval mikrofon ruxsatini bering, keyin ilovani sinang.
6. Qo‘ng‘iroq funksiyasini tekshirishdan oldin Android Settings ichida Default apps → Phone app orqali JARVIS’ni tanlash taklifini ko‘rib chiqing. Hozirgi demo to‘liq dialer emas.

## Keyingi ishlab chiqish bosqichlari
- Uch til uchun alohida til tanlash.
- Haqiqiy AI backend (server orqali).
- Wake-word “JARVIS” uchun foreground service.
- To‘liq InCallService ekranlari, Bluetooth/headset va favqulodda qo‘ng‘iroq holatlarini sinash.
- Kontakt nomini READ_CONTACTS ruxsati bilan xavfsiz aniqlash.
- APK build, imzolash va qurilmada test.
