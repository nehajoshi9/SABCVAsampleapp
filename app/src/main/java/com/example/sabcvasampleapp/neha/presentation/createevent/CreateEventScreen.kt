package com.example.sabcvasampleapp.neha.presentation.createevent

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import java.util.Calendar
import androidx.compose.foundation.layout.*
import com.example.sabcvasampleapp.neha.resources.Repository
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.launch

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class,
    ExperimentalLayoutApi::class
)
@Composable
fun CreateEventScreen(navController: NavController, viewModel: CreateEventViewModel = viewModel()) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    val calendar = Calendar.getInstance()

    val eventTitle by viewModel.title.collectAsState()
    val eventDescription by viewModel.description.collectAsState()
    val eventDate by viewModel.eventDate.collectAsState()
    val startTime by viewModel.startTime.collectAsState()
    val endTime by viewModel.endTime.collectAsState()
    val eventLocation by viewModel.location.collectAsState()
    val hosts by viewModel.cohosts.collectAsState()
    val sponsors by viewModel.sponsors.collectAsState()
    val selectedFileUri by viewModel.fileUri.collectAsState()
    val showDialog by viewModel.showDialog.collectAsState()
    val tags by viewModel.tags.collectAsState()

    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = { uri ->
            uri?.let {
                viewModel.setFile(uri)
                context.contentResolver.takePersistableUriPermission(
                    uri, Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            }
        }
    )

    var locationQuery by remember { mutableStateOf(eventLocation) }
    var coHostQuery by remember { mutableStateOf("") }
    var sponsorQuery by remember { mutableStateOf("") }
    var tagQuery by remember { mutableStateOf("") }

    val locationSuggestions by remember(locationQuery) {
        derivedStateOf {
            Repository.allAddresses.filter {
                it.contains(locationQuery, ignoreCase = true)
            }
        }
    }
    val cohostSuggestions = Repository.allProfiles.filter {
        it.contains(coHostQuery, ignoreCase = true) && !hosts.contains(it)
    }
    val sponsorSuggestions = Repository.allBusinessProfiles.filter {
        it.contains(sponsorQuery, ignoreCase = true) && !sponsors.contains(it)
    }

    val tagSuggestions = Repository.tagUsageMap.keys.filter {
        it.contains(tagQuery, ignoreCase = true) && !tags.contains(it)
    }

    var showDropdown by remember { mutableStateOf(false) }

    //val isValid = locationSuggestions.contains(locationQuery)

    if (showDialog) {
        val errorList by viewModel.errors.collectAsState()

        BasicAlertDialog(
            onDismissRequest = { viewModel.resetValidationDialog() },
            content = {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    tonalElevation = 6.dp
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text("Please fix the following:", fontWeight = FontWeight.SemiBold)

                        errorList.forEach { error ->
                            Text("• $error", color = Color(0xFFC50326))
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            TextButton(onClick = { viewModel.resetValidationDialog() }) {
                                Text("OK")
                            }
                        }
                    }
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Create Event", color = Color.White, fontWeight = FontWeight.SemiBold)
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    Button(
                        onClick = {
                            viewModel.triggerValidationDialog()
                            if (viewModel.isValid()) {
                                val startDateCal = Calendar.getInstance().apply { time = eventDate }
                                val startTimeCal = Calendar.getInstance().apply { time = startTime }
                                val endTimeCal = Calendar.getInstance().apply { time = endTime }

                                startDateCal.set(
                                    Calendar.HOUR_OF_DAY,
                                    startTimeCal.get(Calendar.HOUR_OF_DAY)
                                )
                                startDateCal.set(Calendar.MINUTE, startTimeCal.get(Calendar.MINUTE))
                                startDateCal.set(Calendar.SECOND, startTimeCal.get(Calendar.SECOND))
                                startDateCal.set(
                                    Calendar.MILLISECOND,
                                    startTimeCal.get(Calendar.MILLISECOND)
                                )

                                val endDateCal = Calendar.getInstance().apply { time = eventDate }
                                endDateCal.set(
                                    Calendar.HOUR_OF_DAY,
                                    endTimeCal.get(Calendar.HOUR_OF_DAY)
                                )
                                endDateCal.set(Calendar.MINUTE, endTimeCal.get(Calendar.MINUTE))
                                endDateCal.set(Calendar.SECOND, endTimeCal.get(Calendar.SECOND))
                                endDateCal.set(
                                    Calendar.MILLISECOND,
                                    endTimeCal.get(Calendar.MILLISECOND)
                                )

                                //Log.d("DEBUG", "Start time: ${startTime}")
                                //Log.d("DEBUG", "End time: ${endTime}")

                                val mimeType = selectedFileUri?.let {
                                    context.contentResolver.getType(it)
                                }
                                val isImage = mimeType?.startsWith("image/") == true

                                Repository.addEvent(
                                    eventTitle,
                                    startDateCal.time,
                                    endDateCal.time,
                                    eventLocation,
                                    eventDescription,
                                    hosts,
                                    selectedFileUri,
                                    sponsors,
                                    tags, isImage
                                )
                                navController.navigate("calendar")
                                // Publish
                            }
                        },
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
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(
                        0xFFB00020
                    )
                )
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
            TextFieldSection(
                true,
                "Event Title",
                eventTitle,
                viewModel::updateTitle,
                "Give your event a name"
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextFieldSection(
                true,
                "Description",
                eventDescription,
                viewModel::updateDescription,
                "Tell people what your event is about",
                isMultiline = true
            )

            Spacer(modifier = Modifier.height(16.dp))


            TextFieldSection(
                required = true,
                label = "Date",
                value = Repository.getFormattedDate(eventDate), // or show "Pick a date" if you want a hint
                onValueChange = {},
                placeholder = "MM/DD/YYYY",
                allowTyping = false,
                onClick = {
                    DatePickerDialog(
                        context,
                        { _, y, m, d -> viewModel.updateDateFromPicker(y, m, d) },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                    ).show()
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            AccordionCard(true, true, "Event Duration", Icons.Default.AccessTime) {
                TextFieldSection(
                    required = true,
                    label = "Start Time",
                    value = Repository.getFormattedTime(startTime),
                    onValueChange = {},
                    placeholder = "HH:MM",
                    allowTyping = false,
                    onClick = {
                        TimePickerDialog(
                            context,
                            { _, h, m -> viewModel.updateStartTime(h, m) },
                            calendar.get(Calendar.HOUR_OF_DAY),
                            calendar.get(Calendar.MINUTE),
                            false
                        ).show()
                    }
                )

                Spacer(modifier = Modifier.height(12.dp))

                TextFieldSection(
                    required = true,
                    label = "End Time",
                    value = Repository.getFormattedTime(endTime),
                    onValueChange = {},
                    placeholder = "HH:MM",
                    allowTyping = false,
                    onClick = {
                        TimePickerDialog(
                            context,
                            { _, h, m -> viewModel.updateEndTime(h, m) },
                            calendar.get(Calendar.HOUR_OF_DAY),
                            calendar.get(Calendar.MINUTE),
                            false
                        ).show()
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextFieldSection(
                label = "Location",
                value = locationQuery,
                onValueChange = { newQuery ->
                    locationQuery = newQuery
                    viewModel.updateLocation(newQuery)
                    viewModel.updateIsLocationValid(false)
                    showDropdown = newQuery.length >= 4 && locationSuggestions.isNotEmpty()
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
                                        viewModel.updateIsLocationValid(true)
                                        viewModel.updateLocation(address)
                                        locationQuery = address
                                    }
                                    .padding(vertical = 8.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Upload Placeholder
            AccordionCard(false, false, "Add Media (Optional)", Icons.Default.IosShare) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                        .padding(top = 8.dp)
                        .border(
                            BorderStroke(2.dp, Color.Red), // red border
                            shape = RoundedCornerShape(12.dp)
                        )
                        .clickable { // 👈 trigger picker on click
                            filePickerLauncher.launch(arrayOf("*/*"))
                        },
                    contentAlignment = Alignment.Center,
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = if (selectedFileUri != null) Icons.Default.Check else Icons.Default.IosShare, // ← you'll add this
                            contentDescription = "Upload Icon",
                            tint = if (selectedFileUri != null) Color(0xFF4CAF50) else Color(
                                0xFFe30029
                            ),
                            modifier = Modifier.size(40.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = if (selectedFileUri != null) "File Selected" else "Click to upload",
                            color = if (selectedFileUri != null) Color(0xFF4CAF50) else Color(
                                0xFFe30029
                            )
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

            AccordionCard(false, true, "Hosts", Icons.Default.People) {
                val bringIntoViewRequester = remember { BringIntoViewRequester() }

                TextFieldSection(
                    true,
                    "Search for a host",
                    coHostQuery,
                    { coHostQuery = it },
                    "Search"
                )

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
                                            viewModel.addCohost(name)
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

                FlowRow(modifier = Modifier.padding(top = 8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    hosts.forEach { name ->
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
                                        .clickable { viewModel.removeCohost(name) }
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            AccordionCard(false, false, "Sponsors (Optional)", Icons.Default.AttachMoney) {
                val bringIntoViewRequester = remember { BringIntoViewRequester() }

                TextFieldSection(
                    false,
                    "Search for a sponsor",
                    sponsorQuery,
                    { sponsorQuery = it },
                    "Search"
                )

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
                                            viewModel.addSponsor(name)
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

                FlowRow(modifier = Modifier.padding(top = 8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    sponsors.forEach { name ->
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
                                        .clickable { viewModel.removeSponsor(name) }
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))


            AccordionCard(false, false, "Tags (Optional)", Icons.Default.Tag) {
                // Tag input section (with presets + custom tag support)
                TextFieldSection(
                    required = false,
                    label = "Enter a tag",
                    value = tagQuery,
                    onValueChange = { tagQuery = it },
                    placeholder = "Search", trailingContent = {
                        if (tagQuery.isNotBlank()) {
                        IconButton(
                            onClick = {
                                    viewModel.addTag(tagQuery.trim())
                                    tagQuery = ""
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.AddCircleOutline,
                                contentDescription = "Add Tag",
                                tint = Color(0xFF2596be)
                            )
                        }
                        }
                    }
                )

                // Tag Suggestions Dropdown
                if (tagQuery.isNotBlank() && tagSuggestions.isNotEmpty()) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Column(modifier = Modifier.padding(8.dp)) {
                            tagSuggestions.forEach { tag ->
                                Text(
                                    text = tag,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            viewModel.addTag(tag)
                                            tagQuery = ""
                                        }
                                        .padding(vertical = 8.dp)
                                )
                            }
                        }
                    }
                }

                // Display Selected Tags
                FlowRow(modifier = Modifier.padding(top = 8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    tags.forEach { tag ->
                        Surface(
                            color = Color(0xFFE0E0E0),
                            shape = RoundedCornerShape(50),
                            modifier = Modifier.padding(end = 4.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(text = tag, fontSize = 14.sp)
                                Spacer(modifier = Modifier.width(6.dp))
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Remove",
                                    modifier = Modifier
                                        .size(16.dp)
                                        .clickable { viewModel.removeTag(tag) }
                                )
                            }
                        }
                    }
                }
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
    allowTyping: Boolean = true,
    onClick: (() -> Unit)? = null,
    trailingContent: (@Composable () -> Unit)? = null
) {
    Text(
        text = buildAnnotatedString {
            append(label)
            if (required) {
                withStyle(style = SpanStyle(color = Color.Red)) {
                    append(" *")
                }
            }
        },
        fontSize = 16.sp,
        fontWeight = FontWeight.Medium,
        color = Color.Black
    )
    Spacer(modifier = Modifier.height(4.dp))

    if (!allowTyping) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(if (isMultiline) 120.dp else 56.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFFF2F2F2))
                .clickable(enabled = onClick != null) { onClick?.invoke() }
        ) {
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                placeholder = { Text(placeholder, fontSize = 16.sp) },
                modifier = Modifier.fillMaxSize(),
                textStyle = LocalTextStyle.current.copy(fontSize = 16.sp),
                shape = RoundedCornerShape(12.dp),
                readOnly = true,
                enabled = false,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    disabledBorderColor = Color.Transparent,
                    disabledContainerColor = Color(0xFFF2F2F2),
                    disabledTextColor = Color.Black,
                    disabledLabelColor = Color.Gray,
                    disabledPlaceholderColor = Color.DarkGray
                )
            )
        }
    } else {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, fontSize = 16.sp, color = Color.DarkGray) },
            modifier = Modifier
                .fillMaxWidth()
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
            ), trailingIcon = trailingContent
        )
    }
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
                        if (required) {
                            withStyle(style = SpanStyle(color = Color.Red)) {
                                append(" *")
                            }
                        }
                    },
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(1f)
                )
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