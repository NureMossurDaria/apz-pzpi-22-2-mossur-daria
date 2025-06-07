package ua.nure.mossurd.blooddonation.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.core.content.edit
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ua.nure.mossurd.blooddonation.R
import ua.nure.mossurd.blooddonation.enums.UserRole
import ua.nure.mossurd.blooddonation.misc.Constants
import ua.nure.mossurd.blooddonation.service.LocaleService
import ua.nure.mossurd.blooddonation.ui.theme.BloodDoTheme

class PiAgreementActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        LocaleService.initLocale(this@PiAgreementActivity, applicationContext)
        super.onCreate(savedInstanceState)

        val token = this@PiAgreementActivity.getSharedPreferences(
            Constants.BLOODDO_CONTEXT_PREFS,
            MODE_PRIVATE
        )
            ?.getString(Constants.BLOODDO_TOKEN_NAME, "")
            .orEmpty()

        val rolesStr = this@PiAgreementActivity.getSharedPreferences(
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

        var appLang: String = this@PiAgreementActivity.getSharedPreferences(
            Constants.BLOODDO_CONTEXT_PREFS,
            MODE_PRIVATE
        )
            .getString(Constants.BLOODDO_LANGUAGE_NAME, "")
            .orEmpty()
        var sysLang: String? = this@PiAgreementActivity.getSharedPreferences(
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
                            this@PiAgreementActivity.getSharedPreferences(
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

        setContent {
            BloodDoTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        Text(
                            modifier = Modifier.padding(5.dp),
                            fontSize = TextUnit(30.0f, TextUnitType.Sp),
                            fontWeight = FontWeight.Bold,
                            text = getString(R.string.pi_notice_header)
                        )
                    },
                    bottomBar = {
                        Column(
                            modifier = Modifier
                                .padding(5.dp)
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Top
                        ) {
                            Button(
                                onClick = {
                                    Constants.API_SERVICE.piAgreeCall(token)
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
                                                    "PI agree request failure. Message: ${t.message}"
                                                )
                                            }
                                        })
                                    if (intent.getStringExtra(Constants.FROM_ACTIVITY) == LoginActivity::class.java.simpleName) {
                                        val intent =
                                            Intent(
                                                this@PiAgreementActivity,
                                                MainMenuActivity::class.java
                                            )
                                        this@PiAgreementActivity.startActivity(intent)
                                    }
                                    this@PiAgreementActivity.finish()
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(5.dp)
                                    .height(60.dp)
                            ) {
                                Text(text = getString(R.string.pi_notice_agree))
                            }
                            Button(
                                enabled = !rolesArr.contains(UserRole.MEDIC)
                                        && !rolesArr.contains(UserRole.ADMIN),
                                onClick = {
                                    Constants.API_SERVICE.deleteSelf(token)
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
                                                    "Delete self request failure. Message: ${t.message}"
                                                )
                                            }
                                        })
                                    val intent =
                                        Intent(this@PiAgreementActivity, LoginActivity::class.java)
                                    this@PiAgreementActivity.startActivity(intent)
                                    this@PiAgreementActivity.finish()
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(5.dp)
                                    .height(60.dp)
                            ) {
                                Text(
                                    textAlign = TextAlign.Center,
                                    text = getString(R.string.pi_notice_delete)
                                )
                            }
                        }
                    }) { innerPadding ->
                    Text(
                        modifier = Modifier
                            .padding(innerPadding)
                            .padding(5.dp)
                            .verticalScroll(rememberScrollState()),
                        text = getString(R.string.pi_notice_body)
                    )
                }

                if (intent.getStringExtra(Constants.FROM_ACTIVITY) == LoginActivity::class.java.simpleName) {
                    LanguageButton(this@PiAgreementActivity)
                }
            }
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
                shape = RoundedCornerShape(3.dp),
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
}