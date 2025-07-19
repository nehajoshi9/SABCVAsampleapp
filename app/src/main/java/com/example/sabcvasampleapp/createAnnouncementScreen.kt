package com.example.sabcvasampleapp

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun CreateAnnouncementScreen(
    viewModel: CreateAnnouncementViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val uiState = viewModel.uiState

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header
        Text(
            text = "Admin Account",
            color = Color.Red,
            fontFamily = VietnamPro,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(4.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Create Announcement",
                fontSize = 18.sp,
                fontFamily = VietnamPro,
                color = Color(0xFF0D141C),
                fontWeight = FontWeight.Bold
            )

            TextButton(
                onClick = { viewModel.postAnnouncement() },
                modifier = Modifier.align(Alignment.CenterEnd),
                enabled = !uiState.isLoading && uiState.title.isNotBlank()
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = Color(0xFF4A739C),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        "Post",
                        fontFamily = VietnamPro,
                        fontSize = 16.sp,
                        color = Color(0xFF4A739C),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(0.dp))

        // Type toggle buttons
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            listOf("Announcement", "Post", "Event").forEach { type ->
                OutlinedButton(
                    onClick = { viewModel.selectType(type) },
                    shape = RoundedCornerShape(30),
                    border = BorderStroke(1.dp, Color.Black),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = if (uiState.selectedType == type)
                            Color.LightGray.copy(alpha = 0.4f)
                        else Color.Transparent
                    ),
                    contentPadding = PaddingValues(horizontal = 11.dp, vertical = 0.dp) //
                ) {
                    Text(
                        text = type,
                        fontFamily = VietnamPro,
                        color = Color.Black,
                        fontSize = 14.sp
                    )
                }
            }
        }


        Spacer(modifier = Modifier.height(24.dp))

        // Title input
        TextField(
            value = uiState.title,
            onValueChange = { viewModel.updateTitle(it) },
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFE8EDF5),
                unfocusedContainerColor = Color(0xFFE8EDF5),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            placeholder = {
                Text(
                    "Title",
                    color = Color(0xFF4A739C),
                    fontFamily = VietnamPro
                )
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Description input
        TextField(
            value = uiState.description,
            onValueChange = { viewModel.updateDescription(it) },
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFE8EDF5),
                unfocusedContainerColor = Color(0xFFE8EDF5),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            placeholder = {
                Text(
                    "Description",
                    color = Color(0xFF4A739C),
                    fontFamily = VietnamPro
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
        )

        // Success message
        if (uiState.isPosted) {
            Text(
                text = "Announcement posted successfully!",
                color = Color.Green,
                fontFamily = VietnamPro,
                modifier = Modifier
                    .padding(top = 16.dp)
                    .align(Alignment.CenterHorizontally)
            )
        }

        // Error message
        uiState.errorMessage?.let { error ->
            Text(
                text = error,
                color = Color.Red,
                fontFamily = VietnamPro,
                modifier = Modifier
                    .padding(top = 16.dp)
                    .align(Alignment.CenterHorizontally)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Media Upload Placeholder (UI only)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    BorderStroke(1.dp, Color(0xFFCFDBE8)),
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Upload Media",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                fontFamily = VietnamPro
            )

            Text(
                text = "Tap to select media for your post",
                fontSize = 14.sp,
                color = Color(0xFF0D141C),
                textAlign = TextAlign.Center,
                fontFamily = VietnamPro,
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(Color(0xFFEFF2FA))
                    .padding(horizontal = 24.dp, vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Choose Media",
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    fontSize = 14.sp,
                    fontFamily = VietnamPro
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CreateAnnouncementScreenPreview() {
    SABCVAAppTheme {
        CreateAnnouncementScreen()
    }
}
