package com.turtlpass.module.installedapp.usecase

import android.content.pm.ApplicationInfo
import app.cash.turbine.test
import com.turtlpass.common.domain.Result
import com.turtlpass.module.accessibility.usecase.RetrieveInstalledAppUseCase
import com.turtlpass.module.installedapp.model.InstalledApp
import com.turtlpass.rule.StandardCoroutineRule
import com.turtlpass.rule.runTest
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class InstalledAppsUseCaseTest {

    @get:Rule var standardCoroutineRule = StandardCoroutineRule()

    @Test
    fun `Given list of device apps When invoke the use case Then return Success with list of apps`() {
        standardCoroutineRule.runTest { testDispatcher ->
            val mockDeviceAppsRepository = mockk<DeviceAppsRepository>()
            val mockRetrieveInstalledAppUseCase = mockk<RetrieveInstalledAppUseCase>()
            val installedAppsUseCase = InstalledAppsUseCase(
                testDispatcher,
                mockDeviceAppsRepository,
                mockRetrieveInstalledAppUseCase
            )

            val appInfo1 = mockk<ApplicationInfo>()
            val appInfo2 = mockk<ApplicationInfo>()
            val appList = listOf(appInfo1, appInfo2)

            val installedApp1 = InstalledApp("app1", "com.example.app1")
            val installedApp2 = InstalledApp("app2", "com.example.app2")

            every { mockDeviceAppsRepository.deviceInstalledApplications() } returns appList
            every { mockRetrieveInstalledAppUseCase(appInfo1) } returns installedApp1
            every { mockRetrieveInstalledAppUseCase(appInfo2) } returns installedApp2

            installedAppsUseCase().test {
                assertEquals(Result.Loading, awaitItem())
                assertEquals(Result.Success(listOf(installedApp1, installedApp2)), awaitItem())
                awaitComplete()
            }
        }
    }

    @Test
    fun `Given unsorted apps When invoke Then return sorted apps`() {
        standardCoroutineRule.runTest { testDispatcher ->
            val mockDeviceAppsRepository = mockk<DeviceAppsRepository>()
            val mockRetrieveInstalledAppUseCase = mockk<RetrieveInstalledAppUseCase>()
            val installedAppsUseCase = InstalledAppsUseCase(
                testDispatcher,
                mockDeviceAppsRepository,
                mockRetrieveInstalledAppUseCase
            )

            val appInfo1 = mockk<ApplicationInfo>()
            val appInfo2 = mockk<ApplicationInfo>()
            val appList = listOf(appInfo1, appInfo2)

            val installedApp1 = InstalledApp("App B", "com.example.appB")
            val installedApp2 = InstalledApp("App A", "com.example.appA")

            every { mockDeviceAppsRepository.deviceInstalledApplications() } returns appList
            every { mockRetrieveInstalledAppUseCase(appInfo1) } returns installedApp1
            every { mockRetrieveInstalledAppUseCase(appInfo2) } returns installedApp2

            installedAppsUseCase().test {
                assertEquals(Result.Loading, awaitItem())
                assertEquals(Result.Success(listOf(installedApp2, installedApp1)), awaitItem())
                awaitComplete()
            }
        }
    }

    @Test
    fun `Given empty list of device apps When invoke the use case Then return error`() {
        standardCoroutineRule.runTest { testDispatcher ->
            val mockDeviceAppsRepository = mockk<DeviceAppsRepository>()
            val mockRetrieveInstalledAppUseCase = mockk<RetrieveInstalledAppUseCase>()
            val installedAppsUseCase = InstalledAppsUseCase(
                testDispatcher,
                mockDeviceAppsRepository,
                mockRetrieveInstalledAppUseCase
            )

            every { mockDeviceAppsRepository.deviceInstalledApplications() } returns emptyList()

            installedAppsUseCase().test {
                assertEquals(Result.Loading, awaitItem())
                assertEquals(Result.Error(), awaitItem())
                awaitComplete()
            }
        }
    }

    @Test
    fun `Given exception When invoke the use case Then return error with the exception`() {
        standardCoroutineRule.runTest { testDispatcher ->
            val mockDeviceAppsRepository = mockk<DeviceAppsRepository>()
            val mockRetrieveInstalledAppUseCase = mockk<RetrieveInstalledAppUseCase>()
            val installedAppsUseCase = InstalledAppsUseCase(
                testDispatcher,
                mockDeviceAppsRepository,
                mockRetrieveInstalledAppUseCase
            )

            val exception = Exception("error")
            every { mockDeviceAppsRepository.deviceInstalledApplications() } throws exception

            installedAppsUseCase().test {
                assertEquals(Result.Loading, awaitItem())
                assertEquals(Result.Error(exception), awaitItem())
                awaitComplete()
            }
        }
    }

    @Test
    fun `Given no app retrieved When invoke the use case Then return error`() {
        standardCoroutineRule.runTest { testDispatcher ->
            val mockDeviceAppsRepository = mockk<DeviceAppsRepository>()
            val mockRetrieveInstalledAppUseCase = mockk<RetrieveInstalledAppUseCase>()
            val installedAppsUseCase = InstalledAppsUseCase(
                testDispatcher,
                mockDeviceAppsRepository,
                mockRetrieveInstalledAppUseCase
            )

            val appInfo1 = mockk<ApplicationInfo>()
            val appInfo2 = mockk<ApplicationInfo>()
            val appList = listOf(appInfo1, appInfo2)

            every { mockDeviceAppsRepository.deviceInstalledApplications() } returns appList
            every { mockRetrieveInstalledAppUseCase(appInfo1) } returns null
            every { mockRetrieveInstalledAppUseCase(appInfo2) } returns null

            installedAppsUseCase().test {
                assertEquals(Result.Loading, awaitItem())
                assertEquals(Result.Error(), awaitItem())
                awaitComplete()
            }
        }
    }
}
