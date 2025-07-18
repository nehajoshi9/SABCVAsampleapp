package com.example.sabcvasampleapp.neha.resources

import com.example.sabcvasampleapp.neha.resources.Repository.buildDate
import java.util.Calendar
import java.util.UUID

object Data {
    val allPersonProfiles = listOf(
        createPersonProfile(
            id = "P_${UUID.randomUUID()}",
            name = "Alice Johnson",
            email = "alice.johnson@example.com"
        ),
        createPersonProfile(
            id = "P_${UUID.randomUUID()}",
            name = "Bob Smith",
            email = "bob.smith@example.com"
        ),
        createPersonProfile(
            id = "P_${UUID.randomUUID()}",
            name = "Carmen Reyes",
            email = "carmen.reyes@example.com"
        ),
        createPersonProfile(
            id = "P_${UUID.randomUUID()}",
            name = "David Chen",
            email = "david.chen@example.com"
        ),
        createPersonProfile(
            id = "P_${UUID.randomUUID()}",
            name = "Eva Patel",
            email = "eva.patel@example.com"
        ), createPersonProfile(
            id = "P_${UUID.randomUUID()}",
            name = "Alice Johnson",
            email = "alice.johnson@example.com"
        ),
        createPersonProfile(
            id = "P_${UUID.randomUUID()}",
            name = "Bob Smith",
            email = "bob.smith@example.com"
        ),
        createPersonProfile(
            id = "P_${UUID.randomUUID()}",
            name = "Carmen Reyes",
            email = "carmen.reyes@example.com"
        ),
        createPersonProfile(
            id = "P_${UUID.randomUUID()}",
            name = "David Chen",
            email = "david.chen@example.com"
        ),
        createPersonProfile(
            id = "P_${UUID.randomUUID()}",
            name = "Eva Patel",
            email = "eva.patel@example.com"
        ), createPersonProfile(
            id = "P_${UUID.randomUUID()}",
            name = "Alice Johnson",
            email = "alice.johnson@example.com"
        ),
        createPersonProfile(
            id = "P_${UUID.randomUUID()}",
            name = "Bob Smith",
            email = "bob.smith@example.com"
        ),
        createPersonProfile(
            id = "P_${UUID.randomUUID()}",
            name = "Carmen Reyes",
            email = "carmen.reyes@example.com"
        ),
        createPersonProfile(
            id = "P_${UUID.randomUUID()}",
            name = "David Chen",
            email = "david.chen@example.com"
        ),
        createPersonProfile(
            id = "P_${UUID.randomUUID()}",
            name = "Eva Patel",
            email = "eva.patel@example.com"
        ), createPersonProfile(
            id = "P_${UUID.randomUUID()}",
            name = "Alice Johnson",
            email = "alice.johnson@example.com"
        ),
        createPersonProfile(
            id = "P_${UUID.randomUUID()}",
            name = "Bob Smith",
            email = "bob.smith@example.com"
        ),
        createPersonProfile(
            id = "P_${UUID.randomUUID()}",
            name = "Carmen Reyes",
            email = "carmen.reyes@example.com"
        ),
        createPersonProfile(
            id = "P_${UUID.randomUUID()}",
            name = "David Chen",
            email = "david.chen@example.com"
        ),
        createPersonProfile(
            id = "P_${UUID.randomUUID()}",
            name = "Eva Patel",
            email = "eva.patel@example.com"
        ), createPersonProfile(
            id = "P_${UUID.randomUUID()}",
            name = "Alice Johnson",
            email = "alice.johnson@example.com"
        ),
        createPersonProfile(
            id = "P_${UUID.randomUUID()}",
            name = "Bob Smith",
            email = "bob.smith@example.com"
        ),
        createPersonProfile(
            id = "P_${UUID.randomUUID()}",
            name = "Carmen Reyes",
            email = "carmen.reyes@example.com"
        ),
        createPersonProfile(
            id = "P_${UUID.randomUUID()}",
            name = "David Chen",
            email = "david.chen@example.com"
        ),
        createPersonProfile(
            id = "P_${UUID.randomUUID()}",
            name = "Eva Patel",
            email = "eva.patel@example.com"
        )
    )

    val allBusinessProfiles = listOf(
        createBusinessProfile(
            id = "B_${UUID.randomUUID()}",
            name = "Google",
            email = "contact@google.com"
        ),
        createBusinessProfile(
            id = "B_${UUID.randomUUID()}",
            name = "Facebook",
            email = "info@facebook.com"
        ),
        createBusinessProfile(
            id = "B_${UUID.randomUUID()}",
            name = "Amazon",
            email = "support@amazon.com"
        ),
        createBusinessProfile(
            id = "B_${UUID.randomUUID()}",
            name = "Microsoft",
            email = "hello@microsoft.com"
        )
    )

    val allProfiles = allPersonProfiles + allBusinessProfiles

    private val allEvents = mutableListOf<EventDetails>(
        EventDetails(
            id = UUID.randomUUID().toString(),
            title = "Design Workshop",
            startDate = buildDate(2025, Calendar.JULY, 17, 14, 0),
            endDate = buildDate(2025, Calendar.JULY, 17, 16, 0),
            location = "1600 Amphitheatre Parkway, Mountain View, CA",
            description = "Learn the latest UI/UX design trends and techniques in this hands-on workshop.",
            attendees = allPersonProfiles
                .shuffled()
                .take((6..allPersonProfiles.size).random())
                .map { it.name }, // take this line out
            comments = listOf(
                Comment("John Doe", "Looking forward to this event!", 1751992800000),
                Comment("Alice Kim", "Can't wait to join.", 1751820000000)
            ),
            hosts = allProfiles.shuffled().take((1..allProfiles.size).random()),
            sponsors = allBusinessProfiles.shuffled().take((0..allBusinessProfiles.size).random()),
            tags = listOf("Design", "UX", "Workshop")
        ),
        EventDetails(
            id = UUID.randomUUID().toString(),
            title = "Tech Conference 2025",
            startDate = buildDate(2025, Calendar.JULY, 22, 9, 0),
            endDate = buildDate(
                2025,
                Calendar.JULY,
                22,
                17,
                0
            ), // fixed the endDate which was accidentally set earlier
            location = "Downtown Convention Center",
            description = "Biggest tech event of the year with industry experts.",
            attendees = allPersonProfiles
                .shuffled()
                .take((0..allPersonProfiles.size).random())
                .map { it.name }, // take this line out
            comments = listOf(
                Comment("Raj Patel", "Super hyped!", 1751873800000)
            ),
            hosts = allProfiles.shuffled().take((1..allProfiles.size).random()),
            sponsors = allBusinessProfiles.shuffled().take((0..allBusinessProfiles.size).random()),
            tags = listOf("Tech", "AI", "Networking")
        )
    )


    val allAddresses = listOf(
        "123 Main St, New York, NY",
        "456 Maple Ave, Los Angeles, CA",
        "789 Oak Blvd, Chicago, IL",
        "1600 Pennsylvania Ave NW, Washington, DC",
        "1 Infinite Loop, Cupertino, CA"
    )

    val tagUsageMap = mutableMapOf(
        "Design" to 1,
        "UX" to 1,
        "Tech" to 1,
        "AI" to 1,
        "Networking" to 1,
        "Workshop" to 1
    )

    fun getAllEvents(): List<EventDetails> = allEvents.toList()
    fun getEventById(id: String): EventDetails? = allEvents.find { it.id == id }

    internal fun updateEvent(index: Int, event: EventDetails) {
        allEvents[index] = event
    }

    internal fun getAllEventsMutable(): MutableList<EventDetails> = allEvents
}