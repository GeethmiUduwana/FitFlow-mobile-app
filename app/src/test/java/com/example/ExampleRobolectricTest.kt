package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.model.AlexRivera
import com.example.model.PriyaSingh
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("FitFlow", appName)
  }

  @Test
  fun `verify HCI personas initialized`() {
    assertNotNull(AlexRivera)
    assertEquals("Alex Rivera", AlexRivera.name)
    assertEquals("alex", AlexRivera.id)

    assertNotNull(PriyaSingh)
    assertEquals("Priya Singh", PriyaSingh.name)
    assertEquals("priya", PriyaSingh.id)
  }
}
