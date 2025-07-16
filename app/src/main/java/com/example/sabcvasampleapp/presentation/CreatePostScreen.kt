package com.example.sabcvasampleapp.presentation


import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.ui.text.SpanStyle
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.IosShare
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePostScreen(
    navController: NavController,
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
        TopAppBar(
            title = {
                Text("Create Post", color = Color.White, fontWeight = FontWeight.SemiBold)
            },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
            },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color(0xFFB00020))
        )

        // Subtitle
        Text(
            text = "Share something with your network!",
            fontSize = 16.sp,
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp)
        )

        Spacer(modifier = Modifier.height(4.dp))

        TextFieldSection(
            label = "Post Title",
            value = title,
            onValueChange = { title = it },
            placeholder = "Enter your post title here...",
            isMultiline = false, // or true if needed
            onClick = null       // or { /* your click logic */ }
        )

        Spacer(modifier = Modifier.height(20.dp))

        TextFieldSection(
            label = "Description",
            value = description,
            onValueChange = { description = it },
            placeholder = "Enter your post description here...",
            isMultiline = true, // or true if needed
            onClick = null       // or { /* your click logic */ }
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 📎 Upload Area
        Box(
            modifier = Modifier
                .height(140.dp)
                .padding(top = 8.dp)
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth()
                .padding(
                    horizontal = 16.dp
                )
                .border(
                    BorderStroke(2.dp, Color.Red), // red border
                    shape = RoundedCornerShape(12.dp)
                ).clickable { // 👈 trigger picker on click
                    filePickerLauncher.launch(arrayOf("*/*"))
                },
            contentAlignment = Alignment.Center,
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = if (selectedFileUri != null) Icons.Default.Check else Icons.Default.IosShare, // ← you'll add this
                    contentDescription = "Upload Icon",
                    tint = if (selectedFileUri != null) Color(0xFF4CAF50) else Color(0xFFe30029),
                    modifier = Modifier.size(40.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = if (selectedFileUri != null) "File Selected" else "Click to upload or drag and drop",
                    color = if (selectedFileUri != null) Color(0xFF4CAF50) else Color(0xFFe30029)
                )
                Text(
                    if (selectedFileUri != null) "Click to change file" else "Images, PDFs, documents up to 10MB",
                    fontSize = 14.sp,
                    color = Color.DarkGray
                )
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
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB00020))
                ) {
                    Text("Publish", color = Color.White)
                }
            }
        }
    }

@Composable
fun TextFieldSection(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    isMultiline: Boolean = false,
    onClick: (() -> Unit)? = null
) {
    Text(
        text = buildAnnotatedString {
            append(label)
            withStyle(style = SpanStyle(color = Color.Red)) {
                append(" *")
            }
        },
        fontSize = 16.sp,
        fontWeight = FontWeight.Medium,
        color = Color.Black,
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp)
    )

    Spacer(modifier = Modifier.height(4.dp))

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = placeholder,
                fontSize = 16.sp,
                color = Color.DarkGray
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .height(if (isMultiline) 120.dp else 56.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable(enabled = onClick != null) { onClick?.invoke() },
        textStyle = LocalTextStyle.current.copy(fontSize = 16.sp),
        shape = RoundedCornerShape(12.dp),
        readOnly = false,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent,
            focusedContainerColor = Color(0xFFF2F2F2),
            unfocusedContainerColor = Color(0xFFF2F2F2),
        )
    )
}

