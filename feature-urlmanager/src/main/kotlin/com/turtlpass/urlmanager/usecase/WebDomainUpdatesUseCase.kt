//package com.turtlpass.urlmanager.usecase
//
//import com.turtlpass.accessibility.bus.AccessibilityEventBus
//import com.turtlpass.appmanager.usecase.GetAppNameUseCase
//import com.turtlpass.model.WebsiteUi
//import com.turtlpass.model.event.ObservedAccessibilityEvent
//import com.turtlpass.urlmanager.extension.faviconUrl
//import com.turtlpass.urlmanager.extension.formatTime
//import kotlinx.coroutines.FlowPreview
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.flow.debounce
//import kotlinx.coroutines.flow.distinctUntilChanged
//import kotlinx.coroutines.flow.filterIsInstance
//import kotlinx.coroutines.flow.map
//import kotlinx.coroutines.flow.scan
//import java.time.format.DateTimeFormatter
//import javax.inject.Inject
//
//@OptIn(FlowPreview::class)
//class WebDomainUpdatesUseCase @Inject constructor(
//    private val bus: AccessibilityEventBus,
//    private val getAppNameUseCase: GetAppNameUseCase,
//) {
//    private val timeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")
//
//    operator fun invoke(maxEvents: Int = 20): Flow<List<WebsiteUi>> {
//        return bus.events
//            .filterIsInstance<ObservedAccessibilityEvent.UrlEvent>()
//            .debounce(150)
//            .distinctUntilChanged { old, new -> old.url == new.url }
//            .map { event ->
//                WebsiteUi(
//                    appName = getAppNameUseCase(event.packageName) ?: event.packageName,
//                    packageName = event.packageName,
//                    url = event.url,
//                    timestamp = event.timestamp,
//                    time = timeFormatter.formatTime(event.timestamp),
//                    faviconUrl = event.url.faviconUrl()
//                )
//            }
//            .scan(emptyList<WebsiteUi>()) { list, newEvent ->
//                (list + newEvent).takeLast(maxEvents) // append to list
//            }
//    }
//}