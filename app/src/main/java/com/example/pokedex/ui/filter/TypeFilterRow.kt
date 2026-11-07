package com.example.pokedex.ui.filter

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun TypeFilterRow(
    types: List<String>,
    selectedType: String?,
    onTypeSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val displayTypes = listOf("All") + types.filter { !it.equals("All", ignoreCase = true) }

    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        items(displayTypes) { type ->
            val isSelected = if (type.equals("All", ignoreCase = true)) {
                selectedType.isNullOrEmpty()
            } else {
                type.equals(selectedType, ignoreCase = true)
            }

            val backgroundColor = if (isSelected) Color(0xFFDC0A2D) else Color(0xFFF2F2F2)
            val contentColor = if (isSelected) Color.White else Color.Black

            Surface(
                onClick = {
                    if (type.equals("All", ignoreCase = true)) {
                        onTypeSelected("")
                    } else {
                        onTypeSelected(type)
                    }
                },
                shape = RoundedCornerShape(20.dp),
                color = backgroundColor,
                contentColor = contentColor,
                modifier = Modifier.height(36.dp)
            ) {
                Box(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = type,
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }
    }
}