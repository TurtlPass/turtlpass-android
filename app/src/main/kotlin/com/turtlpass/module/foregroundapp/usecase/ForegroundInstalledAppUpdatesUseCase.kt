//package com.turtlpass.module.foregroundapp.usecase
//
//import com.turtlpass.accessibility.usecase.AppForegroundUpdatesUseCase
//import com.turtlpass.appmanager.usecase.GetApplicationInfoUseCase
//import com.turtlpass.module.foregroundapp.model.ForegroundApp
//import jakarta.inject.Inject
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.flow.mapNotNull
//
//class ForegroundInstalledAppUpdatesUseCase @Inject constructor(
//    private val appForegroundUpdatesUseCase: AppForegroundUpdatesUseCase,
//    private val getApplicationInfoUseCase: GetApplicationInfoUseCase
//) {
//    operator fun invoke(): Flow<ForegroundApp> {
//        return appForegroundUpdatesUseCase()
//            .mapNotNull { event ->
//                val installed = getApplicationInfoUseCase(event.packageName) ?: return@mapNotNull null
//                ForegroundApp(installed, event.timestamp)
//            }
//    }
//
////    operator fun invoke(): Flow<InstalledApp> {
////        return appForegroundUpdatesUseCase()
////            .mapNotNull { event ->
////                getApplicationInfoUseCase(event.packageName)
////            }
////    }
//}
