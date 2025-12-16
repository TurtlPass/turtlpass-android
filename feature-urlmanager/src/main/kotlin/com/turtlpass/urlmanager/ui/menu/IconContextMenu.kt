package com.turtlpass.urlmanager.ui.menu

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.turtlpass.ui.icons.DeleteForever24Px
import com.turtlpass.ui.icons.MoreVert24Px
import com.turtlpass.ui.theme.AppTheme.colors
import com.turtlpass.ui.theme.AppTheme.typography
import com.turtlpass.urlmanager.R

@Composable
fun IconContextMenu(
    onDelete: () -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        IconButton(onClick = { expanded = true }) {
            Icon(
                imageVector = MoreVert24Px,
                contentDescription = "Menu",
                tint = colors.text.primary
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            shape = RoundedCornerShape(16.dp),
            containerColor = colors.default.background,
            tonalElevation = 0.dp,
            offset = DpOffset(x = (-8).dp, y = (-8).dp),
        ) {
            DropdownMenuItem(
                onClick = {
                    expanded = false
                    onDelete()
                },
                leadingIcon = {
                    Icon(
                        imageVector = DeleteForever24Px,
                        contentDescription = stringResource(R.string.feature_urlmanager_delete_all),
                        tint = colors.default.destructive,
                    )
                },
                text = {
                    Text(
                        modifier = Modifier.padding(end = 8.dp),
                        text = stringResource(R.string.feature_urlmanager_delete_all),
                        style = typography.h3.copy(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = colors.default.destructive,
                        ),
                    )
                }
            )
        }
    }
}
