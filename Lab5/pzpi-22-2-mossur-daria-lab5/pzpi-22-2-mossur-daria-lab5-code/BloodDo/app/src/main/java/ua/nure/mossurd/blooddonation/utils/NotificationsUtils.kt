package ua.nure.mossurd.blooddonation.utils

import android.content.Context
import java.time.LocalTime

class NotificationsUtils {
    companion object {
        private const val SEPARATOR: String = "|||"

        fun getNotificationText(rawData: String, defaultValue: String, language: String, context: Context): String {
            var result: String = defaultValue
            val strings: List<String> = rawData.split(SEPARATOR)
            if (strings.isNotEmpty()) {
                val textKey = strings[0]
                if (strings.size == 1) {
                    result = context.getString(context.resources.getIdentifier(textKey, "string", context.packageName))

                } else {
                    val textParams: List<Object> = (strings.subList(1, strings.size) as List<Object>)
                    if (textKey == "notification_medic_spoiled_blood_header") {
                        result = context.getString(context.resources.getIdentifier(textKey, "string", context.packageName), textParams[0] as String)
                    } else if (textKey == "notification_donor_donation_tomorrow_body") {
                        val address = LocalizationUtils.transliterateToLanguage(textParams[0] as String, language)
                        val time = LocalizationUtils.getLocalizedTime(LocalTime.parse(textParams[1] as String), language)
                        result = context.getString(context.resources.getIdentifier(textKey, "string", context.packageName), address, time)
                    } else {
                        result = context.getString(
                            context.resources.getIdentifier(
                                textKey,
                                "string",
                                context.packageName
                            ), textParams.toTypedArray()
                        )
                    }
                }
            }
            return result
        }
    }
}