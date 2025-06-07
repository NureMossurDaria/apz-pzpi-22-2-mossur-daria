package ua.nure.mossurd.blooddonation.activity

import android.Manifest
import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.edit
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ua.nure.mossurd.blooddonation.R
import ua.nure.mossurd.blooddonation.dto.DonorDataDto
import ua.nure.mossurd.blooddonation.dto.MedicDataDto
import ua.nure.mossurd.blooddonation.enums.UserRole
import ua.nure.mossurd.blooddonation.misc.Constants
import ua.nure.mossurd.blooddonation.service.LocaleService
import ua.nure.mossurd.blooddonation.ui.theme.BloodDoTheme
import ua.nure.mossurd.blooddonation.utils.LocalizationUtils

class MainMenuActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        LocaleService.initLocale(this@MainMenuActivity, applicationContext)
        super.onCreate(savedInstanceState)

        setContent {
            BloodDoTheme {
                Content(this@MainMenuActivity)
            }
        }
    }

    @Composable
    fun Content(context: Context?) {
        val token = this@MainMenuActivity.getSharedPreferences(
            Constants.BLOODDO_CONTEXT_PREFS,
            MODE_PRIVATE
        )
            ?.getString(Constants.BLOODDO_TOKEN_NAME, "")
            .orEmpty()

        var appLang: String = this@MainMenuActivity.getSharedPreferences(
            Constants.BLOODDO_CONTEXT_PREFS,
            MODE_PRIVATE
        )
            .getString(Constants.BLOODDO_LANGUAGE_NAME, "")
            .orEmpty()
        var sysLang: String? = this@MainMenuActivity.getSharedPreferences(
            Constants.BLOODDO_CONTEXT_PREFS,
            MODE_PRIVATE
        )
            .getString(Constants.BLOODDO_SYSTEM_LANGUAGE, "")
        if (appLang != sysLang) {
            Constants.API_SERVICE.updateLanguageCall(token, appLang)
                .enqueue(object : Callback<Void> {
                    override fun onResponse(
                        call: Call<Void?>,
                        response: Response<Void?>
                    ) {
                        if (!response.isSuccessful) {
                            Toast.makeText(
                                applicationContext,
                                getString(R.string.request_generic_error),
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            context?.getSharedPreferences(
                                Constants.BLOODDO_CONTEXT_PREFS,
                                MODE_PRIVATE
                            )
                                ?.edit() { putString(Constants.BLOODDO_SYSTEM_LANGUAGE, appLang) }
                        }
                    }

                    override fun onFailure(
                        call: Call<Void?>,
                        t: Throwable
                    ) {
                        Toast.makeText(
                            applicationContext,
                            getString(R.string.request_generic_error),
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.e(
                            this.javaClass.name,
                            "Update language request failure. Message: ${t.message}"
                        )
                    }
                })
        }

        val rolesStr = this@MainMenuActivity.getSharedPreferences(
            Constants.BLOODDO_CONTEXT_PREFS,
            MODE_PRIVATE
        )
            ?.getString(Constants.BLOODDO_ROLES_NAME, "")
            .orEmpty()
        val rolesArr =
            Constants.GSON.fromJson<List<UserRole>>(
                rolesStr,
                object : TypeToken<List<UserRole>>() {}.type
            )

        var donorDataStr: String? = null
        var donorInfo: DonorDataDto? by remember { mutableStateOf(null) }
        var medicDataStr: String? = null
        var medicInfo: MedicDataDto? by remember { mutableStateOf(null) }

        if (rolesArr.contains(UserRole.DONOR)) {
            donorDataStr = this@MainMenuActivity.getSharedPreferences(
                Constants.BLOODDO_CONTEXT_PREFS,
                MODE_PRIVATE
            )
                ?.getString(Constants.BLOODDO_DONOR_DATA, null)
            if (donorDataStr != null) {
                donorInfo = Constants.GSON.fromJson<DonorDataDto>(
                    donorDataStr,
                    object : TypeToken<DonorDataDto>() {}.type
                )
            } else {
                Constants.API_SERVICE.getSelfDonorCall(token)
                    .enqueue(object : Callback<DonorDataDto> {
                        override fun onResponse(
                            call: Call<DonorDataDto>,
                            response: Response<DonorDataDto>
                        ) {
                            if (response.isSuccessful) {
                                donorInfo = response.body()
                                this@MainMenuActivity.getSharedPreferences(
                                    Constants.BLOODDO_CONTEXT_PREFS,
                                    MODE_PRIVATE
                                )
                                    ?.edit() {
                                        putString(
                                            Constants.BLOODDO_DONOR_DATA,
                                            Constants.GSON.toJson(donorInfo)
                                        )
                                    }
                            } else {
                                Toast.makeText(
                                    applicationContext,
                                    getString(R.string.request_generic_error),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        override fun onFailure(
                            call: Call<DonorDataDto?>,
                            t: Throwable
                        ) {
                            Toast.makeText(
                                applicationContext,
                                getString(R.string.request_generic_error),
                                Toast.LENGTH_SHORT
                            ).show()
                            Log.e(
                                this.javaClass.name,
                                "Get donor data request failure. Message: ${t.message}"
                            )
                        }
                    })
            }
        }
        if (rolesArr.contains(UserRole.MEDIC)) {
            medicDataStr = this@MainMenuActivity.getSharedPreferences(
                Constants.BLOODDO_CONTEXT_PREFS,
                MODE_PRIVATE
            )
                ?.getString(Constants.BLOODDO_MEDIC_DATA, null)
            if (medicDataStr != null) {
                medicInfo = Constants.GSON.fromJson<MedicDataDto>(
                    medicDataStr,
                    object : TypeToken<MedicDataDto>() {}.type
                )
            } else {
                Constants.API_SERVICE.getSelfMedicCall(token)
                    .enqueue(object : Callback<MedicDataDto> {
                        override fun onResponse(
                            call: Call<MedicDataDto>,
                            response: Response<MedicDataDto>
                        ) {
                            if (response.isSuccessful) {
                                medicInfo = response.body()
                                this@MainMenuActivity.getSharedPreferences(
                                    Constants.BLOODDO_CONTEXT_PREFS,
                                    MODE_PRIVATE
                                )
                                    ?.edit() {
                                        putString(
                                            Constants.BLOODDO_MEDIC_DATA,
                                            Constants.GSON.toJson(medicInfo)
                                        )
                                    }
                            } else {
                                Toast.makeText(
                                    applicationContext,
                                    getString(R.string.request_generic_error),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        override fun onFailure(
                            call: Call<MedicDataDto?>,
                            t: Throwable
                        ) {
                            Toast.makeText(
                                applicationContext,
                                getString(R.string.request_generic_error),
                                Toast.LENGTH_SHORT
                            ).show()
                            Log.e(
                                this.javaClass.name,
                                "Get medic data request failure. Message: ${t.message}"
                            )
                        }
                    })
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(
                    applicationContext,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this@MainMenuActivity,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    1
                )
            }
        }

        Box(modifier = Modifier.fillMaxSize()) {
            Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    if (rolesArr.contains(UserRole.MEDIC) && medicInfo != null) {
                        MedicInfo(this@MainMenuActivity, medicInfo!!, appLang)
                    }
                    if (rolesArr.contains(UserRole.DONOR) && donorInfo != null) {
                        DonorInfo(donorInfo!!, appLang)
                    }
                    if (rolesArr.contains(UserRole.MEDIC)) {
                        // TODO medic set of buttons
//                        Button(
//                            onClick = { /* Handle click for Button 3 */ },
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(8.dp)
//                                .height(80.dp)
//                        ) {
//                            Text(
//                                textAlign = TextAlign.Center,
//                                text = "1234567890"
//                            )
//                        }
                    }
                    if (rolesArr.contains(UserRole.DONOR)) {
                        Button(
                            onClick = {
                                val intent =
                                    Intent(context, DonationAppointmentActivity::class.java)
                                context?.startActivity(intent)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .height(80.dp),
                            enabled = true
                        ) {
                            Text(
                                textAlign = TextAlign.Center,
                                text = getString(R.string.main_menu_donation_appointment_button)
                            )
                        }
                        Button(
                            onClick = {
                                val intent =
                                    Intent(context, MyDonationAppointmentsActivity::class.java)
                                context?.startActivity(intent)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .height(80.dp),
                            enabled = true
                        ) {
                            Text(
                                textAlign = TextAlign.Center,
                                text = getString(R.string.main_menu_donation_my_appointments_button)
                            )
                        }
                        Button(
                            onClick = {
                                val intent =
                                    Intent(context, DonationRecommendationsActivity::class.java)
                                context?.startActivity(intent)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .height(80.dp),
                            enabled = true
                        ) {
                            Text(
                                textAlign = TextAlign.Center,
                                text = getString(R.string.main_menu_donation_recommendations_button)
                            )
                        }
                        Button(
                            onClick = {
                                val intent =
                                    Intent(context, MyDonationsActivity::class.java)
                                context?.startActivity(intent)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .height(80.dp),
                            enabled = true
                        ) {
                            Text(
                                textAlign = TextAlign.Center,
                                text = getString(R.string.main_menu_donation_history_button)
                            )
                        }
                    }

                    Button(
                        onClick = {
                            val intent = Intent(context, PiAgreementActivity::class.java)
                            intent.putExtra(
                                Constants.FROM_ACTIVITY,
                                MainMenuActivity::class.java.simpleName
                            )
                            context?.startActivity(intent)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .height(80.dp),
                        enabled = true
                    ) {
                        Text(

                            textAlign = TextAlign.Center,
                            text = getString(R.string.main_menu_pi_notice_settings_button)
                        )
                    }
                    Button(
                        onClick = {
                            val intent =
                                Intent(context, NotificationsHistoryActivity::class.java)
                            context?.startActivity(intent)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .height(80.dp),
                        enabled = true
                    ) {
                        Text(
                            textAlign = TextAlign.Center,
                            text = getString(R.string.notifications_history_activity_header)
                        )
                    }
                }
            }

            LanguageButton(this@MainMenuActivity)
        }
    }

    @Composable
    fun LanguageButton(context: Context) {
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.TopEnd) {
            Button(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(0.dp),
                contentPadding = PaddingValues(0.dp),
                border = BorderStroke(0.dp, Color.Transparent),
                colors = ButtonColors(
                    Color.Transparent,
                    Color.White,
                    Color.Transparent,
                    Color.White
                ),
                shape = RoundedCornerShape(0.dp),
                onClick = {
                    LocaleService.switchLocaleName(context)
                    (context as Activity).recreate()
                }) {
                Image(
                    modifier = Modifier.size(30.dp, 20.dp),
                    contentDescription = getString(R.string.lang_short_name),
                    painter = painterResource(R.mipmap.flag)
                )
            }
        }
    }

    @Composable
    fun DonorInfo(donorInfo: DonorDataDto, language: String, modifier: Modifier = Modifier) {
        Text(
            text = getString(
                R.string.profile_activity_donor_data_template,
                LocalizationUtils.transliterateToLanguage(donorInfo.firstName, language),
                LocalizationUtils.transliterateToLanguage(donorInfo.lastName, language),
                LocalizationUtils.getLocalizedBloodType(donorInfo.bloodType, language),
                if (donorInfo.rhesus) '+' else '-',
                donorInfo.phone
            ),
            modifier = modifier
                .padding(10.dp)
                .fillMaxWidth()
        )
    }

    @Composable
    fun MedicInfo(
        context: Context?,
        medicInfo: MedicDataDto,
        language: String,
        modifier: Modifier = Modifier
    ) {
        Text(
            text = getString(
                R.string.profile_activity_medic_data_template,
                LocalizationUtils.transliterateToLanguage(medicInfo.firstName, language),
                LocalizationUtils.transliterateToLanguage(medicInfo.lastName, language),
                medicInfo.phone,
                LocalizationUtils.transliterateToLanguage(medicInfo.hospital.name, language),
                LocalizationUtils.transliterateToLanguage(medicInfo.hospital.address, language)
            ),
            modifier = modifier
                .padding(10.dp)
                .fillMaxWidth()
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = {
                            val clipboard: ClipboardManager =
                                (getSystemService(CLIPBOARD_SERVICE) as ClipboardManager)
                            val clip: ClipData = ClipData.newPlainText(
                                LocalizationUtils.transliterateToLanguage(
                                    medicInfo.hospital.name,
                                    language
                                ),
                                LocalizationUtils.transliterateToLanguage(
                                    medicInfo.hospital.name,
                                    language
                                )
                            )
                            clipboard.setPrimaryClip(clip)
                            Toast.makeText(
                                context,
                                getString(R.string.profile_activity_hospital_name_copied),
                                Toast.LENGTH_SHORT
                            ).show()
                        },
                        onLongPress = {
                            val clipboard: ClipboardManager =
                                (getSystemService(CLIPBOARD_SERVICE) as ClipboardManager)
                            val clip: ClipData = ClipData.newPlainText(
                                LocalizationUtils.transliterateToLanguage(
                                    medicInfo.hospital.address,
                                    language
                                ),
                                LocalizationUtils.transliterateToLanguage(
                                    medicInfo.hospital.address,
                                    language
                                )
                            )
                            clipboard.setPrimaryClip(clip)
                            Toast.makeText(
                                context,
                                getString(R.string.profile_activity_hospital_address_copied),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    )
                }
        )
    }

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        BloodDoTheme {
            Content(null)
        }
    }
}