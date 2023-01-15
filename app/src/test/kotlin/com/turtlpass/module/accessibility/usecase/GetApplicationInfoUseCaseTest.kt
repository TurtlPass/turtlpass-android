package com.turtlpass.module.accessibility.usecase

import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import com.google.common.truth.Truth.assertThat
import com.turtlpass.common.extension.getPackageInfoCompat
import com.turtlpass.module.chooser.usecase.ExtractTopLevelDomainUseCase
import com.turtlpass.module.installedapp.model.InstalledApp
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

class GetApplicationInfoUseCaseTest {

    private val packageManager = mockk<PackageManager>()
    private val extractTopLevelDomainUseCase = mockk<ExtractTopLevelDomainUseCase>()
    private val getApplicationInfoUseCase by lazy {
        GetApplicationInfoUseCase(
            packageManager = packageManager,
            extractTopLevelDomainUseCase = extractTopLevelDomainUseCase
        )
    }

    @Test
    fun `Given a package name When getApplicationInfo is called Then the correct InstalledApp is returned`() {
        runBlocking {
            // Set up test data
            val packageName = "com.example.app"
            val appName = "Example App"
            val topLevelDomain = "example.com"
            val appInfo = mockk<ApplicationInfo>().apply {
                this.packageName = packageName
            }
            val packageInfo = mockk<PackageInfo>().apply {
                this.applicationInfo = appInfo
            }
            val installedApp = InstalledApp(appName, packageName, topLevelDomain)

            // Set up mock behaviors
            every { packageManager.getApplicationLabel(appInfo) } returns appName
            every {
                packageManager.getPackageInfoCompat(
                    packageName,
                    PackageManager.GET_META_DATA
                )
            } returns packageInfo
            every { extractTopLevelDomainUseCase(packageName) } returns topLevelDomain

            // Invoke the use case and assert on the result
            val result = getApplicationInfoUseCase(packageName)
            assertThat(result).isEqualTo(installedApp)
        }
    }

    @Test
    fun `Given a package name of a system app When getApplicationInfo is called Then null is returned`() {
        runBlocking {
            // Set up test data
            val packageName = "com.example.app"
            val appName = "Example App"
            val topLevelDomain = "example.com"
            val appInfo = mockk<ApplicationInfo>().apply {
                this.packageName = packageName
                this.flags = ApplicationInfo.FLAG_SYSTEM
            }
            val packageInfo = mockk<PackageInfo>().apply {
                this.applicationInfo = appInfo
            }

            // Set up mock behaviors
            every { packageManager.getApplicationLabel(appInfo) } returns appName
            every {
                packageManager.getPackageInfoCompat(
                    packageName,
                    PackageManager.GET_META_DATA
                )
            } returns packageInfo
            every { extractTopLevelDomainUseCase(packageName) } returns topLevelDomain

            // Invoke the use case and assert on the result
            val result = getApplicationInfoUseCase(packageName)
            assertThat(result).isNull()
        }
    }

    @Test
    fun `Given a package name that throws an exception When getApplicationInfo is called Then null is returned`() {
        runBlocking {
            // Set up test data
            val packageName = "com.example.app"
            val appName = "Example App"
            val topLevelDomain = "example.com"
            val appInfo = mockk<ApplicationInfo>().apply {
                this.packageName = packageName
            }

            // Set up mock behaviors
            every { packageManager.getApplicationLabel(appInfo) } returns appName
            every {
                packageManager.getPackageInfoCompat(
                    packageName,
                    PackageManager.GET_META_DATA
                )
            } throws Exception()
            every { extractTopLevelDomainUseCase(packageName) } returns topLevelDomain

            // Invoke the use case and assert on the result
            val result = getApplicationInfoUseCase(packageName)
            assertThat(result).isNull()
        }
    }
}
