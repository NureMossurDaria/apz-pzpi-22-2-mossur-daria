package ua.nure.mossurd.blooddonation.service

import android.content.Context
import android.content.res.Configuration
import androidx.activity.ComponentActivity.MODE_PRIVATE
import androidx.core.content.edit
import ua.nure.mossurd.blooddonation.misc.Constants
import java.util.Locale

class LocaleService {

    companion object {

        fun initLocale(context: Context, applicationContext: Context) {
            val curLocaleName =
                context.getSharedPreferences(Constants.BLOODDO_CONTEXT_PREFS, MODE_PRIVATE)
                    ?.getString(Constants.BLOODDO_LANGUAGE_NAME, null)
            val curLocale: Locale
            if (curLocaleName != null) {
                curLocale = Locale(curLocaleName)
            } else {
                curLocale = context.resources.configuration.locales.get(0)
                context.getSharedPreferences(Constants.BLOODDO_CONTEXT_PREFS, MODE_PRIVATE)
                    ?.edit() { putString(Constants.BLOODDO_LANGUAGE_NAME, curLocale.language) }
            }
            Locale.setDefault(curLocale)

            val configuration: Configuration = context.resources.configuration
            configuration.setLocale(curLocale)
            configuration.setLayoutDirection(curLocale)

            context.resources.updateConfiguration(configuration, context.resources.displayMetrics)

            val applicationConfiguration: Configuration = applicationContext.resources.configuration
            applicationConfiguration.setLocale(curLocale)
            applicationConfiguration.setLayoutDirection(curLocale)

            applicationContext.resources.updateConfiguration(applicationConfiguration, applicationContext.resources.displayMetrics)
        }

        fun switchLocaleName(context: Context) {
            val curLocaleName =
                context.getSharedPreferences(Constants.BLOODDO_CONTEXT_PREFS, MODE_PRIVATE)
                    ?.getString(Constants.BLOODDO_LANGUAGE_NAME, "")
            if (!curLocaleName.equals("en")) {
                context.getSharedPreferences(Constants.BLOODDO_CONTEXT_PREFS, MODE_PRIVATE)
                    ?.edit() { putString(Constants.BLOODDO_LANGUAGE_NAME, "en") }
            } else {
                context.getSharedPreferences(Constants.BLOODDO_CONTEXT_PREFS, MODE_PRIVATE)
                    ?.edit() { putString(Constants.BLOODDO_LANGUAGE_NAME, "uk") }
            }
        }
    }
}