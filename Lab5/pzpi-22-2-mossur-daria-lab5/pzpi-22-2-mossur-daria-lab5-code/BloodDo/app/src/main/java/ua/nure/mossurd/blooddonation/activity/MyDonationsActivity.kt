package ua.nure.mossurd.blooddonation.activity

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
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

class MyDonationsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        LocaleService.initLocale(this@MyDonationsActivity, applicationContext)
        super.onCreate(savedInstanceState)

        val token = this@MyDonationsActivity.getSharedPreferences(
            Constants.BLOODDO_CONTEXT_PREFS,
            MODE_PRIVATE
        )
            ?.getString(Constants.BLOODDO_TOKEN_NAME, "")
            .orEmpty()

        val language = Optional.ofNullable(
            this@MyDonationsActivity.getSharedPreferences(
                Constants.BLOODDO_CONTEXT_PREFS,
                MODE_PRIVATE
            )
                ?.getString(Constants.BLOODDO_LANGUAGE_NAME, "uk")
        )
            .orElse("uk")

        var isWaitingCircleDisplayed = mutableStateOf(true)

        var events = mutableStateListOf<DonationEventDto>()

        Constants.API_SERVICE.getMyDonations(token)
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
                        "Get my donations request failure. Message: ${t.message}"
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
                                    text = getString(R.string.my_donations_header)
                                )
                            },
                            bottomBar = {
                                Button(
                                    modifier = Modifier
                                        .padding(5.dp)
                                        .fillMaxWidth()
                                        .height(60.dp),
                                    onClick = {
                                        this@MyDonationsActivity.finish()
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
                                                )
                                    Row(
                                        modifier = Modifier
                                            .padding(5.dp)
                                            .fillMaxWidth()
                                            .wrapContentHeight()
                                            .background(color = MaterialTheme.colorScheme.secondary)
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