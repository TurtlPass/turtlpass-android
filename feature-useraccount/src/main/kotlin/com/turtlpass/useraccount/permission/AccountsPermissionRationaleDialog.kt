package com.turtlpass.useraccount.permission

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.turtlpass.ui.button.SecondaryButton
import com.turtlpass.ui.theme.AppTheme.dimensions
import com.turtlpass.ui.theme.AppTheme.typography
import com.turtlpass.useraccount.R

@Composable
fun AccountsPermissionRationaleDialog(onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        shape = RoundedCornerShape(dimensions.x32),
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(R.string.feature_useraccount_rationale_title),
                style = typography.h3.copy(
                    fontWeight = FontWeight.Normal,
                )
            )
        },
        text = {
            Text(
                text = stringResource(R.string.feature_useraccount_rationale_body),
                style = typography.body
            )
        },
        confirmButton = {
            SecondaryButton(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(bottom = dimensions.x8, end = dimensions.x8),
                text = stringResource(R.string.feature_useraccount_rationale_button_ok),
                onClick = onConfirm,
            )
        },
    )
}
