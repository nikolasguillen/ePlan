package com.example.eplan

import com.example.eplan.ui.UIelements
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.compose.BackHandler
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.eplan.ui.NavigationItem
import com.shrikanthravi.collapsiblecalendarview.widget.CollapsibleCalendar

class HomeFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                OurAppTheme {
                    HomeComposer()
                }
            }
        }
    }

    private val ourDarkColorScheme = darkColorScheme()

    private val ourLightColorScheme = lightColorScheme()

    @Composable
    fun OurAppTheme(
        darkTheme: Boolean = isSystemInDarkTheme(),
        content: @Composable () -> Unit
    ) {
        val ourColorScheme = if (darkTheme) ourDarkColorScheme else ourLightColorScheme

        MaterialTheme(
            content = content,
            colorScheme = ourColorScheme
        )
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun HomeComposer() {

        val cards = remember { mutableListOf<List<String>>() }

        cards.add(listOf("commessa", "descrizione commessa", "08:00", "09:00", "2", "180"))
        cards.add(listOf("commessa", "descrizione commessa", "08:00", "09:00", "2", "180"))
        cards.add(listOf("commessa", "descrizione commessa", "08:00", "09:00", "2", "180"))
        cards.add(listOf("commessa", "descrizione commessa", "08:00", "09:00", "2", "180"))
        cards.add(listOf("commessa", "descrizione commessa", "08:00", "09:00", "2", "180"))
        cards.add(listOf("commessa", "descrizione commessa", "08:00", "09:00", "2", "180"))
        cards.add(listOf("commessa", "descrizione commessa", "08:00", "09:00", "2", "180"))
        cards.add(listOf("commessa", "descrizione commessa", "08:00", "09:00", "2", "180"))
        cards.add(listOf("commessa", "descrizione commessa", "08:00", "09:00", "2", "180"))
        cards.add(listOf("commessa", "descrizione commessa", "08:00", "09:00", "2", "180"))
        cards.add(listOf("commessa", "descrizione commessa", "08:00", "09:00", "2", "180"))
        cards.add(listOf("commessa", "descrizione commessa", "08:00", "09:00", "2", "180"))
        cards.add(listOf("commessa", "descrizione commessa", "08:00", "09:00", "2", "180"))


        Scaffold(
            bottomBar = { UIelements. },
            topBar = { TopBar() },
            floatingActionButton = { FloatingActionButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Outlined.Create, contentDescription = "Aggiungi attività")
            }
            },
            content = {
                BackHandler() {
                    activity?.finish()
                }
                Column(modifier = Modifier.padding(bottom = it.calculateBottomPadding())) {
                    setupCalendar()
                    LazyColumn() {
                        items(cards) { card ->
                            ActivityCard(
                                activityName = card[0],
                                activityDescription = card[1],
                                start = card[2],
                                end = card[3],
                                oreSpostamento = card[4],
                                km = card[5]
                            )
                        }
                    }
                }
            }
        )
    }

    @Composable
    private fun setupCalendar() {

        val todayBackgroundColor = MaterialTheme.colorScheme.primary.toArgb()
        val todayTextColor = MaterialTheme.colorScheme.onPrimary.toArgb()
        val selectedDayBackgroundColor = MaterialTheme.colorScheme.onSurface.toArgb()
        val selectedDayTextColor = MaterialTheme.colorScheme.surface.toArgb()
        val dynamicTextColor = MaterialTheme.colorScheme.onSurface.toArgb()

        AndroidView(factory = { context ->
            CollapsibleCalendar(context).apply {

                primaryColor = resources.getColor(R.color.transparent, context.theme)
                textColor = dynamicTextColor
                setExpandIconColor(dynamicTextColor)

                // Material dynamic theme
                AppCompatResources.getDrawable(context, R.drawable.selection_circle)?.setTint(selectedDayBackgroundColor)
                selectedItemBackgroundDrawable = AppCompatResources.getDrawable(context, R.drawable.selection_circle)
                selectedItemTextColor = selectedDayTextColor
                AppCompatResources.getDrawable(context, R.drawable.today_circle)?.setTint(todayBackgroundColor)
                todayItemBackgroundDrawable = AppCompatResources.getDrawable(context, R.drawable.today_circle)
                todayItemTextColor = todayTextColor

                setCalendarListener(object : CollapsibleCalendar.CalendarListener {
                    override fun onClickListener() {
                    }

                    override fun onDataUpdate() {
                    }

                    override fun onDayChanged() {
                    }

                    override fun onDaySelect() {
                        day = selectedDay
                        collapse(100)
                    }

                    override fun onItemClick(v: View) {
                    }

                    override fun onMonthChange() {
                    }

                    override fun onWeekChange(position: Int) {
                    }

                })
            }
        },
            modifier = (Modifier
                .fillMaxWidth()
                .padding(0.dp, 0.dp, 0.dp, 5.dp))
        )
    }

/*    @Composable
    private fun TopBar() {
        SmallTopAppBar(title = { Text(stringResource(id = R.string.app_name)) })
    }

    @Composable
    private fun BottomNavBar() {
        val items = listOf(
            NavigationItem.Home,
            NavigationItem.Appointments,
            NavigationItem.Account
        )
        NavigationBar() {
            items.forEach { item ->
                NavigationBarItem(
                    selected = (item.title == "Foglio ore"),
                    onClick = {  },
                    icon = { Icon(painterResource(id = item.icon), contentDescription = item.title) },
                    label = { Text(text = item.title) },
                    modifier = Modifier.background(
                        colorResource(id = R.color.transparent),
                        CircleShape
                    )
                )
            }
        }
    }*/

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun ActivityCard(activityName: String, activityDescription: String, start: String, end: String, oreSpostamento: String, km: String) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 5.dp)
                .clip(RoundedCornerShape(8.dp))
                .clickable {
                    findNavController().navigate(
                        HomeFragmentDirections.viewActivityDetails(
                            activityName,
                            activityDescription,
                            start,
                            end,
                            oreSpostamento,
                            km
                        )
                    )
                },
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = activityName.replaceFirstChar { it.uppercase() }, style = MaterialTheme.typography.titleMedium)
                Text(text = activityDescription.replaceFirstChar { it.uppercase() }, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(bottom = 3.dp))
                Text(text = start, style =  MaterialTheme.typography.labelSmall)
                Text(text = end, style =  MaterialTheme.typography.labelSmall)
            }
        }
    }

    inline fun Modifier.noRippleClickable(crossinline onClick: ()->Unit): Modifier = composed {
        clickable(indication = null,
            interactionSource = remember { MutableInteractionSource() }) {
            onClick()
        }
    }
}