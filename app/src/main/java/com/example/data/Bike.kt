package com.example.data

enum class BikeCategory(val label: String) {
    ALL("All Bikes (100+)"),
    ADULT_MTB("Adult MTB & XC"),
    HARDTAIL("Hardtail 29\""),
    DIRT_JUMP("Dirt Jump & Street"),
    KIDS("Kids Bikes"),
    DEALS("Under KSh 5,000")
}

data class Bike(
    val id: String,
    val name: String,
    val brand: String,
    val category: BikeCategory,
    val categoryLabel: String,
    val priceKSh: Int,
    val minNegotiableKSh: Int,
    val wheelSize: String,
    val gears: String,
    val frame: String,
    val suspension: String,
    val brakes: String,
    val condition: String,
    val imageUrl: String,
    val localDrawableRes: Int? = null,
    val description: String,
    val featured: Boolean = false,
    val inStock: Boolean = true
) {
    val displayPrice: String get() = "KSh %,d".format(priceKSh)
    val displayMinNegotiable: String get() = "KSh %,d".format(minNegotiableKSh)
}

data class NegotiationOffer(
    val id: String,
    val customerName: String,
    val customerPhone: String,
    val bikeId: String,
    val bikeName: String,
    val listedPriceKSh: Int,
    val offerPriceKSh: Int,
    val deliveryOption: String,
    val paymentOption: String,
    val notes: String,
    val timestamp: Long = System.currentTimeMillis()
)
