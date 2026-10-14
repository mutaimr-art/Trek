package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.Bike
import com.example.data.NegotiationOffer
import com.example.ui.components.CyanPinkGradient
import com.example.ui.theme.DarkNavyBackground
import com.example.ui.theme.DarkSurfaceBorder
import com.example.ui.theme.DarkSurfaceCard
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonPink
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.WhatsAppGreen
import com.example.util.IntentHelpers
import java.util.UUID

@Composable
fun NegotiateScreen(
    allBikes: List<Bike>,
    recentOffers: List<NegotiationOffer>,
    onSubmitOffer: (NegotiationOffer) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    var selectedBikeIndex by remember { mutableStateOf(0) }
    var isDropdownExpanded by remember { mutableStateOf(false) }

    val currentBike = allBikes.getOrNull(selectedBikeIndex) ?: allBikes.first()

    var customerName by remember { mutableStateOf("") }
    var customerPhone by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    var offerAmount by remember(currentBike.id) {
        mutableFloatStateOf((currentBike.priceKSh * 0.88f).coerceAtLeast(currentBike.minNegotiableKSh.toFloat()))
    }

    val destinations = listOf(
        "Store Pickup (Opposite Mavuno Fertilizer, Athi River)",
        "Delivery: Athi River / Lukenya / Greatwall",
        "Delivery: Kitengela / Syokimau / MLOLONGO",
        "Delivery: Nairobi CBD / Westlands / Kasarani",
        "Parcel: Machakos / Mombasa / Kisumu / Upcountry"
    )
    var selectedDestination by remember { mutableStateOf(destinations[0]) }

    val currentOfferInt = offerAmount.toInt()
    val savings = currentBike.priceKSh - currentOfferInt

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(DarkNavyBackground),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Title & Pitch
        item {
            Column {
                Text(
                    text = "DIRECT WHATSAPP",
                    style = MaterialTheme.typography.displaySmall,
                    color = TextPrimary,
                    fontWeight = FontWeight.Black
                )
                Text(
                    text = "PRICE NEGOTIATOR",
                    style = MaterialTheme.typography.displaySmall,
                    color = NeonPink,
                    fontWeight = FontWeight.Black
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "We welcome all genuine offers! Pick your bike model, set your offer, and send it directly to our manager at +254 785 638 731.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
            }
        }

        // Bike Picker dropdown card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
                border = androidx.compose.foundation.BorderStroke(1.dp, NeonCyan)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "1. SELECT BICYCLE MODEL",
                        style = MaterialTheme.typography.labelMedium,
                        color = NeonCyan,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(DarkSurfaceElevated)
                            .border(1.dp, DarkSurfaceBorder, RoundedCornerShape(12.dp))
                            .clickable { isDropdownExpanded = true }
                            .padding(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = currentBike.name,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                Text(
                                    text = "${currentBike.categoryLabel} • Listed: ${currentBike.displayPrice}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondary
                                )
                            }
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = "Select",
                                tint = NeonCyan
                            )
                        }

                        DropdownMenu(
                            expanded = isDropdownExpanded,
                            onDismissRequest = { isDropdownExpanded = false },
                            modifier = Modifier.background(DarkSurfaceCard)
                        ) {
                            allBikes.forEachIndexed { index, bike ->
                                DropdownMenuItem(
                                    text = {
                                        Column {
                                            Text(
                                                text = "${bike.name} (${bike.displayPrice})",
                                                color = TextPrimary,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Text(
                                                text = bike.categoryLabel,
                                                color = TextSecondary,
                                                fontSize = 11.sp
                                            )
                                        }
                                    },
                                    onClick = {
                                        selectedBikeIndex = index
                                        isDropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }

        // Offer Slider & Discount
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
                border = androidx.compose.foundation.BorderStroke(1.dp, DarkSurfaceBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "2. PROPOSED OFFER",
                        style = MaterialTheme.typography.labelMedium,
                        color = NeonCyan,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Text(
                            text = "KSh %,d".format(currentOfferInt),
                            style = MaterialTheme.typography.displayMedium,
                            fontWeight = FontWeight.Black,
                            color = NeonPink
                        )

                        if (savings > 0) {
                            val percent = (savings.toFloat() / currentBike.priceKSh * 100).toInt()
                            Box(
                                modifier = Modifier
                                    .background(Color(0x2225D366), RoundedCornerShape(6.dp))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "Save KSh %,d (%d%%)".format(savings, percent),
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = WhatsAppGreen
                                )
                            }
                        }
                    }

                    Slider(
                        value = offerAmount,
                        onValueChange = { offerAmount = it },
                        valueRange = (currentBike.minNegotiableKSh * 0.9f)..(currentBike.priceKSh * 1.0f),
                        steps = 15,
                        colors = SliderDefaults.colors(
                            thumbColor = NeonCyan,
                            activeTrackColor = NeonPink,
                            inactiveTrackColor = DarkSurfaceElevated
                        )
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Negotiation floor: ${currentBike.displayMinNegotiable}",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextMuted
                        )
                        Text(
                            text = "Store tag: ${currentBike.displayPrice}",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextMuted
                        )
                    }
                }
            }
        }

        // Contact info & destination
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
                border = androidx.compose.foundation.BorderStroke(1.dp, DarkSurfaceBorder)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "3. YOUR DETAILS & COLLECTION",
                        style = MaterialTheme.typography.labelMedium,
                        color = NeonCyan,
                        fontWeight = FontWeight.Bold
                    )

                    OutlinedTextField(
                        value = customerName,
                        onValueChange = { customerName = it },
                        label = { Text("Your Full Name", color = TextSecondary) },
                        leadingIcon = {
                            Icon(Icons.Default.Person, contentDescription = null, tint = NeonCyan)
                        },
                        placeholder = { Text("e.g. Dennis Kiprop", color = TextMuted) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NeonCyan,
                            unfocusedBorderColor = DarkSurfaceBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            focusedContainerColor = DarkSurfaceElevated,
                            unfocusedContainerColor = DarkSurfaceElevated
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    OutlinedTextField(
                        value = customerPhone,
                        onValueChange = { customerPhone = it },
                        label = { Text("Phone Number", color = TextSecondary) },
                        leadingIcon = {
                            Icon(Icons.Default.Phone, contentDescription = null, tint = NeonCyan)
                        },
                        placeholder = { Text("e.g. 0722000000", color = TextMuted) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NeonCyan,
                            unfocusedBorderColor = DarkSurfaceBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            focusedContainerColor = DarkSurfaceElevated,
                            unfocusedContainerColor = DarkSurfaceElevated
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Text(
                        text = "Pickup / Delivery Destination",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondary,
                        fontWeight = FontWeight.Bold
                    )

                    destinations.forEach { dest ->
                        val isSelected = dest == selectedDestination
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) Color(0x2200E5FF) else DarkSurfaceElevated)
                                .border(
                                    1.dp,
                                    if (isSelected) NeonCyan else DarkSurfaceBorder,
                                    RoundedCornerShape(8.dp)
                                )
                                .clickable { selectedDestination = dest }
                                .padding(10.dp)
                        ) {
                            Text(
                                text = dest,
                                style = MaterialTheme.typography.bodySmall,
                                color = if (isSelected) TextPrimary else TextSecondary,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }

                    OutlinedTextField(
                        value = notes,
                        onValueChange = { notes = it },
                        label = { Text("Special notes or questions (Optional)", color = TextSecondary) },
                        placeholder = { Text("e.g. Can you hold it for me until Saturday?", color = TextMuted) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NeonCyan,
                            unfocusedBorderColor = DarkSurfaceBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            focusedContainerColor = DarkSurfaceElevated,
                            unfocusedContainerColor = DarkSurfaceElevated
                        ),
                        shape = RoundedCornerShape(12.dp),
                        maxLines = 2
                    )
                }
            }
        }

        // Send Offer CTA
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(CyanPinkGradient)
                    .clickable {
                        val senderName = customerName.ifBlank { "Athi River Customer" }
                        val senderPhone = customerPhone.ifBlank { "Not provided" }
                        val fullMessage = buildString {
                            appendLine("Hello TREK Athi River,")
                            appendLine("👤 Name: $senderName")
                            appendLine("📞 Phone: $senderPhone")
                            appendLine("🚴 I want to negotiate for: ${currentBike.name}")
                            appendLine("🏷️ Listed Price: ${currentBike.displayPrice}")
                            appendLine("💰 My Offer: KSh %,d".format(currentOfferInt))
                            appendLine("📍 Destination: $selectedDestination")
                            if (notes.isNotBlank()) {
                                appendLine("💬 Note: $notes")
                            }
                            appendLine("What is your best price on this?")
                        }

                        val offerRecord = NegotiationOffer(
                            id = UUID.randomUUID().toString(),
                            customerName = senderName,
                            customerPhone = senderPhone,
                            bikeId = currentBike.id,
                            bikeName = currentBike.name,
                            listedPriceKSh = currentBike.priceKSh,
                            offerPriceKSh = currentOfferInt,
                            deliveryOption = selectedDestination,
                            paymentOption = "M-PESA / Cash",
                            notes = notes
                        )
                        onSubmitOffer(offerRecord)
                        IntentHelpers.openWhatsApp(context, fullMessage)
                    }
                    .padding(vertical = 16.dp)
                    .testTag("submit_negotiation_whatsapp_btn"),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Message,
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "SEND OFFER ON WHATSAPP (+254 785 638 731)",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Black,
                        color = Color.Black
                    )
                }
            }
        }

        // Recent Offers History if any
        if (recentOffers.isNotEmpty()) {
            item {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "YOUR RECENT OFFERS",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = NeonCyan
                )
            }

            items(recentOffers) { offer ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
                    border = androidx.compose.foundation.BorderStroke(1.dp, DarkSurfaceBorder)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = offer.bikeName,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                text = "Offered: KSh %,d (Listed: KSh %,d)".format(
                                    offer.offerPriceKSh,
                                    offer.listedPriceKSh
                                ),
                                style = MaterialTheme.typography.bodySmall,
                                color = NeonPink
                            )
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFF163E24))
                                .clickable {
                                    val msg = "Hi TREK Athi River, following up on my offer of KSh %,d for ${offer.bikeName}.".format(offer.offerPriceKSh)
                                    IntentHelpers.openWhatsApp(context, msg)
                                }
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "Follow Up",
                                style = MaterialTheme.typography.labelSmall,
                                color = WhatsAppGreen,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(60.dp))
        }
    }
}
