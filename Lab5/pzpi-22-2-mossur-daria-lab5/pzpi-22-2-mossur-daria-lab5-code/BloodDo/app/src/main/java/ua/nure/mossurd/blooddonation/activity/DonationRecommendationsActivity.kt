package ua.nure.mossurd.blooddonation.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import ua.nure.mossurd.blooddonation.R
import ua.nure.mossurd.blooddonation.service.LocaleService
import ua.nure.mossurd.blooddonation.ui.theme.BloodDoTheme

class DonationRecommendationsActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        LocaleService.initLocale(this@DonationRecommendationsActivity, applicationContext)
        super.onCreate(savedInstanceState)

        setContent {
            BloodDoTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        Text(
                            modifier = Modifier.padding(5.dp),
                            fontSize = TextUnit(30.0f, TextUnitType.Sp),
                            fontWeight = FontWeight.Bold,
                            text = getString(R.string.donation_recommendations_header)
                        )
                    },
                    bottomBar = {
                        Button(
                            modifier = Modifier
                                .padding(5.dp)
                                .fillMaxWidth()
                                .height(60.dp),
                            onClick = {
                                this@DonationRecommendationsActivity.finish()
                            }
                        ) {
                            Text(text = getString(R.string.generic_ok_button))
                        }
                    }
                ) { innerPadding ->
                    Text(
                        modifier = Modifier
                            .padding(innerPadding)
                            .padding(5.dp)
                            .verticalScroll(rememberScrollState()),
                        text = getString(R.string.donation_recommendations_body)
                    )
                }
            }
        }
    }
}
