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
fun LoginScreen(
    onSendOtpClick: (String) -> Unit
) {
    var phoneNumber by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(40.dp))

        Box(
            modifier = Modifier
                .size(64.dp)
                .background(Color(0xFFB00020), RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text("S", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 28.sp)
        }

        Spacer(Modifier.height(16.dp))
        Text("SABCVA", fontWeight = FontWeight.Bold, fontSize = 22.sp)
        Text("South Asian Business Council of VA", fontSize = 14.sp, color = Color.Gray)

        Spacer(Modifier.height(32.dp))

        Text("Welcome Back!", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Text("Ready to network and grow?", fontSize = 14.sp, color = Color.Gray)

        Spacer(Modifier.height(24.dp))

        OutlinedTextField(
            value = phoneNumber,
            onValueChange = { phoneNumber = it },
            label = { Text("Phone Number") },
            placeholder = { Text("(555) 123-4567") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = { onSendOtpClick(phoneNumber) },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB00020)),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp),
            enabled = phoneNumber.isNotBlank()
        ) {
            Text("Send OTP", color = Color.White, fontSize = 16.sp)
        }
    }
}
