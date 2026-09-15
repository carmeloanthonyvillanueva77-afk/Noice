package com.example.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.HabitCategory
import com.example.ui.theme.CrispWhite
import com.example.ui.theme.ForestDeepEmerald
import com.example.ui.theme.ForestPastelSage
import com.example.ui.theme.SlateBorder
import com.example.ui.theme.SlateLight

@Composable
fun CategoryFilterRow(
    selectedCategory: String,
    onCategorySelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val categories = listOf("All") + HabitCategory.entries.map { it.displayName }

    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .testTag("category_filter_row"),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 2.dp)
    ) {
        items(categories) { category ->
            val isSelected = selectedCategory.equals(category, ignoreCase = true)
            val emoji = if (category == "All") "🌿" else HabitCategory.fromString(category).defaultEmoji

            FilterChip(
                selected = isSelected,
                onClick = { onCategorySelected(category) },
                label = {
                    Text(
                        text = "$emoji $category",
                        fontSize = 13.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                },
                shape = RoundedCornerShape(14.dp),
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = ForestDeepEmerald,
                    selectedLabelColor = CrispWhite,
                    containerColor = CrispWhite,
                    labelColor = SlateLight
                ),
                border = if (!isSelected) {
                    FilterChipDefaults.filterChipBorder(
                        enabled = true,
                        selected = false,
                        borderColor = SlateBorder
                    )
                } else null,
                modifier = Modifier
                    .height(38.dp)
                    .testTag("category_chip_$category")
            )
        }
    }
}
