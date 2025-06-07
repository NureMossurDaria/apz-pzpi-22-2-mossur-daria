package ua.nure.mossurd.blooddonation.activity

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ua.nure.mossurd.blooddonation.R
import ua.nure.mossurd.blooddonation.dto.DonationEventDto
import ua.nure.mossurd.blooddonation.misc.Constants
import ua.nure.mossurd.blooddonation.service.LocaleService
import ua.nure.mossurd.blooddonation.ui.theme.BloodDoTheme
import ua.nure.mossurd.blooddonation.utils.LocalizationUtils
import java.util.Optional

class MyDonationAppointmentsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        LocaleService.initLocale(this@MyDonationAppointmentsActivity, applicationContext)
        super.onCreate(savedInstanceState)

        val token = this@MyDonationAppointmentsActivity.getSharedPreferences(
            Constants.BLOODDO_CONTEXT_PREFS,
            MODE_PRIVATE
        )
            ?.getString(Constants.BLOODDO_TOKEN_NAME, "")
            .orEmpty()

        val language = Optional.ofNullable(
            this@MyDonationAppointmentsActivity.getSharedPreferences(
                Constants.BLOODDO_CONTEXT_PREFS,
                MODE_PRIVATE
            )
                ?.getString(Constants.BLOODDO_LANGUAGE_NAME, "uk")
        )
            .orElse("uk")

        var isWaitingCircleDisplayed = mutableStateOf(true)

        var events = mutableStateListOf<DonationEventDto>()

        Constants.API_SERVICE.getMyAppointments(token)
            .enqueue(object : Callback<List<DonationEventDto>> {
                override fun onResponse(
                    call: Call<List<DonationEventDto>?>,
                    response: Response<List<DonationEventDto>?>
                ) {
                    if (response.isSuccessful) {
                        events.addAll(response.body().orEmpty())
                    } else {
                        Toast.makeText(
                            applicationContext,
                            getString(R.string.request_generic_error),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    afterRequest()
                }

                override fun onFailure(
                    call: Call<List<DonationEventDto>?>,
                    t: Throwable
                ) {
                    Toast.makeText(
                        applicationContext,
                        getString(R.string.request_generic_error),
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.e(
                        this.javaClass.name,
                        "Get my appointments request failure. Message: ${t.message}"
                    )
                    afterRequest()
                }

                fun afterRequest() {
                    isWaitingCircleDisplayed.value = false
                }
            })

        setContent {
            BloodDoTheme {
                Box(modifier = Modifier.fillMaxSize()) {
                    if (!isWaitingCircleDisplayed.value) {
                        Scaffold(
                            modifier = Modifier.fillMaxSize(),
                            topBar = {
                                Text(
                                    modifier = Modifier.padding(5.dp),
                                    fontSize = TextUnit(30.0f, TextUnitType.Sp),
                                    fontWeight = FontWeight.Bold,
                                    text = getString(R.string.my_appointments_header)
                                )
                            },
                            bottomBar = {
                                Button(
                                    modifier = Modifier
                                        .padding(5.dp)
                                        .fillMaxWidth()
                                        .height(60.dp),
                                    onClick = {
                                        this@MyDonationAppointmentsActivity.finish()
                                    }
                                ) {
                                    Text(text = getString(R.string.back_generic_label))
                                }
                            }
                        ) { innerPadding ->
                            LazyColumn(modifier = Modifier.padding(innerPadding)) {
                                items(events) { eventInfo ->
                                    val eventText: String =
                                        LocalizationUtils.transliterateToLanguage(
                                            eventInfo.eventAddress,
                                            language
                                        ) +
                                                "\n" +
                                                LocalizationUtils.getLocalizedDateTime(
                                                    eventInfo.dateTime,
                                                    language
                                                ) +
                                                "\n" +
                                                getString(R.string.generic_notes_label) +
                                                eventInfo.notes
                                    Row(
                                        modifier = Modifier
                                            .padding(5.dp)
                                            .fillMaxWidth()
                                            .wrapContentHeight()
                                            .background(color = MaterialTheme.colorScheme.secondary)
                                            .pointerInput(Unit) {
                                                detectTapGestures(
                                                    onTap = {
                                                        AlertDialog.Builder(this@MyDonationAppointmentsActivity)
                                                            .setTitle(R.string.my_appointments_details_header)
                                                            .setMessage(eventText)
                                                            .setPositiveButton(R.string.my_appointments_details_remove_appointment) { dialog, which ->
                                                                Constants.API_SERVICE.deleteEventAppointment(
                                                                    token,
                                                                    eventInfo.id
                                                                ).enqueue(object : Callback<Void> {
                                                                    override fun onResponse(
                                                                        call: Call<Void?>,
                                                                        response: Response<Void?>
                                                                    ) {
                                                                        if (response.isSuccessful) {
                                                                            Toast.makeText(
                                                                                applicationContext,
                                                                                getString(R.string.my_appointments_details_removed),
                                                                                Toast.LENGTH_SHORT
                                                                            ).show()
                                                                            events.remove(eventInfo)
                                                                        } else {
                                                                            Toast.makeText(
                                                                                applicationContext,
                                                                                getString(R.string.request_generic_error),
                                                                                Toast.LENGTH_SHORT
                                                                            ).show()
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
                                                                            "Delete appointment request failure. Message: ${t.message}"
                                                                        )
                                                                    }

                                                                })
                                                            }
                                                            .setNegativeButton(R.string.back_generic_label) { dialog, which -> }
                                                            .create()
                                                            .show()
                                                    }
                                                )
                                            }
                                    ) {
                                        Text(
                                            modifier = Modifier.padding(5.dp),
                                            text = eventText
                                        )
                                    }

                                }
                            }
                        }
                    }

                    LoadingResponseWaitingCircle(isDisplayed = isWaitingCircleDisplayed.value)
                }
            }
        }
    }

    @Composable
    fun LoadingResponseWaitingCircle(isDisplayed: Boolean) {
        if (isDisplayed) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Gray),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}
