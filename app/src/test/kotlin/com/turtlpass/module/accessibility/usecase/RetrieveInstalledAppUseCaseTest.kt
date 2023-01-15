package com.turtlpass.module.accessibility.usecase

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import com.google.common.truth.Truth.assertThat
import com.turtlpass.module.chooser.usecase.ExtractTopLevelDomainUseCase
import com.turtlpass.module.installedapp.model.InstalledApp
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test

class RetrieveInstalledAppUseCaseTest {

    private val packageManager = mockk<PackageManager>()
    private val extractTopLevelDomainUseCase = mockk<ExtractTopLevelDomainUseCase>()
    private val retrieveInstalledAppUseCase by lazy {
        RetrieveInstalledAppUseCase(
            packageManager = packageManager,
            extractTopLevelDomainUseCase = extractTopLevelDomainUseCase
        )
    }

    @Test
    fun `Given an app info with flags 0 When retrieveInstalledApp is called Then the correct InstalledApp is returned`() {
        runBlocking {
            // Set up test data
            val packageName = "com.example.app"
            val appName = "Example App"
            val topLevelDomain = "example.com"
            val installedApp = InstalledApp(appName, packageName, topLevelDomain)

            // Set up mock behaviors
            val appInfo = mockk<ApplicationInfo>().apply {
                this.packageName = packageName
                this.flags = 0
            }
            every { packageManager.getApplicationLabel(appInfo) } returns appName
            every { extractTopLevelDomainUseCase(packageName) } returns topLevelDomain

            // Invoke the use case and assert on the result
            val result = retrieveInstalledAppUseCase(appInfo)
            assertThat(result).isEqualTo(installedApp)
        }
    }

    @Test
    fun `When we request a system app Then we get null`() {
        runBlocking {
            // Set up test data
            val packageName = "com.example.system.app"
            val appName = "Example System App"
            val topLevelDomain = "example.com"

            // Set up mock behaviors
            val appInfo = mockk<ApplicationInfo>().apply {
                this.packageName = packageName
                this.flags = ApplicationInfo.FLAG_SYSTEM
            }
            every { packageManager.getApplicationLabel(appInfo) } returns appName
            every { extractTopLevelDomainUseCase(packageName) } returns topLevelDomain

            // Invoke the use case and assert on the result
            val result = retrieveInstalledAppUseCase(appInfo)
            assertThat(result).isNull()
        }
    }
}
