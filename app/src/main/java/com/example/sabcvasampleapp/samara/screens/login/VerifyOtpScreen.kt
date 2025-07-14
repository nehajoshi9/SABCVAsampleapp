package com.example.sabcva_app.screens.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun VerifyOtpScreen(
    phoneNumber: String,
    onBackClick: () -> Unit,
    onVerifyOtpClick: (String) -> Unit,
    onResendOtpClick: () -> Unit
) {
    var otpCode by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(40.dp))

        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "<",
                fontSize = 24.sp,
                modifier = Modifier
                    .clickable { onBackClick() }
                    .padding(end = 8.dp)
            )
        }

        Spacer(Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .size(64.dp)
                .background(Color(0xFFB00020), RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text("S", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 28.sp)
        }

        Spacer(Modifier.height(12.dp))
        Text("Verify OTP", fontWeight = FontWeight.Bold, fontSize = 20.sp)
        Text("Enter the code sent to $phoneNumber", fontSize = 14.sp, color = Color.Gray)

        Spacer(Modifier.height(28.dp))

        OutlinedTextField(
            value = otpCode,
            onValueChange = { otpCode = it },
            label = { Text("OTP Code") },
            placeholder = { Text("123456") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = { onVerifyOtpClick(otpCode) },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB00020)),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp),
            enabled = otpCode.length == 6 // Assuming 6-digit OTP
        ) {
            Text("Verify", color = Color.White)
        }

        Spacer(Modifier.height(16.dp))

        Text(
            "Resend OTP",
            fontSize = 13.sp,
            color = Color(0xFFB00020),
            modifier = Modifier.clickable { onResendOtpClick() }
        )
    }
}
