package ua.nure.mossurd.blooddonation.misc

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ua.nure.mossurd.blooddonation.request.ApiService
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.temporal.ChronoUnit
import java.util.Date
import java.util.Locale

object Constants {
    const val BLOODDO_CONTEXT_PREFS = "BloodDoContextPrefs"

    const val BLOODDO_TOKEN_NAME = "blooddo_token"
    const val BLOODDO_USER_NAME = "blooddo_user"
    const val BLOODDO_ROLES_NAME = "blooddo_roles"
    const val BLOODDO_SYSTEM_LANGUAGE = "blooddo_system_lang"
    const val BLOODDO_PI_AGREED = "blooddo_pi_agreed"

    const val BLOODDO_LANGUAGE_NAME = "blooddo_lang"

    const val BLOODDO_DONOR_DATA = "blooddo_donor_data"
    const val BLOODDO_MEDIC_DATA = "blooddo_medic_data"

    const val BLOODDO_API_PATH = "https://localhost:14224"

    const val AUTH_HEADER = "Authorization"

    const val FROM_ACTIVITY = "blooddo_from_activity"

    const val DEFAULT_NOTIFICATION_CHANNEL = "default_notification_channel"

    val NOTIFICATIONS_PING_TIME = Duration.of(10, ChronoUnit.MINUTES)

    val GSON: Gson = GsonBuilder().registerTypeAdapter(Date::class.java,
        object: JsonDeserializer<Date> {
            override fun deserialize(
                json: JsonElement?,
                typeOfT: Type?,
                context: JsonDeserializationContext?
            ): Date {
                return SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US).parse(json?.asJsonPrimitive?.asString!!)
            }
        })
        .create();

    val API_SERVICE: ApiService = Retrofit.Builder()
        .baseUrl(BLOODDO_API_PATH)
        .addConverterFactory(GsonConverterFactory.create(GSON))
        .build()
        .create(ApiService::class.java)
}