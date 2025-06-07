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
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.edit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ua.nure.mossurd.blooddonation.R
import ua.nure.mossurd.blooddonation.dto.LoginResponseDto
import ua.nure.mossurd.blooddonation.misc.Constants
import ua.nure.mossurd.blooddonation.service.LocaleService
import ua.nure.mossurd.blooddonation.ui.theme.BloodDoTheme

class LoginActivity : ComponentActivity() {

    var requestSuccess: Boolean? = null
    var piAgreed: Boolean? = null
    var isWaitingCircleDisplayed = mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        LocaleService.initLocale(this@LoginActivity, applicationContext)
        super.onCreate(savedInstanceState)

        this@LoginActivity.getSharedPreferences(Constants.BLOODDO_CONTEXT_PREFS, MODE_PRIVATE)
            ?.edit() { putString(Constants.BLOODDO_DONOR_DATA, null) }
        this@LoginActivity.getSharedPreferences(Constants.BLOODDO_CONTEXT_PREFS, MODE_PRIVATE)
            ?.edit() { putString(Constants.BLOODDO_MEDIC_DATA, null) }

        setContent {
            BloodDoTheme {
                Content(this@LoginActivity)
            }
        }
    }

    @Composable
    fun Content(context: Context?) {
        Box(modifier = Modifier.fillMaxSize()) {
            Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    var username by remember { mutableStateOf("") }
                    var password by remember { mutableStateOf("") }

                    Text(
                        text = getString(R.string.login_activity_welcome)
                    )

                    TextField(
                        singleLine = true,
                        value = username,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .height(80.dp),
                        onValueChange = { username = it },
                        label = { Text(getString(R.string.login_activity_username)) }
                    )
                    TextField(
                        singleLine = true,
                        value = password,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .height(80.dp),
                        visualTransformation = PasswordVisualTransformation(),
                        onValueChange = { password = it },
                        label = { Text(getString(R.string.login_activity_password)) }
                    )
                    Button(
                        onClick = {
                            isWaitingCircleDisplayed.value = true

                            try {
                                loginRequest(username, password, context)
                            } catch (e: Exception) {
                                Toast.makeText(this@LoginActivity, e.message, Toast.LENGTH_SHORT)
                                    .show()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .height(80.dp),
                        enabled = true
                    ) {
                        Text(text = getString(R.string.login_activity_login_button))
                    }
                }
            }

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
                        LocaleService.switchLocaleName(this@LoginActivity)
                        (context as Activity).recreate()
                    }) {
                    Image(
                        modifier = Modifier.size(30.dp, 20.dp),
                        contentDescription = getString(R.string.lang_short_name),
                        painter = painterResource(R.mipmap.flag)
                    )
                }
            }

            LoadingResponseWaitingCircle(isDisplayed = isWaitingCircleDisplayed.value)
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

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        BloodDoTheme {
            Content(null)
        }
    }

    private fun loginRequest(
        username: String,
        password: String,
        context: Context?
    ) {
        context?.getSharedPreferences(Constants.BLOODDO_CONTEXT_PREFS, MODE_PRIVATE)
            ?.edit() { putString(Constants.BLOODDO_TOKEN_NAME, null) }
        context?.getSharedPreferences(Constants.BLOODDO_CONTEXT_PREFS, MODE_PRIVATE)
            ?.edit() { putString(Constants.BLOODDO_USER_NAME, null) }
        context?.getSharedPreferences(Constants.BLOODDO_CONTEXT_PREFS, MODE_PRIVATE)
            ?.edit() { putString(Constants.BLOODDO_ROLES_NAME, null) }
        context?.getSharedPreferences(Constants.BLOODDO_CONTEXT_PREFS, MODE_PRIVATE)
            ?.edit() { putString(Constants.BLOODDO_PI_AGREED, null) }
        context?.getSharedPreferences(Constants.BLOODDO_CONTEXT_PREFS, MODE_PRIVATE)
            ?.edit() { putString(Constants.BLOODDO_SYSTEM_LANGUAGE, null) }

        val requestBody = mapOf("username" to username, "password" to password)

        Constants.API_SERVICE.loginCall(requestBody).enqueue(object : Callback<LoginResponseDto> {
            override fun onResponse(
                call: Call<LoginResponseDto>,
                response: Response<LoginResponseDto>
            ) {
                if (response.isSuccessful) {
                    val responseData = response.body()
                    if (responseData != null) {
                        context?.getSharedPreferences(Constants.BLOODDO_CONTEXT_PREFS, MODE_PRIVATE)
                            ?.edit() {
                                putString(
                                    Constants.BLOODDO_TOKEN_NAME,
                                    "Bearer ${responseData.token}"
                                )
                            }
                        context?.getSharedPreferences(Constants.BLOODDO_CONTEXT_PREFS, MODE_PRIVATE)
                            ?.edit() { putString(Constants.BLOODDO_USER_NAME, responseData.name) }
                        context?.getSharedPreferences(Constants.BLOODDO_CONTEXT_PREFS, MODE_PRIVATE)
                            ?.edit() {
                                putString(
                                    Constants.BLOODDO_ROLES_NAME,
                                    Constants.GSON.toJson(responseData.roles)
                                )
                            }
                        context?.getSharedPreferences(Constants.BLOODDO_CONTEXT_PREFS, MODE_PRIVATE)
                            ?.edit() {
                                putString(
                                    Constants.BLOODDO_SYSTEM_LANGUAGE,
                                    responseData.language
                                )
                            }
                        context?.getSharedPreferences(Constants.BLOODDO_CONTEXT_PREFS, MODE_PRIVATE)
                            ?.edit() {
                                putBoolean(
                                    Constants.BLOODDO_PI_AGREED,
                                    responseData.piAgreed
                                )
                            }
                        piAgreed = responseData.piAgreed
                    }
                    requestSuccess = true
                } else {
                    requestSuccess = false
                }
                afterRequest()
            }

            override fun onFailure(call: Call<LoginResponseDto>, t: Throwable) {
                Toast.makeText(
                    applicationContext,
                    getString(R.string.login_activity_generic_error),
                    Toast.LENGTH_SHORT
                ).show()
                Log.e(this.javaClass.name, "Login request failure. Message: ${t.message}")
                requestSuccess = false
                afterRequest()
            }

            fun afterRequest() {
                if (requestSuccess as Boolean) {
                    if (piAgreed != true) {
                        val intent = Intent(context, PiAgreementActivity::class.java)
                        intent.putExtra(
                            Constants.FROM_ACTIVITY,
                            LoginActivity::class.java.simpleName
                        )
                        context?.startActivity(intent)
                        (context as Activity).finish()
                    } else {
                        val intent = Intent(context, MainMenuActivity::class.java)
                        context?.startActivity(intent)
                        (context as Activity).finish()
                    }
                } else {
                    Toast.makeText(
                        this@LoginActivity, getString(R.string.login_activity_invalid_credentials),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                isWaitingCircleDisplayed.value = false
                requestSuccess = null
            }
        })
    }
}

