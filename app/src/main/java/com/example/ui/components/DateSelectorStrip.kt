package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Today
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.DayItemUi
import com.example.ui.theme.CrispWhite
import com.example.ui.theme.ForestDeepEmerald
import com.example.ui.theme.ForestPastelSage
import com.example.ui.theme.ForestSoftSage
import com.example.ui.theme.SlateBorder
import com.example.ui.theme.SlateDark
import com.example.ui.theme.SlateLight
import java.time.LocalDate

@Composable
fun DateSelectorStrip(
    days: List<DayItemUi>,
    selectedDate: LocalDate,
    onSelectDate: (LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {
    val isTodaySelected = selectedDate == LocalDate.now()

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "DAILY FLOW",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                color = SlateLight
            )

            if (!isTodaySelected) {
                AssistChip(
                    onClick = { onSelectDate(LocalDate.now()) },
                    label = { Text("Jump to Today", fontSize = 12.sp, fontWeight = FontWeight.SemiBold) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Today,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = ForestDeepEmerald
                        )
                    },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = ForestPastelSage,
                        labelColor = ForestDeepEmerald
                    ),
                    border = null,
                    modifier = Modifier.height(32.dp).testTag("jump_to_today_button")
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("date_selector_strip"),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 2.dp)
        ) {
            items(days) { day ->
                val isSelected = day.isSelected
                val isToday = day.isToday

                Surface(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .clickable { onSelectDate(day.date) }
                        .testTag("date_item_${day.dateString}"),
                    shape = RoundedCornerShape(16.dp),
                    color = when {
                        isSelected -> ForestDeepEmerald
                        isToday -> ForestPastelSage.copy(alpha = 0.6f)
                        else -> MaterialTheme.colorScheme.surface
                    },
                    border = if (!isSelected) {
                        androidx.compose.foundation.BorderStroke(1.dp, SlateBorder)
                    } else null
                ) {
                    Column(
                        modifier = Modifier
                            .width(52.dp)
                            .padding(vertical = 12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = day.dayName.uppercase(),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = when {
                                isSelected -> ForestSoftSage
                                isToday -> ForestDeepEmerald
                                else -> SlateLight
                            }
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = day.dayNumber,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = when {
                                isSelected -> CrispWhite
                                isToday -> ForestDeepEmerald
                                else -> SlateDark
                            }
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        // Dot indicator for today
                        if (isToday) {
                            Box(
                                modifier = Modifier
                                    .size(4.dp)
                                    .clip(CircleShape)
                                    .background(if (isSelected) ForestSoftSage else ForestDeepEmerald)
                            )
                        } else {
                            Spacer(modifier = Modifier.height(4.dp))
                        }
                    }
                }
            }
        }
    }
}
