package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsBike
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.outlined.DirectionsBike
import androidx.compose.material.icons.outlined.LocalOffer
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.Bike
import com.example.ui.MainViewModel
import com.example.ui.ScreenTab
import com.example.ui.components.FloatingWhatsAppButton
import com.example.ui.components.NegotiationOfferSheet
import com.example.ui.components.StoreHeaderBar
import com.example.ui.components.TopAnnouncementBanner
import com.example.ui.screens.BikeDetailScreen
import com.example.ui.screens.CatalogScreen
import com.example.ui.screens.NegotiateScreen
import com.example.ui.screens.StoreLocationScreen
import com.example.ui.screens.WishlistScreen
import com.example.ui.theme.DarkNavyBackground
import com.example.ui.theme.DarkSurfaceBorder
import com.example.ui.theme.DarkSurfaceCard
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonPink
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.util.IntentHelpers

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                TrekApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun TrekApp(viewModel: MainViewModel) {
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    val currentTab by viewModel.currentTab.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val filteredBikes by viewModel.filteredBikes.collectAsState()
    val favoriteBikes by viewModel.favoriteBikes.collectAsState()
    val favoriteIds by viewModel.favoriteIds.collectAsState()
    val recentOffers by viewModel.recentOffers.collectAsState()

    val selectedBikeForDetail by viewModel.selectedBikeForDetail.collectAsState()
    val selectedBikeForOffer by viewModel.selectedBikeForOffer.collectAsState()
    val userFeedbackMessage by viewModel.userFeedbackMessage.collectAsState()

    LaunchedEffect(userFeedbackMessage) {
        userFeedbackMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearFeedbackMessage()
        }
    }

    // Handle back button when on sub-tab or detail screen
    BackHandler(enabled = selectedBikeForDetail != null || currentTab != ScreenTab.CATALOG) {
        if (selectedBikeForDetail != null) {
            viewModel.closeBikeDetail()
        } else {
            viewModel.selectTab(ScreenTab.CATALOG)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets.safeDrawing,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            if (selectedBikeForDetail == null) {
                Column(modifier = Modifier.statusBarsPadding()) {
                    TopAnnouncementBanner(
                        onClick = { IntentHelpers.openWhatsApp(context) }
                    )
                    StoreHeaderBar(
                        onLocationClick = { viewModel.selectTab(ScreenTab.STORE) },
                        onCallClick = { IntentHelpers.openDialer(context) },
                        onWhatsAppClick = { IntentHelpers.openWhatsApp(context) },
                        savedCount = favoriteIds.size,
                        onWishlistClick = { viewModel.selectTab(ScreenTab.SAVED) }
                    )
                }
            }
        },
        bottomBar = {
            if (selectedBikeForDetail == null) {
                NavigationBar(
                    modifier = Modifier
                        .navigationBarsPadding()
                        .background(DarkNavyBackground)
                        .border(1.dp, Color(0x2200E5FF)),
                    containerColor = DarkNavyBackground,
                    contentColor = TextPrimary
                ) {
                    NavigationBarItem(
                        selected = currentTab == ScreenTab.CATALOG,
                        onClick = { viewModel.selectTab(ScreenTab.CATALOG) },
                        icon = {
                            Icon(
                                imageVector = if (currentTab == ScreenTab.CATALOG) Icons.Filled.DirectionsBike else Icons.Outlined.DirectionsBike,
                                contentDescription = "Bikes"
                            )
                        },
                        label = {
                            Text(
                                text = "BIKES",
                                fontWeight = if (currentTab == ScreenTab.CATALOG) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 11.sp
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.Black,
                            selectedTextColor = NeonCyan,
                            indicatorColor = NeonCyan,
                            unselectedIconColor = TextSecondary,
                            unselectedTextColor = TextMuted
                        ),
                        modifier = Modifier.testTag("tab_bikes")
                    )

                    NavigationBarItem(
                        selected = currentTab == ScreenTab.NEGOTIATE,
                        onClick = { viewModel.selectTab(ScreenTab.NEGOTIATE) },
                        icon = {
                            Icon(
                                imageVector = if (currentTab == ScreenTab.NEGOTIATE) Icons.Filled.LocalOffer else Icons.Outlined.LocalOffer,
                                contentDescription = "Negotiate"
                            )
                        },
                        label = {
                            Text(
                                text = "NEGOTIATE",
                                fontWeight = if (currentTab == ScreenTab.NEGOTIATE) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 11.sp
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.Black,
                            selectedTextColor = NeonPink,
                            indicatorColor = NeonPink,
                            unselectedIconColor = TextSecondary,
                            unselectedTextColor = TextMuted
                        ),
                        modifier = Modifier.testTag("tab_negotiate")
                    )

                    NavigationBarItem(
                        selected = currentTab == ScreenTab.SAVED,
                        onClick = { viewModel.selectTab(ScreenTab.SAVED) },
                        icon = {
                            BadgedBox(
                                badge = {
                                    if (favoriteIds.isNotEmpty()) {
                                        Badge(
                                            containerColor = NeonPink,
                                            contentColor = Color.White
                                        ) {
                                            Text("${favoriteIds.size}")
                                        }
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = if (currentTab == ScreenTab.SAVED) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                                    contentDescription = "Saved"
                                )
                            }
                        },
                        label = {
                            Text(
                                text = "SAVED",
                                fontWeight = if (currentTab == ScreenTab.SAVED) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 11.sp
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.Black,
                            selectedTextColor = NeonCyan,
                            indicatorColor = NeonCyan,
                            unselectedIconColor = TextSecondary,
                            unselectedTextColor = TextMuted
                        ),
                        modifier = Modifier.testTag("tab_saved")
                    )

                    NavigationBarItem(
                        selected = currentTab == ScreenTab.STORE,
                        onClick = { viewModel.selectTab(ScreenTab.STORE) },
                        icon = {
                            Icon(
                                imageVector = if (currentTab == ScreenTab.STORE) Icons.Filled.LocationOn else Icons.Outlined.LocationOn,
                                contentDescription = "Store"
                            )
                        },
                        label = {
                            Text(
                                text = "STORE",
                                fontWeight = if (currentTab == ScreenTab.STORE) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 11.sp
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.Black,
                            selectedTextColor = NeonCyan,
                            indicatorColor = NeonCyan,
                            unselectedIconColor = TextSecondary,
                            unselectedTextColor = TextMuted
                        ),
                        modifier = Modifier.testTag("tab_store")
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Main content depending on current tab
            when (currentTab) {
                ScreenTab.CATALOG -> {
                    CatalogScreen(
                        bikes = filteredBikes,
                        favoriteIds = favoriteIds,
                        searchQuery = searchQuery,
                        selectedCategory = selectedCategory,
                        onSearchChanged = viewModel::onSearchQueryChanged,
                        onCategorySelected = viewModel::onCategorySelected,
                        onBikeClick = viewModel::openBikeDetail,
                        onFavoriteToggle = viewModel::toggleFavorite,
                        onNegotiateClick = viewModel::openOfferDialog,
                        onNavigateToStore = { viewModel.selectTab(ScreenTab.STORE) }
                    )
                }
                ScreenTab.NEGOTIATE -> {
                    NegotiateScreen(
                        allBikes = viewModel.allBikes,
                        recentOffers = recentOffers,
                        onSubmitOffer = viewModel::recordSubmittedOffer
                    )
                }
                ScreenTab.SAVED -> {
                    WishlistScreen(
                        favoriteBikes = favoriteBikes,
                        onBikeClick = viewModel::openBikeDetail,
                        onRemoveFavorite = viewModel::toggleFavorite,
                        onNegotiateClick = viewModel::openOfferDialog,
                        onExploreCatalog = { viewModel.selectTab(ScreenTab.CATALOG) }
                    )
                }
                ScreenTab.STORE -> {
                    StoreLocationScreen()
                }
            }

            // Floating WhatsApp Quick Action Button
            AnimatedVisibility(
                visible = selectedBikeForDetail == null && currentTab != ScreenTab.NEGOTIATE,
                enter = fadeIn() + slideInVertically { it / 2 },
                exit = fadeOut() + slideOutVertically { it / 2 },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            ) {
                FloatingWhatsAppButton(
                    onClick = { IntentHelpers.openWhatsApp(context) }
                )
            }

            // Bike Detail Screen Overlay
            selectedBikeForDetail?.let { bike ->
                BikeDetailScreen(
                    bike = bike,
                    isFavorite = favoriteIds.contains(bike.id),
                    onBack = viewModel::closeBikeDetail,
                    onFavoriteToggle = { viewModel.toggleFavorite(bike) },
                    onOpenOfferDialog = { viewModel.openOfferDialog(bike) }
                )
            }

            // Offer Sheet Dialog
            selectedBikeForOffer?.let { bike ->
                NegotiationOfferSheet(
                    bike = bike,
                    onDismiss = viewModel::closeOfferDialog,
                    onSubmitOffer = viewModel::recordSubmittedOffer
                )
            }
        }
    }
}
