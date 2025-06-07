package ua.nure.mossurd.blooddonation.utils

import android.icu.text.DateFormat
import com.ibm.icu.text.Transliterator
import ua.nure.mossurd.blooddonation.enums.BloodType
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.util.Date
import java.util.Locale

class LocalizationUtils {
    companion object {

        private val ukToEnGeneralTransliterator = Transliterator.getInstance("Ukrainian-Latin/BGN")

        private val ukToEnAddressRules = """
                    м\. > city;
                    місто\  > city\ ;
                    смт\. > town;
                    с\. > village;
                    село\  > village\ ;
                    хутір\  > khutir\ ;
                    хут\. > khutir;
                    о\. > isl\.;
                    острів\  > island\ ;
                    вул\. > st\.;
                    вулиця\  > street\ ;
                    просп\. > ave\.;
                    проспект\  > avenue\ ;
                    пров\. > ln\.;
                    провулок\  > lane\ ;
                    шлях\  > way\ ;
                    шл\. > way;
                    майдан\  > square\ ;
                    м\-д > sq\.;
                    майд\. > sq\.;
                    площа\  > square\ ;
                    пл\. > sq\.;
                    господарство\  > farm\ ;
                    госп\. > farm;
                    дорога\  > road\ ;
                    дор\. > rd\.;
                    місце\  > place\ ;
                    район\  > district\ ;
                    р\-н\  > dist\.\ ;
                    квартал\  > block\ ;
                    кварт\. > block;
                    набережна\  > embankment\ ;
                    наб\. > embank\.;
                    бульвар\  > boulevard\ ;
                    бульв\. > blvd\.;
                    перехрестя\  > intersection\ ;
                    перехр\. > intersection;
                    територія\  > area\ ;
                    тер\. > area;
                    в\'їзд\  > entrance\ ;
                    шосе\  > highway\ ;
                    тупик\  > end\ ;
                    алея\  > alley\ ;
                    парк\  > park\ ;
                    затока\  > harbor\ ;
                    узвіз\  > descent\ ;
                    спуск\  > descent\ ;
                    уз\. > descent;
                    міст\  > bridge\ ;
                    проїзд\  > pass\ ;
                    підйом\  > ascent\ ;
                    лінія\  > line\ ;
            """.trimIndent()

        private val ukToEnAddressTransliterator = Transliterator.createFromRules(
            "AddressUkEnExceptions",
            ukToEnAddressRules,
            Transliterator.FORWARD
        )

        private val enToUkGeneralTransliterator =
            Transliterator.getInstance("Latin-Cyrillic/Ukrainian")

        private val enToUkAddressRules = """
                    city\  > місто\ ;
                    town > смт\.;
                    village\  > село\ ;
                    khutir\  > хутір\ ;
                    isl\. > о\.;
                    island\  > острів\ ;
                    st\. > вул\.;
                    street\  > вулиця\ ;
                    ave\. > просп\.;
                    avenue\  > проспект\ ;
                    ln\. > пров\.;
                    lane\  > провулок\ ;
                    way\  > шлях\ ;
                    square\  > майдан\ ;
                    sq\. > майдан\ ;
                    farm\  > господарство\ ;
                    road\  > дорога\ ;
                    rd\. > дор\.;
                    place\  > місце\ ;
                    district\  > район\ ;
                    dist\.\  > р\-н\ ;
                    block\  > квартал\ ;
                    embankment\  > набережна\ ;
                    embank\. > наб\.;
                    boulevard\  > бульвар\ ;
                    blvd\. > бульв\.;
                    intersection\  > перехрестя\ ;
                    area\  > територія\ ;
                    entrance\  > в\'їзд\ ;
                    highway\  > шосе\ ;
                    end\  > тупик\ ;
                    alley\  > алея\ ;
                    park\  > парк\ ;
                    harbor\  > затока\ ;
                    descent\  > узвіз\ ;
                    bridge\  > міст\ ;
                    pass\  > проїзд\ ;
                    ascent\  > підйом\ ;
                    line\  > лінія\ ;
            """.trimIndent()

        private val enToUkAddressTransliterator = Transliterator.createFromRules(
            "AddressEnUkExceptions",
            enToUkAddressRules,
            Transliterator.FORWARD
        )

        fun getLocalizedBloodType(bloodType: BloodType?, language: String?): String? {
            return if (language == "en") {
                bloodType.toString();
            } else {
                if (bloodType == BloodType.O) {
                    "I"
                } else if (bloodType == BloodType.A) {
                    "II"
                } else if (bloodType == BloodType.B) {
                    "III"
                } else if (bloodType == BloodType.AB) {
                    "IV"
                } else {
                    null
                }
            }
        }

        fun getLocalizedDateTime(dateTime: Date, language: String): String? {
            val locale = Locale(language)
            val formatter =
                DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, locale)
            return formatter.format(dateTime)
        }

        fun getLocalizedDate(dateTime: Date, language: String): String? {
            val locale = Locale(language)
            val formatter = DateFormat.getDateInstance(DateFormat.SHORT, locale)
            return formatter.format(dateTime)
        }

        fun getLocalizedTime(localTime: LocalTime, language: String): String? {
            val locale = Locale(language)
            val fromTime = Date.from(
                Instant.from(
                    localTime.atDate(LocalDate.now()).atZone(ZoneId.systemDefault())
                )
            )
            val formatter = DateFormat.getTimeInstance(DateFormat.SHORT, locale)
            return formatter.format(fromTime)
        }

        fun transliterateToLanguage(str: String, lang: String): String {
            return if (lang == "en") {
                transliterateUkToEn(str)
            } else {
                transliterateEnToUk(str)
            }
        }

        private fun transliterateUkToEn(str: String): String {
            val intermediateText = ukToEnAddressTransliterator.transliterate(str)
            return ukToEnGeneralTransliterator.transliterate(intermediateText)
        }

        private fun transliterateEnToUk(str: String): String {
            val intermediateText = enToUkAddressTransliterator.transliterate(str)
            return enToUkGeneralTransliterator.transliterate(intermediateText)
        }
    }
}