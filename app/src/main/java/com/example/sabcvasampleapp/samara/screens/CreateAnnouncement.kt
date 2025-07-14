package com.example.sabcvasampleapp.samara.screens

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sabcva_app.R

@Composable
fun CreateAnnouncementScreen(
    onCancel: () -> Unit = {},
    onPublish: (String, String, Uri?) -> Unit = { _, _, _ -> }
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedFileUri by remember { mutableStateOf<Uri?>(null) }

    val context = LocalContext.current

    // 🧩 File Picker Launcher
    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = { uri ->
            if (uri != null) {
                selectedFileUri = uri
                context.contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            }
        }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(0.dp)
    ) {
        // 🔴 Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFD32F2F))
                .padding(horizontal = 24.dp, vertical = 20.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.Campaign,
                    contentDescription = "Megaphone",
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Create Announcement",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        // Subtitle
        Text(
            text = "Keep your network informed and engaged.",
            fontSize = 14.sp,
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp)
        )

        Spacer(modifier = Modifier.height(4.dp))

        // 🔤 Title Field
        Column(modifier = Modifier.padding(horizontal = 24.dp)) {
            Text("Announcement Title *", fontWeight = FontWeight.Medium)
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                placeholder = { Text("Enter announcement title...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 📝 Description Field
        Column(modifier = Modifier.padding(horizontal = 24.dp)) {
            Text("Description *", fontWeight = FontWeight.Medium)
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                placeholder = { Text("Write your announcement details here...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 📎 Upload Area
        Column(modifier = Modifier.padding(horizontal = 24.dp)) {
            Text("Add Media (Optional)", fontWeight = FontWeight.Medium)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .padding(top = 8.dp)
                    .border(
                        BorderStroke(2.dp, Color(0xFFFF80AB)),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .clickable { // 👈 trigger picker on click
                        filePickerLauncher.launch(arrayOf("*/*"))
                    },
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        painter = painterResource(R.drawable.ic_upload),
                        contentDescription = "Upload Icon",
                        tint = Color(0xFFFF4081),
                        modifier = Modifier.size(40.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (selectedFileUri != null) "File Selected" else "Click to upload or drag and drop",
                        color = if (selectedFileUri != null) Color(0xFF4CAF50) else Color.Red
                    )
                    Text("Images, PDFs, documents up to 10MB", fontSize = 12.sp, color = Color.Gray)
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // ✅ Buttons
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        ) {
            OutlinedButton(
                onClick = onCancel,
                modifier = Modifier.weight(1f)
            ) {
                Text("Cancel")
            }

            Spacer(modifier = Modifier.width(16.dp))

            Button(
                onClick = { onPublish(title, description, selectedFileUri) },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))
            ) {
                Text("Publish", color = Color.White)
            }
        }
    }
}
