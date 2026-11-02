package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.UserPreferences
import com.example.service.AiVideoEngine
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read app_name string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Manas-MatchCut", appName)
  }

  @Test
  fun `verify available AI video models list`() {
    val models = AiVideoEngine.AVAILABLE_MODELS
    assertTrue(models.isNotEmpty())
    val veoModel = models.find { it.id == "veo-2" }
    assertNotNull(veoModel)
    assertEquals("Google Veo 2", veoModel?.name)
  }

  @Test
  fun `verify daily free credits logic`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val prefs = UserPreferences(context)
    val initialCredits = prefs.remainingCredits.value
    assertTrue(initialCredits > 0)
  }
}
