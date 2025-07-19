package com.example.sabcvasampleapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class BusinessEvent(
    val title: String,
    val dateLocation: String,
    val imageRes: Int
)


data class contactInformation(
    val contact: String,
    val type: ContactType
)

enum class ContactType{
    PHONE, EMAIL, WEBSITE
}


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BusinessProfileScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onShareClick: () -> Unit = {}
) {
    val tags = listOf("Business", "Entrepreneur", "Startup", "Management")
    val ownedBusinesses = listOf(
        Business("Carter's Cafe", "Artisan breads and pastries", R.drawable.cafe_pfp),
        Business("TechSolutions", "IT Services Company", R.drawable.pfp)
    )
    val workplaces = listOf(
        Business("Tech Innovators Inc.", "Product Development", R.drawable.pfp)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Header with Back and Share buttons
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            // Back Button
            IconButton(
                onClick = onBackClick,
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(
                    painter = painterResource(R.drawable.back_arrow),
                    contentDescription = "Back",
                    modifier = Modifier.size(24.dp))
            }

            // Title
            Text(
                text = "Business Profile",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = VietnamPro,
                modifier = Modifier.align(Alignment.Center)
            )

            // Share Button
            IconButton(
                onClick = onShareClick,
                modifier = Modifier.align(Alignment.CenterEnd)
            ) {
                Icon(
                    painter = painterResource(R.drawable.share_icon),
                    contentDescription = "Share",
                    modifier = Modifier.size(24.dp))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Profile Picture
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(R.drawable.business_pfp),
                contentDescription = "Business Profile Picture",
                modifier = Modifier
                    .size(100.dp)
                    .clip(MaterialTheme.shapes.medium),
                contentScale = ContentScale.Crop
            )
        }

        // Business Name and Type
        Text(
            text = "Tech Innovators Inc.",
            fontSize = 24.sp,
            fontFamily = VietnamPro,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Text(
            text = "Innovating the future, today.",
            fontSize = 16.sp,
            fontFamily = VietnamPro,
            color = Color(0xFF5C758A),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Text(
            text = "Industry: Technology",
            fontSize = 16.sp,
            fontFamily = VietnamPro,
            color = Color(0xFF5C758A),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Business Tags
        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalArrangement = Arrangement.Center
        ) {
            listOf("Software Development", "AI Solutions", "Cloud Computing").forEach { tag ->
                AssistChip(
                    onClick = { /* Handle tag click */ },
                    label = { Text(tag, fontFamily = VietnamPro, fontWeight = FontWeight.Medium, fontSize = 13.sp) },
                    modifier = Modifier.padding(4.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = Color(0xFFEBEDF2)
                    ),
                    border = null
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Business Description
        Text(
            text = "About Us",
            fontSize = 18.sp,
            color = Color(0xFF133A94),
            fontFamily = VietnamPro,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Tech Innovators Inc. is at the forefront of technological advancement, dedicated to creating cutting-edge solutions that redefine industries. Our team of experts is committed to excellence, driving innovation, and delivering unparalleled value to our clients. We strive to shape a future where technology empowers and transforms.",
            fontSize = 16.sp,
            fontFamily = VietnamPro,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Contact Information
        Text(
            text = "Contact",
            fontSize = 18.sp,
            color = Color(0xFF133A94),
            fontFamily = VietnamPro,
            fontWeight = FontWeight.Bold
        )

        Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
            val profileContacts = listOf(contactInformation("+1 (555) 123-4567", ContactType.PHONE), contactInformation("info@techinnovators.com", ContactType.EMAIL),
                contactInformation("techinnovators.com", ContactType.WEBSITE))
            profileContacts.forEach { contact ->
                ContactInfo(contact = contact)
            }
        }



        Spacer(modifier = Modifier.height(24.dp))

        // Recent Events Section
        Text(
            text = "Recent Events",
            fontSize = 18.sp,
            color = Color(0xFF133A94),
            fontFamily = VietnamPro,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        val recentEvents = listOf(
            BusinessEvent(
                "Tech Summit 2025",
                "June 15, 2025 | Williamsburg, VA",
                R.drawable.event1
            ),
            BusinessEvent(
                "Product Launch",
                "May 22, 2025 | Richmond, VA",
                R.drawable.event2
            ),
            BusinessEvent(
                "Innovation Workshop",
                "April 10, 2025 | Virginia Beach, VA",
                R.drawable.event1
            )
        )

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(recentEvents) { event ->
                EventCard(event = event)
            }
        }
    }
}

@Composable
private fun ContactInfo(contact: contactInformation) {
    Row(
        modifier = Modifier.fillMaxWidth().height(60.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(
                when(contact.type){
                    ContactType.EMAIL -> R.drawable.email_icon
                    ContactType.PHONE -> R.drawable.phone_icon
                    ContactType.WEBSITE -> R.drawable.web_icon
                }
            ),
            contentDescription = "Email",
            modifier = Modifier.size(48.dp).padding(end = 8.dp),
            contentScale = ContentScale.Fit
        )
        Column {
            Text(contact.contact, fontSize = 16.sp, color = Color(0xFF0377EB), style = TextStyle(textDecoration = TextDecoration.Underline))
        }
    }

}

@Composable
private fun EventCard(event: BusinessEvent) {
    Column(
        modifier = Modifier.width(200.dp)
    ) {
        Image(
            painter = painterResource(event.imageRes),
            contentDescription = event.title,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(4f/3f)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = event.title,
            fontWeight = FontWeight.SemiBold,
            fontFamily = VietnamPro,
            fontSize = 16.sp
        )

        Text(
            text = event.dateLocation,
            color = Color(0xFF5C758A),
            fontSize = 14.sp,
            fontFamily = VietnamPro
        )
    }
}

@Preview(showBackground = true)
@Composable
fun BusinessProfilePreview() {
    MaterialTheme {
        BusinessProfileScreen(
            onBackClick = {},
            onShareClick = {}
        )
    }
}