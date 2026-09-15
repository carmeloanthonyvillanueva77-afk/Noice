package com.example.ui.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CrispWhite
import com.example.ui.theme.ForestDeepEmerald
import com.example.ui.theme.ForestUltraLight
import com.example.ui.theme.SlateDark
import com.example.ui.theme.SlateLight

@Composable
fun ResetDayDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                imageVector = Icons.Default.RestartAlt,
                contentDescription = null,
                tint = ForestDeepEmerald
            )
        },
        title = {
            Text(
                text = "Clean Slate for Today?",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = SlateDark
            )
        },
        text = {
            Text(
                text = "This will uncheck all habits for the selected day so you can start fresh. Your streaks and historical data on other days will remain preserved.",
                fontSize = 14.sp,
                color = SlateLight
            )
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm()
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(containerColor = ForestDeepEmerald),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.testTag("confirm_reset_button")
            ) {
                Text("Reset Slate", color = CrispWhite, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Cancel", color = SlateLight)
            }
        },
        shape = RoundedCornerShape(20.dp),
        containerColor = CrispWhite
    )
}
