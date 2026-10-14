package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.BikeCategory
import com.example.data.BikeRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("TREK Athi River", appName)
  }

  @Test
  fun `verify catalog contains expected bikes and categories`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val repository = BikeRepository(context)
    val bikes = repository.getAllBikes()

    assertTrue("Catalog should contain at least 6 bikes", bikes.size >= 6)

    // Check sample bikes and prices requested by user
    val scottGambler = bikes.find { it.name.contains("SCOTT Gambler", ignoreCase = true) }
    assertNotNull("SCOTT Gambler should be in catalog", scottGambler)
    assertEquals(6500, scottGambler?.priceKSh)

    val trekSuper = bikes.find { it.name.contains("TREK Supercaliber", ignoreCase = true) }
    assertNotNull("TREK Supercaliber should be in catalog", trekSuper)
    assertEquals(6500, trekSuper?.priceKSh)

    val specialized = bikes.find { it.name.contains("Specialized XC", ignoreCase = true) }
    assertNotNull("Specialized XC should be in catalog", specialized)
    assertEquals(6800, specialized?.priceKSh)

    val borace = bikes.find { it.name.contains("Borace", ignoreCase = true) }
    assertNotNull("Borace should be in catalog", borace)
    assertEquals(7500, borace?.priceKSh)

    // Check filtering
    val kidsBikes = repository.filterBikes("", BikeCategory.KIDS)
    assertTrue("Should have kids bikes", kidsBikes.isNotEmpty())

    val searchResults = repository.filterBikes("Scott", BikeCategory.ALL)
    assertTrue("Should match Scott bikes", searchResults.all { it.name.contains("Scott", ignoreCase = true) || it.brand.equals("Scott", ignoreCase = true) })
  }
}
