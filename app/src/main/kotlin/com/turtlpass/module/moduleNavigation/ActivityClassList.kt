package com.turtlpass.module.moduleNavigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.text.ExperimentalTextApi
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.turtlpass.di.moduleNavigation.ActivityClass
import com.turtlpass.di.moduleNavigation.ActivityLabel
import com.turtlpass.module.main.MainActivity
import com.turtlpass.module.selection.SelectionActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

fun buildActivityClassList(): List<ActivityClass> {
    val list = ArrayList<ActivityClass>()
    ActivityLabel.entries.forEach {
        list.add(ActivityClass(it, getClassName(it)))
    }
    return list
}

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterialApi::class,
    ExperimentalPermissionsApi::class, ExperimentalTextApi::class,
    ExperimentalCoroutinesApi::class, FlowPreview::class
)
private fun getClassName(activityLabel: ActivityLabel) = when (activityLabel) {
    ActivityLabel.MainActivity -> MainActivity::class.java.name
    ActivityLabel.SelectionActivity -> SelectionActivity::class.java.name
}
