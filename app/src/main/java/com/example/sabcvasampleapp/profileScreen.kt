package com.example.sabcvasampleapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sabcva.app.ui.theme.SABCVAAppTheme

val VietnamPro = FontFamily(
    Font(R.font.bevietnampro_bold, FontWeight.Bold),
    Font(R.font.bevietnampro_thin, FontWeight.Thin),
    Font(R.font.bevietnampro_black, FontWeight.Black),
    Font(R.font.bevietnampro_light, FontWeight.Light),
    Font(R.font.bevietnampro_medium, FontWeight.Medium)
)


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProfileScreen(modifier: Modifier = Modifier) {
    val tags = listOf("UI/UX Design", "User Research", "Prototyping", "Interaction Design")
    val ownedBusinesses = remember {
        listOf(
            Business("Carter's Cafe", "Artisan breads and pastries", R.drawable.cafe_pfp),
            //Business("TechSpark", "AI-based learning solutions", R.drawable.pfp)
        )
    }
    val workplaces = remember {
        listOf(
            Business("Tech Innovators Inc.", "Product Design", R.drawable.workplace_pfp),
            //Business("Startup Hub", "CTO", R.drawable.pfp)
        )
    }

    Column(
        modifier = modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState()) // Add this modifier for scrolling
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp), // typical app bar height
            contentAlignment = Alignment.Center
        ) {
            // Centered Title
            Text(
                text = "Profile",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = VietnamPro
            )

            // Back Button
            IconButton(
                onClick = { /* Handle back */ },
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(
                    painter = painterResource(R.drawable.back_arrow),
                    contentDescription = "Back",
                    modifier = Modifier.size(24.dp)

                )
            }
        }

        // Rest of your content remains the same...
        Spacer(modifier = Modifier.height(13.dp))

        // Profile Picture
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(R.drawable.pfp),
                contentDescription = "Profile Picture",
                modifier = Modifier.size(100.dp).aspectRatio(1f).padding(8.dp).clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        }

        // Name and Job Title
        Text(text = "Ethan Carter", fontSize = 24.sp, fontFamily = VietnamPro, fontWeight = FontWeight.Bold, modifier = Modifier.align(Alignment.CenterHorizontally))
        Text(text = "Product Designer", fontSize = 16.sp, fontFamily = VietnamPro, fontWeight = FontWeight.Medium, color = Color(0xFF5C758A), modifier = Modifier.align(Alignment.CenterHorizontally))

        Spacer(modifier = Modifier.height(12.dp))

        // Tags
        FlowRow(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            tags.take(5).forEach { tag ->
                AssistChip(onClick = {}, label = { Text(text = tag, fontFamily = VietnamPro, fontWeight = FontWeight.Medium, fontSize = 13.sp) }, shape = RoundedCornerShape(50), modifier = Modifier.padding(horizontal = 4.dp), colors = AssistChipDefaults.assistChipColors(
                    containerColor = Color(0xFFEBEDF2)), border = null)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Bio
        Text(text = "Bio", fontSize = 18.sp, color = Color(0xFF133A94), fontFamily = VietnamPro, fontWeight = FontWeight.Bold)
        Text("Passionate about creating user-centric designs that solve real-world problems. Always learning and exploring new technologies.", fontSize = 16.sp, fontFamily = VietnamPro, fontWeight = FontWeight.Medium)

        Spacer(modifier = Modifier.height(20.dp))

        // Owned Businesses
        Text("Owned Businesses", fontFamily = VietnamPro, color = Color(0xFF133A94), fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            ownedBusinesses.forEach { business ->
                Row(
                    modifier = Modifier.fillMaxWidth().height(100.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(business.title, fontWeight = FontWeight.SemiBold, fontFamily = VietnamPro)
                        Text(business.description, fontSize = 14.sp, fontFamily = VietnamPro, color = Color(0xFF5C758A), fontWeight = FontWeight.Light)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Image(
                        painter = painterResource(business.imageRes),
                        contentDescription = business.title,
                        modifier = Modifier.size(width = 150.dp, height = 80.dp), // Rectangular dimensions
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }

        // Workplaces
        Text("Workplaces", fontSize = 18.sp, fontFamily = VietnamPro, fontWeight = FontWeight.Bold, color = Color(0xFF133A94))
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            workplaces.forEach { place ->
                Row(
                    modifier = Modifier.fillMaxWidth().height(100.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(place.imageRes),
                        contentDescription = place.title,
                        modifier = Modifier.size(48.dp),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(place.title, fontWeight = FontWeight.SemiBold, fontSize = 16.sp, fontFamily = VietnamPro)
                        Text(place.description, fontSize = 14.sp, color = Color(0xFF5C758A), fontFamily = VietnamPro)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Contact Section
        // Contact Section
        Text("Contact Me", fontSize = 18.sp, fontFamily = VietnamPro, fontWeight = FontWeight.Bold, color = Color(0xFF133A94))
        Spacer(modifier = Modifier.height(8.dp))
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            // Phone contacts
            Row(
                modifier = Modifier.fillMaxWidth().height(60.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(R.drawable.phone_icon), // Replace with your phone icon
                    contentDescription = "Phone",
                    modifier = Modifier.size(48.dp).padding(end = 8.dp),
                    contentScale = ContentScale.Fit
                )
                Column {
                    Text("Personal Phone", fontWeight = FontWeight.Medium, fontFamily = VietnamPro)
                    Text("+1 (555) 123-4567", fontSize = 14.sp, color = Color.Gray)
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth().height(60.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(R.drawable.phone_icon), // Replace with your phone icon
                    contentDescription = "Phone",
                    modifier = Modifier.size(48.dp).padding(end = 8.dp),
                    contentScale = ContentScale.Fit
                )
                Column {
                    Text("Office Phone", fontWeight = FontWeight.Medium, fontFamily = VietnamPro)
                    Text("+1 (555) 987-6543", fontSize = 14.sp, color = Color.Gray)
                }
            }

            // Email contacts
            Row(
                modifier = Modifier.fillMaxWidth().height(60.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(R.drawable.email_icon), // Replace with your email icon
                    contentDescription = "Email",
                    modifier = Modifier.size(48.dp).padding(end = 8.dp),
                    contentScale = ContentScale.Fit
                )
                Column {
                    Text("Personal Email", fontWeight = FontWeight.Medium, fontFamily = VietnamPro)
                    Text("ethan.carter@email.com", fontSize = 14.sp, color = Color.Gray)
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth().height(60.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(R.drawable.email_icon), // Replace with your email icon
                    contentDescription = "Email",
                    modifier = Modifier.size(48.dp).padding(end = 8.dp),
                    contentScale = ContentScale.Fit
                )
                Column {
                    Text("Work Email", fontWeight = FontWeight.Medium, fontFamily = VietnamPro)
                    Text("ecarter@work.com", fontSize = 14.sp, color = Color.Gray)
                }
            }
        }
    }
}

data class Business(val title: String, val description: String, val imageRes: Int)

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    SABCVAAppTheme {
        ProfileScreen()
    }
}