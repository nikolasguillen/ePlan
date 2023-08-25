package com.example.eplan.presentation.ui.components.uiElements

import androidx.compose.animation.Crossfade
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.eplan.R
import com.example.eplan.presentation.util.spacing
import kotlinx.datetime.Month
import java.time.format.TextStyle

@Composable
fun MonthPicker(
    startMonth: Month,
    startYear: Int,
    onConfirm: (month: Int, year: Int) -> Unit,
    onDismiss: () -> Unit
) {

    val months = remember {
        listOf(
            Month.JANUARY,
            Month.FEBRUARY,
            Month.MARCH,
            Month.APRIL,
            Month.MAY,
            Month.JUNE,
            Month.JULY,
            Month.AUGUST,
            Month.SEPTEMBER,
            Month.OCTOBER,
            Month.NOVEMBER,
            Month.DECEMBER
        )
    }

    val years = remember {
        val currentYear = java.time.LocalDate.now().year
        val years = mutableListOf<Int>()
        for (i in currentYear downTo 1900) {
            years.add(i)
        }
        years.toList()
    }

    var selectedMonth by remember { mutableStateOf(startMonth) }
    var selectedYear by remember { mutableIntStateOf(startYear) }

    val locale = LocalConfiguration.current.locales[0]

    var currentTabIndex by remember { mutableIntStateOf(0) }

    val selectedTabTextColor = MaterialTheme.colorScheme.onPrimary
    val unselectedTabTextColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f)

    Dialog(
        onDismissRequest = { onDismiss() }
    ) {
        Surface(
            color = MaterialTheme.colorScheme.surface,
            shape = MaterialTheme.shapes.medium
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .background(color = MaterialTheme.colorScheme.primary)
                        .padding(all = MaterialTheme.spacing.small)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "Seleziona mese e anno",
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.align(Alignment.Start)
                    )

                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        TextButton(
                            onClick = { currentTabIndex = 0 },
                            colors = ButtonDefaults.textButtonColors(contentColor = if (currentTabIndex == 0) selectedTabTextColor else unselectedTabTextColor)
                        ) {
                            Text(
                                text = selectedMonth.getDisplayName(TextStyle.FULL, locale)
                                    .uppercase(),
                                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = if (currentTabIndex == 0) FontWeight.Bold else FontWeight.Normal)
                            )
                        }

                        Text(text = "/", color = MaterialTheme.colorScheme.onPrimary)

                        TextButton(
                            onClick = { currentTabIndex = 1 },
                            colors = ButtonDefaults.textButtonColors(contentColor = if (currentTabIndex == 1) selectedTabTextColor else unselectedTabTextColor)
                        ) {
                            Text(
                                text = selectedYear.toString(),
                                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = if (currentTabIndex == 1) FontWeight.Bold else FontWeight.Normal)
                            )
                        }
                    }
                }

                Box(modifier = Modifier.height(400.dp)) {
                    Crossfade(targetState = currentTabIndex, label = "MonthYearSelector") { index ->
                        if (index == 0) {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
                                modifier = Modifier
                                    .padding(all = MaterialTheme.spacing.medium)
                                    .verticalScroll(
                                        rememberScrollState()
                                    )
                            ) {
                                months.chunked(3).forEach {
                                    Row(horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)) {
                                        it.forEach {
                                            Box(
                                                contentAlignment = Alignment.Center,
                                                modifier = Modifier
                                                    .size(80.dp)
                                                    .background(
                                                        color = if (selectedMonth == it) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                                                        shape = CircleShape
                                                    )
                                                    .clip(CircleShape)
                                                    .clickable { selectedMonth = it }
                                            ) {
                                                Text(
                                                    text = it.name.take(3),
                                                    color = if (selectedMonth == it) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            LazyColumn(
                                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
                                contentPadding = PaddingValues(all = MaterialTheme.spacing.medium),
                            ) {
                                years.chunked(3).forEach {
                                    item {
                                        Row(
                                            horizontalArrangement = Arrangement.spacedBy(
                                                MaterialTheme.spacing.medium
                                            )
                                        ) {
                                            it.forEach {
                                                Box(
                                                    contentAlignment = Alignment.Center,
                                                    modifier = Modifier
                                                        .size(80.dp)
                                                        .background(
                                                            color = if (selectedYear == it) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                                                            shape = CircleShape
                                                        )
                                                        .clip(CircleShape)
                                                        .clickable { selectedYear = it }
                                                ) {
                                                    Text(
                                                        text = it.toString(),
                                                        color = if (selectedYear == it) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                Row(
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(all = MaterialTheme.spacing.small)
                ) {
                    TextButton(onClick = { onConfirm(selectedMonth.value, selectedYear) }) {
                        Text(text = stringResource(id = R.string.annulla))
                    }

                    Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))

                    TextButton(onClick = { onDismiss() }) {
                        Text(text = stringResource(id = R.string.conferma))
                    }
                }
            }
        }
    }
}

@Composable
@Preview
fun MonthPickerPreview() {
    MonthPicker(
        startMonth = Month.JANUARY,
        startYear = 2023,
        onConfirm = { _, _ -> },
        onDismiss = { }
    )
}