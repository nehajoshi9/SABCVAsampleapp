
package com.example.sabcvasampleapp.presentation.createevent

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.layout.FlowRow
import kotlinx.coroutines.launch
import androidx.navigation.NavController
import java.util.*
import android.net.Uri
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class, ExperimentalFoundationApi::class)
@Composable
fun CreateEventScreen(navController: NavController) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val bringIntoViewRequester = remember { BringIntoViewRequester() }
    var invitedCoHosts by remember { mutableStateOf(listOf<String>()) }
    var eventTitle by remember { mutableStateOf("") }
    var eventDate by remember { mutableStateOf("") }
    var startTime by remember { mutableStateOf("") }
    var endTime by remember { mutableStateOf("") }
    var eventLocation by remember { mutableStateOf("") }
    var eventDescription by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()
    var selectedFileUri by remember { mutableStateOf<Uri?>(null) }
    var requiredFilled by remember { mutableStateOf(false) }

    requiredFilled = eventTitle.isNotBlank() && eventDate.isNotBlank() && eventLocation.isNotBlank() && eventDescription.isNotBlank() && startTime.isNotBlank() && endTime.isNotBlank() && invitedCoHosts.isNotEmpty()

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
    val calendar = Calendar.getInstance()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Create Event", color = Color.White, fontWeight = FontWeight.SemiBold)
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                actions = {
                    Button(
                        onClick = { /* Publish */ },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = Color(0xFFB00020)
                        ),
                        shape = RoundedCornerShape(50),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp),
                        modifier = Modifier.padding(end = 12.dp)
                    ) {
                        Text("Publish", fontWeight = FontWeight.Medium, fontSize = 14.sp)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color(0xFFB00020))
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .verticalScroll(scrollState)
                .padding(paddingValues)
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            TextFieldSection(true, "Event Title", eventTitle, { eventTitle = it }, "Give your event a name")

            Spacer(modifier = Modifier.height(16.dp))

            TextFieldSection(true,"Description", eventDescription, { eventDescription = it }, "Tell people what your event is about", isMultiline = true)

            Spacer(modifier = Modifier.height(16.dp))

            TextFieldSection(true, "Date", eventDate, {}, "MM/DD/YYYY", isReadOnly = true, onClick = {
                DatePickerDialog(context, { _, y, m, d ->
                    eventDate = String.format("%02d/%02d/%04d", m + 1, d, y)
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
            })

            Spacer(modifier = Modifier.height(16.dp))

            AccordionCard(true, true,"Event Duration", Icons.Default.AccessTime) {
                TextFieldSection(true, "Start Time", startTime, {}, "HH:MM", isReadOnly = true, onClick = {
                    TimePickerDialog(context, { _, h, m ->
                        startTime = String.format("%02d:%02d", h, m)
                    }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show()
                })

                Spacer(modifier = Modifier.height(12.dp))

                TextFieldSection(true, "End Time", endTime, {}, "HH:MM", isReadOnly = true, onClick = {
                    TimePickerDialog(context, { _, h, m ->
                        endTime = String.format("%02d:%02d", h, m)
                    }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show()
                })
            }

            Spacer(modifier = Modifier.height(16.dp))

// ...
            var locationQuery by remember { mutableStateOf(eventLocation) }
            val allAddresses = listOf(
                "123 Main St, New York, NY",
                "456 Maple Ave, Los Angeles, CA",
                "789 Oak Blvd, Chicago, IL",
                "1600 Pennsylvania Ave NW, Washington, DC",
                "1 Infinite Loop, Cupertino, CA"
            )
            val locationSuggestions = allAddresses.filter {
                it.contains(locationQuery, ignoreCase = true)
            }

            /*Text(
                text = "Location",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF374151)
            )
            Spacer(modifier = Modifier.height(4.dp))*/

            var showDropdown by remember { mutableStateOf(false) }
            TextFieldSection(
                label = "Location",
                value = locationQuery,
                onValueChange = {
                    locationQuery = it
                    showDropdown = locationQuery.length >= 4 && locationSuggestions.isNotEmpty()
                },
                placeholder = "Search"
            )

            if (showDropdown) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        locationSuggestions.forEach { address ->
                            Text(
                                text = address,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        showDropdown = false
                                        eventLocation = address
                                        locationQuery = address // to reflect selected value in text field
                                    }
                                    .padding(vertical = 8.dp)
                            )
                        }
                    }
                }
            }


            Spacer(modifier = Modifier.height(16.dp))

            // Upload Placeholder
            AccordionCard(false, false,"Add Media (Optional)", Icons.Default.IosShare) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                        .padding(top = 8.dp)
                        .bringIntoViewRequester(bringIntoViewRequester)
                        .onFocusChanged {
                            if (it.isFocused) {
                                coroutineScope.launch {
                                    bringIntoViewRequester.bringIntoView()
                                }
                            }
                        }
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
            }

            Spacer(modifier = Modifier.height(16.dp))


            AccordionCard(false, true,"Hosts", Icons.Default.People) {
                var coHostQuery by remember { mutableStateOf("") }

                val allProfiles = listOf("Alice Johnson", "Bob Smith", "Carmen Reyes", "David Chen",
                    "Eva Patel", "Google", "Facebook", "Amazon", "Microsoft")
                val cohostSuggestions = allProfiles.filter {
                    it.contains(coHostQuery, ignoreCase = true) && !invitedCoHosts.contains(it)
                }

                TextFieldSection(true, "Search for a co-host", coHostQuery, { coHostQuery = it }, "Search")

                if (coHostQuery.isNotBlank() && cohostSuggestions.isNotEmpty()) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Column(modifier = Modifier.padding(8.dp)) {
                            cohostSuggestions.forEach { name ->
                                Text(
                                    text = name,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            invitedCoHosts = invitedCoHosts + name
                                            coHostQuery = ""
                                        }
                                        .bringIntoViewRequester(bringIntoViewRequester)
                                        .onFocusChanged {
                                            if (it.isFocused) {
                                                coroutineScope.launch {
                                                    bringIntoViewRequester.bringIntoView()
                                                }
                                            }
                                        }
                                        .padding(vertical = 8.dp)
                                )
                            }
                        }
                    }
                }

                FlowRow(modifier = Modifier.padding(top = 8.dp)) {
                    invitedCoHosts.forEach { name ->
                        Surface(
                            color = Color(0xFFE0E0E0),
                            shape = RoundedCornerShape(50),
                            modifier = Modifier.padding(end = 4.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(text = name, fontSize = 14.sp)
                                Spacer(modifier = Modifier.width(6.dp))
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Remove",
                                    modifier = Modifier
                                        .size(16.dp)
                                        .clickable { invitedCoHosts = invitedCoHosts - name }
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))

            AccordionCard(false, false,"Sponsors", Icons.Default.AttachMoney) {
                var sponsorQuery by remember { mutableStateOf("") }
                var invitedSponsors by remember { mutableStateOf(listOf<String>()) }
                val allProfiles = listOf("Google", "Facebook", "Amazon", "Microsoft")
                val sponsorSuggestions = allProfiles.filter {
                    it.contains(sponsorQuery, ignoreCase = true) && !invitedSponsors.contains(it)
                }

                TextFieldSection(false,"Search for a sponsor", sponsorQuery, { sponsorQuery = it }, "Search")

                if (sponsorQuery.isNotBlank() && sponsorSuggestions.isNotEmpty()) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Column(modifier = Modifier.padding(8.dp)) {
                            sponsorSuggestions.forEach { name ->
                                Text(
                                    text = name,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            invitedSponsors = invitedSponsors + name
                                            sponsorQuery = ""
                                        }
                                        .bringIntoViewRequester(bringIntoViewRequester)
                                        .onFocusChanged {
                                            if (it.isFocused) {
                                                coroutineScope.launch {
                                                    bringIntoViewRequester.bringIntoView()
                                                }
                                            }
                                        }
                                        .padding(vertical = 8.dp)
                                )
                            }
                        }
                    }
                }

                FlowRow(modifier = Modifier.padding(top = 8.dp)) {
                    invitedSponsors.forEach { name ->
                        Surface(
                            color = Color(0xFFE0E0E0),
                            shape = RoundedCornerShape(50),
                            modifier = Modifier.padding(end = 4.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(text = name, fontSize = 14.sp)
                                Spacer(modifier = Modifier.width(6.dp))
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Remove",
                                    modifier = Modifier
                                        .size(16.dp)
                                        .clickable { invitedSponsors = invitedSponsors - name }
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}



@Composable
fun TextFieldSection(
    required: Boolean = true,
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    isMultiline: Boolean = false,
    isReadOnly: Boolean = false,
    onClick: (() -> Unit)? = null
) {
        Text(
            text = buildAnnotatedString {
                append(label)
                if(required) {
                withStyle(style = SpanStyle(color = Color.Red)) {
                    append(" *")
                }}
            },
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(4.dp))


    OutlinedTextField(
        value = value,
        textStyle = LocalTextStyle.current.copy(fontSize = 16.sp),
        onValueChange = onValueChange,
        placeholder = { Text(placeholder, fontSize = 16.sp) },
        modifier = Modifier
            .fillMaxWidth()
            .height(if (isMultiline) 120.dp else 56.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable(enabled = onClick != null) { onClick?.invoke() },
        shape = RoundedCornerShape(12.dp),
        readOnly = isReadOnly,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent,
            focusedContainerColor = Color(0xFFF2F2F2),
            unfocusedContainerColor = Color(0xFFF2F2F2)
        )
    )
}

@Composable
fun AccordionCard(
    isExpanded: Boolean,
    required: Boolean = true,
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onExpand: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit

) {
    var expanded by remember { mutableStateOf(isExpanded) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF9F9F9))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    expanded = !expanded
                    if (expanded) onExpand?.invoke()
                }
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = icon, contentDescription = null, tint = Color(0xFFB00020))
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = buildAnnotatedString {
                        append(title)
                        if(required) {
                        withStyle(style = SpanStyle(color = Color.Red)) {
                            append(" *")
                        }}
                    },

                 fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))

                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = Color.Gray
                )
            }
            if (expanded) {
                Spacer(modifier = Modifier.height(12.dp))
                content()
            }
        }
    }
}