package com.example.data

import android.content.Context
import android.content.SharedPreferences
import com.example.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class BikeRepository(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("trek_athi_river_prefs", Context.MODE_PRIVATE)

    private val _favoriteIds = MutableStateFlow<Set<String>>(loadFavorites())
    val favoriteIds: StateFlow<Set<String>> = _favoriteIds.asStateFlow()

    private val _recentOffers = MutableStateFlow<List<NegotiationOffer>>(emptyList())
    val recentOffers: StateFlow<List<NegotiationOffer>> = _recentOffers.asStateFlow()

    private val catalog = listOf(
        Bike(
            id = "scott_gambler_29",
            name = "SCOTT Gambler Downhill",
            brand = "SCOTT",
            category = BikeCategory.ADULT_MTB,
            categoryLabel = "Adult MTB 29\" Full Suspension",
            priceKSh = 6500,
            minNegotiableKSh = 5400,
            wheelSize = "29\"",
            gears = "1x12 Shimano Zee / Deore",
            frame = "Alloy 6061 Custom Butted Hydroformed",
            suspension = "Fox 40 Dual Crown 200mm / Rear Vanilla Coil",
            brakes = "Shimano 4-Piston Hydraulic Disc",
            condition = "Workshop Tuned • Ready to Shred",
            imageUrl = "https://images.unsplash.com/photo-1541625602330-2277a4c46182?q=80&w=900",
            localDrawableRes = R.drawable.bike1,
            description = "High-performance downhill and trail beast built for rough Kenyan terrains, Ngong hills and Lukenya trails. Plush dual suspension and rock-solid stopping power.",
            featured = true,
            inStock = true
        ),
        Bike(
            id = "trek_supercaliber_98",
            name = "TREK Supercaliber 9.8",
            brand = "TREK",
            category = BikeCategory.ADULT_MTB,
            categoryLabel = "Adult MTB 29\" Carbon XC",
            priceKSh = 6500,
            minNegotiableKSh = 5400,
            wheelSize = "29\"",
            gears = "1x12 SRAM GX Eagle Rapid Shift",
            frame = "OCLV Mountain Carbon Lightweight",
            suspension = "IsoStrut Fox Performance 60mm & RockShox SID 100mm",
            brakes = "Shimano Deore XT Hydraulic Disc",
            condition = "Import Ex-UK • Grade A Super Clean",
            imageUrl = "https://images.unsplash.com/photo-1576435728678-68d0fbf94e91?q=80&w=900",
            localDrawableRes = R.drawable.bike2,
            description = "World Cup cross-country rocket. Unique integrated IsoStrut shock gives efficiency of a hardtail with the compliance of full suspension. Lightweight carbon build.",
            featured = true,
            inStock = true
        ),
        Bike(
            id = "scott_scale_tanwall",
            name = "SCOTT Scale Tanwall",
            brand = "SCOTT",
            category = BikeCategory.HARDTAIL,
            categoryLabel = "Adult MTB 29\" Hardtail",
            priceKSh = 7500,
            minNegotiableKSh = 6300,
            wheelSize = "29\"",
            gears = "1x11 Shimano Deore Shadow+",
            frame = "Scale Alloy 6061 Custom Butted",
            suspension = "RockShox Judy Silver TK Solo Air 100mm Lockout",
            brakes = "Shimano MT200 Hydraulic Disc",
            condition = "Mint Condition • Fresh Tanwall Maxxis Tyres",
            imageUrl = "https://images.unsplash.com/photo-1485965120184-e220f721d03e?q=80&w=900",
            localDrawableRes = R.drawable.bike3,
            description = "Clean modern trail geometry with striking retro tanwall tyres. Fast-rolling 29er wheels make everyday commuting and weekend off-roading effortless.",
            featured = true,
            inStock = true
        ),
        Bike(
            id = "specialized_xc_custom",
            name = "Specialized XC Custom",
            brand = "Specialized",
            category = BikeCategory.ADULT_MTB,
            categoryLabel = "Adult MTB 26\" Red Purple Custom",
            priceKSh = 6800,
            minNegotiableKSh = 5600,
            wheelSize = "26\"",
            gears = "3x8 Shimano Acera Rapidfire",
            frame = "A1 Premium Butted Aluminum",
            suspension = "SR Suntour XCT 100mm with Preload",
            brakes = "Promax Mechanical Disc F/R",
            condition = "Custom Paint & Tuned • Heavy Duty Rims",
            imageUrl = "https://images.unsplash.com/photo-1511994298241-608e28f14fde?q=80&w=900",
            localDrawableRes = R.drawable.bike4,
            description = "Agile 26-inch custom-built Specialized hardtail with distinctive iridescent red-purple finish. Highly responsive in urban streets and quick trail switchbacks.",
            featured = false,
            inStock = true
        ),
        Bike(
            id = "borace_dirt_jump_26",
            name = "Borace Dirt Jump 26",
            brand = "Borace",
            category = BikeCategory.DIRT_JUMP,
            categoryLabel = "Adult & Teen Street / Pumptrack",
            priceKSh = 7500,
            minNegotiableKSh = 6200,
            wheelSize = "26\"",
            gears = "Single Speed with Chain Tensioner",
            frame = "4130 Chromoly High Strength Frame",
            suspension = "Manitou Circus Expert 100mm DJ Fork",
            brakes = "Rear Hydraulic Disc (Gyro ready)",
            condition = "Stunt Tuned • Reinforced Double Wall Rims",
            imageUrl = "https://images.unsplash.com/photo-1532298229144-0ec0c57515c7?q=80&w=900",
            localDrawableRes = R.drawable.bike5,
            description = "Built for jumps, wheelies, bunny hops, and pumptracks. Ultra-stiff Chromoly construction with reinforced bottom bracket and low-profile saddle.",
            featured = false,
            inStock = true
        ),
        Bike(
            id = "trek_marlin_7",
            name = "TREK Marlin 7 Trail 29\"",
            brand = "TREK",
            category = BikeCategory.HARDTAIL,
            categoryLabel = "Adult MTB 29\" Hydraulic Disc",
            priceKSh = 8000,
            minNegotiableKSh = 6800,
            wheelSize = "29\"",
            gears = "1x10 Shimano Deore with Clutch",
            frame = "Alpha Silver Aluminum Internal Routing",
            suspension = "RockShox Judy Coil 100mm Lockout",
            brakes = "Tektro HD-M275 Hydraulic Disc",
            condition = "Excellent Condition • Athi River Verified",
            imageUrl = "https://images.unsplash.com/photo-1507035895480-2b3156c31fc8?q=80&w=900",
            description = "Kenya's favorite all-rounder mountain bike. Smooth 1x drivetrain eliminates chain drops while 29-inch wheels conquer rough roads and tarmac with speed.",
            featured = true,
            inStock = true
        ),
        Bike(
            id = "giant_talon_29",
            name = "Giant Talon 2 Trail 29er",
            brand = "Giant",
            category = BikeCategory.HARDTAIL,
            categoryLabel = "Adult MTB 29\" Trail Hardtail",
            priceKSh = 7800,
            minNegotiableKSh = 6600,
            wheelSize = "29\"",
            gears = "2x9 Shimano Alivio / Altus",
            frame = "ALUXX-Grade Aluminum",
            suspension = "SR Suntour XCM30 100mm Hydraulic Lockout",
            brakes = "Tektro TKD 143 Hydraulic Disc",
            condition = "Direct Ex-Japan Import • Fully Serviced",
            imageUrl = "https://images.unsplash.com/photo-1571068316344-75bc76f77890?q=80&w=900",
            description = "Sturdy cross-country geometry designed for comfort and rugged endurance. Equipped with dual chainrings for conquering steep uphill gradients easily.",
            featured = false,
            inStock = true
        ),
        Bike(
            id = "bmx_freestyle_street",
            name = "Cult Gateway BMX 20\"",
            brand = "BMX",
            category = BikeCategory.DIRT_JUMP,
            categoryLabel = "Street & Freestyle Stunt Bike",
            priceKSh = 5500,
            minNegotiableKSh = 4800,
            wheelSize = "20\"",
            gears = "25T/9T Micro-Drive Cassette Hub",
            frame = "Chromoly Front Triangle",
            suspension = "Rigid Tapered Chromoly Fork",
            brakes = "U-Brake with Clear Pads + Pegs Included",
            condition = "Ready to Ride • 4 Grind Pegs Included",
            imageUrl = "https://images.unsplash.com/photo-1517649763962-0c623066013b?q=80&w=900",
            description = "Legendary street BMX with responsive geometry. Great for skateparks, street tricks, and durable youth riding. Includes 4 steel stunt pegs.",
            featured = false,
            inStock = true
        ),
        Bike(
            id = "kids_bikes_20_24",
            name = "TREK Precaliber 20\"/24\"",
            brand = "TREK",
            category = BikeCategory.KIDS,
            categoryLabel = "Kids & Youth Age 6-12 Years",
            priceKSh = 4500,
            minNegotiableKSh = 3800,
            wheelSize = "20\" - 24\"",
            gears = "7-Speed Shimano Twist Shift",
            frame = "Dialed Aluminum Lightweight",
            suspension = "Front Dialed Suspension Fork 35mm",
            brakes = "Linear Pull V-Brakes with Kids Reach Levers",
            condition = "Like New • Lightweight & Safe",
            imageUrl = "https://images.unsplash.com/photo-1484156818044-c040038ed88d?q=80&w=900",
            description = "Engineered with lightweight aluminum so kids can handle it easily without fatigue. Easy-twist Shimano gears help children learn gear shifting smoothly.",
            featured = true,
            inStock = true
        ),
        Bike(
            id = "kids_bikes_16_hero",
            name = "Kids Bike 16\" with Training Wheels",
            brand = "RoyalBaby",
            category = BikeCategory.KIDS,
            categoryLabel = "Kids Age 3-6 Years Beginner",
            priceKSh = 3500,
            minNegotiableKSh = 2900,
            wheelSize = "16\"",
            gears = "Single Speed with Full Chain Guard",
            frame = "Reinforced Steel Low Standover",
            suspension = "Rigid Fork with Padded Handlebars",
            brakes = "Front Caliper + Rear Coaster Brake",
            condition = "Brand New / Clean Refurbished • Stabilizers Included",
            imageUrl = "https://images.unsplash.com/photo-1502744688674-c619d1586c9e?q=80&w=900",
            description = "Perfect starter bicycle for young riders. Includes removable heavy-duty training wheels, handlebar safety bell, water bottle holder, and mudguards.",
            featured = false,
            inStock = true
        )
    )

    fun getAllBikes(): List<Bike> = catalog

    fun getBikeById(id: String): Bike? = catalog.find { it.id == id }

    fun filterBikes(query: String, category: BikeCategory): List<Bike> {
        return catalog.filter { bike ->
            val matchesCategory = when (category) {
                BikeCategory.ALL -> true
                BikeCategory.DEALS -> bike.priceKSh <= 5000
                else -> bike.category == category
            }
            val matchesQuery = query.isBlank() ||
                bike.name.contains(query, ignoreCase = true) ||
                bike.brand.contains(query, ignoreCase = true) ||
                bike.categoryLabel.contains(query, ignoreCase = true) ||
                bike.wheelSize.contains(query, ignoreCase = true) ||
                bike.description.contains(query, ignoreCase = true)

            matchesCategory && matchesQuery
        }
    }

    fun toggleFavorite(bikeId: String) {
        val current = _favoriteIds.value.toMutableSet()
        if (current.contains(bikeId)) {
            current.remove(bikeId)
        } else {
            current.add(bikeId)
        }
        _favoriteIds.value = current
        saveFavorites(current)
    }

    fun isFavorite(bikeId: String): Boolean = _favoriteIds.value.contains(bikeId)

    fun recordOffer(offer: NegotiationOffer) {
        val current = _recentOffers.value.toMutableList()
        current.add(0, offer)
        _recentOffers.value = current.take(20)
    }

    private fun loadFavorites(): Set<String> {
        return prefs.getStringSet("favorite_bike_ids", emptySet()) ?: emptySet()
    }

    private fun saveFavorites(ids: Set<String>) {
        prefs.edit().putStringSet("favorite_bike_ids", ids).apply()
    }
}
