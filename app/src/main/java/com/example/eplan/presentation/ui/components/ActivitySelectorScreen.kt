package com.example.eplan.presentation.ui.components

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.eplan.R
import com.example.eplan.domain.model.Activity
import com.example.eplan.presentation.util.spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivitySelectorScreen(
    activities: List<Activity?>,
    selectedActivityId: String?,
    onActivitySelected: (String) -> Unit,
    searchQuery: String,
    onQueryChange: (String) -> Unit,
    onBackPressed: () -> Unit
) {

    Scaffold(
        modifier = Modifier.background(color = MaterialTheme.colorScheme.primaryContainer),
        topBar = {
            MediumTopAppBar(
                title = { Text(text = stringResource(id = R.string.seleziona_attivita)) },
                navigationIcon = {
                    IconButton(onClick = { onBackPressed() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.indietro)
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        BackHandler { onBackPressed() }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { onQueryChange(it) },
                label = { Text(text = stringResource(id = R.string.attivita)) },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Search, contentDescription = null)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MaterialTheme.spacing.medium)
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                activities
                    .filter {
                        it?.name?.contains(searchQuery, true) == true
                    }
                    .forEach { activity ->
                        activity?.let {
                            item {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { onActivitySelected(it.id) },
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    val scrollState = rememberScrollState()

                                    RadioButton(
                                        selected = it.id == selectedActivityId,
                                        onClick = { onActivitySelected(it.id) }
                                    )

                                    Row(
                                        modifier = Modifier
                                            .horizontalFadingEdge(
                                                scrollState,
                                                20.dp,
                                                MaterialTheme.colorScheme.surface
                                            )
                                            .horizontalScroll(scrollState)
                                    ) {
                                        Text(text = it.name)
                                    }
                                }
                            }
                        }
                    }
            }
        }
    }
}

private fun Modifier.horizontalFadingEdge(
    scrollState: ScrollState,
    length: Dp,
    edgeColor: Color? = null,
) = composed(
    debugInspectorInfo {
        name = "length"
        value = length
    }
) {
    val color = edgeColor ?: MaterialTheme.colorScheme.surface

    drawWithContent {
        val lengthValue = length.toPx()
        val scrollFromStart by derivedStateOf { scrollState.value }
        val scrollFromEnd by derivedStateOf { scrollState.maxValue - scrollState.value }

        val startFadingEdgeStrength = lengthValue * (scrollFromStart / lengthValue).coerceAtMost(1f)

        val endFadingEdgeStrength = lengthValue * (scrollFromEnd / lengthValue).coerceAtMost(1f)

        drawContent()

        drawRect(
            brush = Brush.horizontalGradient(
                colors = listOf(
                    color,
                    Color.Transparent,
                ),
                startX = 0f,
                endX = startFadingEdgeStrength,
            ),
            size = Size(
                startFadingEdgeStrength,
                this.size.height,
            ),
        )

        drawRect(
            brush = Brush.horizontalGradient(
                colors = listOf(
                    Color.Transparent,
                    color,
                ),
                startX = size.width - endFadingEdgeStrength,
                endX = size.width,
            ),
            topLeft = Offset(x = size.width - endFadingEdgeStrength, y = 0f),
        )
    }
}