package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NegotiationOfferSheet(
    bike: Bike,
    onDismiss: () -> Unit,
    onSubmitOffer: (NegotiationOffer) -> Unit
) {
    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var customerName by remember { mutableStateOf("") }
    var customerPhone by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }

    // Initial offer value around 10-15% discount
    var offerAmount by remember {
        mutableFloatStateOf((bike.priceKSh * 0.88f).coerceAtLeast(bike.minNegotiableKSh.toFloat()))
    }

    val deliveryOptions = listOf(
        "Store Pickup (Opposite Mavuno Fertilizer)",
        "Delivery (Athi River / Kitengela / Syokimau / MLOLONGO)",
        "Countrywide Parcel (Machakos / Nairobi / Coast)"
    )
    var selectedDelivery by remember { mutableStateOf(deliveryOptions[0]) }

    val paymentOptions = listOf(
        "M-PESA on Collection",
        "Cash on Pickup",
        "M-PESA Send Money / Till"
    )
    var selectedPayment by remember { mutableStateOf(paymentOptions[0]) }

    val currentOfferInt = offerAmount.toInt()
    val savings = bike.priceKSh - currentOfferInt

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = DarkNavyBackground,
        scrimColor = Color(0xCC000000),
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(bottom = 36.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "NEGOTIATE PRICE",
                        style = MaterialTheme.typography.headlineLarge,
                        color = NeonCyan,
                        fontWeight = FontWeight.Black
                    )
                    Text(
                        text = "Athi River Direct WhatsApp Offer",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .size(36.dp)
                        .background(DarkSurfaceElevated, CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = TextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Selected Bike Summary Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(DarkSurfaceCard, RoundedCornerShape(16.dp))
                    .border(1.dp, DarkSurfaceBorder, RoundedCornerShape(16.dp))
                    .padding(14.dp)
            ) {
                Column {
                    Text(
                        text = bike.categoryLabel,
                        style = MaterialTheme.typography.labelSmall,
                        color = NeonCyan
                    )
                    Text(
                        text = bike.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Listed Price: ${bike.displayPrice}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary
                        )
                        Box(
                            modifier = Modifier
                                .background(Color(0x22FF2A6D), RoundedCornerShape(6.dp))
                                .border(1.dp, NeonPink, RoundedCornerShape(6.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "Negotiable Range",
                                style = MaterialTheme.typography.labelSmall,
                                color = NeonPink,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Interactive Offer Slider
            Text(
                text = "YOUR OFFERED PRICE (KSh)",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Black,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = "KSh %,d".format(currentOfferInt),
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Black,
                    color = NeonPink
                )

                if (savings > 0) {
                    val discountPercent = (savings.toFloat() / bike.priceKSh * 100).toInt()
                    Text(
                        text = "Discount: KSh %,d (%d%%)".format(savings, discountPercent),
                        style = MaterialTheme.typography.labelMedium,
                        color = WhatsAppGreen,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Slider(
                value = offerAmount,
                onValueChange = { offerAmount = it },
                valueRange = (bike.minNegotiableKSh * 0.9f)..(bike.priceKSh * 1.0f),
                steps = 15,
                colors = SliderDefaults.colors(
                    thumbColor = NeonCyan,
                    activeTrackColor = NeonPink,
                    inactiveTrackColor = DarkSurfaceElevated
                ),
                modifier = Modifier.testTag("offer_slider")
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Lowest Guide: KSh %,d".format(bike.minNegotiableKSh),
                    style = MaterialTheme.typography.labelSmall,
                    color = TextMuted
                )
                Text(
                    text = "Listed: ${bike.displayPrice}",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextMuted
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Name Input
            OutlinedTextField(
                value = customerName,
                onValueChange = { customerName = it },
                label = { Text("Your Name", color = TextSecondary) },
                leadingIcon = {
                    Icon(Icons.Default.Person, contentDescription = null, tint = NeonCyan)
                },
                placeholder = { Text("e.g. Brian Otieno", color = TextMuted) },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("input_customer_name"),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = NeonCyan,
                    unfocusedBorderColor = DarkSurfaceBorder,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary,
                    focusedContainerColor = DarkSurfaceCard,
                    unfocusedContainerColor = DarkSurfaceCard
                ),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Phone Input
            OutlinedTextField(
                value = customerPhone,
                onValueChange = { customerPhone = it },
                label = { Text("Phone / WhatsApp Number", color = TextSecondary) },
                leadingIcon = {
                    Icon(Icons.Default.Phone, contentDescription = null, tint = NeonCyan)
                },
                placeholder = { Text("e.g. 0712345678", color = TextMuted) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("input_customer_phone"),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = NeonCyan,
                    unfocusedBorderColor = DarkSurfaceBorder,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary,
                    focusedContainerColor = DarkSurfaceCard,
                    unfocusedContainerColor = DarkSurfaceCard
                ),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Delivery Option Picker
            Text(
                text = "Collection / Delivery Preference",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = TextSecondary
            )
            Spacer(modifier = Modifier.height(6.dp))
            deliveryOptions.forEach { option ->
                val isSelected = option == selectedDelivery
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 3.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isSelected) Color(0x2200E5FF) else DarkSurfaceCard)
                        .border(
                            1.dp,
                            if (isSelected) NeonCyan else DarkSurfaceBorder,
                            RoundedCornerShape(8.dp)
                        )
                        .clickable { selectedDelivery = option }
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = option,
                        style = MaterialTheme.typography.bodySmall,
                        color = if (isSelected) TextPrimary else TextSecondary,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Optional Note / Trade-in
            OutlinedTextField(
                value = note,
                onValueChange = { note = it },
                label = { Text("Questions / Trade-in inquiries (Optional)", color = TextSecondary) },
                placeholder = { Text("e.g. Can I test ride tomorrow morning?", color = TextMuted) },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = NeonCyan,
                    unfocusedBorderColor = DarkSurfaceBorder,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary,
                    focusedContainerColor = DarkSurfaceCard,
                    unfocusedContainerColor = DarkSurfaceCard
                ),
                shape = RoundedCornerShape(12.dp),
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Send Offer on WhatsApp Button
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
                            appendLine("🚴 I want to negotiate for: ${bike.name}")
                            appendLine("🏷️ Listed Price: ${bike.displayPrice}")
                            appendLine("💰 My Offer: KSh %,d".format(currentOfferInt))
                            appendLine("📍 Delivery: $selectedDelivery")
                            appendLine("💳 Payment: $selectedPayment")
                            if (note.isNotBlank()) {
                                appendLine("💬 Note: $note")
                            }
                            appendLine("Can you accept this offer or give your best counter-offer?")
                        }

                        val offerRecord = NegotiationOffer(
                            id = UUID.randomUUID().toString(),
                            customerName = senderName,
                            customerPhone = senderPhone,
                            bikeId = bike.id,
                            bikeName = bike.name,
                            listedPriceKSh = bike.priceKSh,
                            offerPriceKSh = currentOfferInt,
                            deliveryOption = selectedDelivery,
                            paymentOption = selectedPayment,
                            notes = note
                        )
                        onSubmitOffer(offerRecord)
                        IntentHelpers.openWhatsApp(context, fullMessage)
                        onDismiss()
                    }
                    .padding(vertical = 14.dp)
                    .testTag("submit_whatsapp_offer_btn"),
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
                        text = "SEND OFFER ON WHATSAPP",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Black,
                        color = Color.Black
                    )
                }
            }
        }
    }
}
