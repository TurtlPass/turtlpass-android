package com.turtlpass.usb.intent

import android.content.Intent
import android.hardware.usb.UsbManager
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.text.ExperimentalTextApi
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.turtlpass.di.moduleNavigation.ActivityLabel
import com.turtlpass.di.moduleNavigation.ModuleNavigation
import dagger.hilt.android.AndroidEntryPoint
import jakarta.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@OptIn(
    FlowPreview::class,
    ExperimentalAnimationApi::class,
    ExperimentalMaterialApi::class,
    ExperimentalPermissionsApi::class,
    ExperimentalTextApi::class,
    ExperimentalCoroutinesApi::class,
)
@AndroidEntryPoint
class UsbIntentActivity : AppCompatActivity() {

    @Inject
    lateinit var moduleNavigation: ModuleNavigation

    private val viewModel by viewModels<UsbIntentViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (intent.action != UsbManager.ACTION_USB_DEVICE_ATTACHED) {
            finish()
            return
        }

        when (viewModel.decideUsbIntent()) {
            is UsbIntentDecision.LaunchSelection -> {
                moduleNavigation.buildIntent(this, ActivityLabel.SelectionActivity)?.apply {
                    addFlags(
                        Intent.FLAG_ACTIVITY_NEW_TASK or
                                Intent.FLAG_ACTIVITY_CLEAR_TASK or
                                Intent.FLAG_ACTIVITY_NO_ANIMATION
                    )
                    putExtra(EXTRA_IS_USB_INTENT, true)
                }.also { intent ->
                    startActivity(intent)
                }
            }

            is UsbIntentDecision.LaunchMain -> {
                moduleNavigation.buildIntent(this, ActivityLabel.MainActivity)?.apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }.also { intent ->
                    startActivity(intent)
                }
            }

            is UsbIntentDecision.Finish -> {}
        }
        finish()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        finish()
    }

    companion object {
        const val EXTRA_IS_USB_INTENT = "usb_intent"
    }
}
