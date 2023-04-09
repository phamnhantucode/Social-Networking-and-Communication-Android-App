package com.phamnhantucode.composeclonemessengerclient.chatfeature

import android.widget.Space
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface

import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.phamnhantucode.composeclonemessengerclient.R
import com.phamnhantucode.composeclonemessengerclient.ui.theme.lightColor2
import com.phamnhantucode.composeclonemessengerclient.ui.theme.lightTextBody
import kotlinx.coroutines.delay

@Composable
fun SearchScreen(
    navController: NavHostController,
    viewModel: ChatViewModel
) {
    Surface(
        color = MaterialTheme.colors.background,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier,
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            TopBarSearchScreen(
                modifier = Modifier
                    .fillMaxWidth()
            )
            MidBarSearchScreen(
                modifier = Modifier.fillMaxWidth()
            )
            Divider(
                startIndent = 20.dp,
                thickness = 2.dp,
                modifier = Modifier.align(CenterHorizontally)
            )
            LazyColumn(
            ) {
                items(200) {
                    SinglePerson(
                        modifier = Modifier
                            .padding(horizontal = 12.dp, vertical = 5.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun TopBarSearchScreen(
    modifier: Modifier = Modifier,
    isDarkTheme: Boolean = isSystemInDarkTheme()
) {
    val iconColor = if (isDarkTheme) Color.White else Color.Black
    var backgroundColor by remember {
        mutableStateOf(Color.White)
    }
    if (isDarkTheme) {
        backgroundColor = Color.Black
    }
    var searchValue by remember {
        mutableStateOf("")
    }
    Row(
        modifier = modifier
            .background(backgroundColor)
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "Back",
            colorFilter = ColorFilter.tint(iconColor),
            modifier = Modifier
                .size(30.dp)
        )
        BasicTextField(
            value = searchValue,
            onValueChange = {},
            modifier = Modifier
                .weight(Float.MAX_VALUE)
        ) { innerTextField ->
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(22.dp))
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier,

                    ) {
                    if (searchValue.isNotBlank()) {
                        innerTextField()
                    } else {
                        Text(
                            text = stringResource(id = R.string.searchHolder),
                            style = TextStyle(
                                fontSize = 16.sp
                            )
                        )
                    }
                }
                if (searchValue.isNotBlank()) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_x),
                        contentDescription = "Back",
                        colorFilter = ColorFilter.tint(iconColor),
                        modifier = Modifier
                            .size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun MidBarSearchScreen(
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        var selectedTabPosition by remember { mutableStateOf(0) }

        val items = listOf(
            "Friends", "People"
        )

        TabRow(
            selectedTabPosition = selectedTabPosition,
            modifier = Modifier
        ) {
            TabItem(title = "Friends", icon = R.drawable.ic_friend, position = 0) {
                selectedTabPosition = 0
            }
            TabItem(title = "People", icon = R.drawable.ic_people, position = 1) {
                selectedTabPosition = 1
            }
        }
    }
}

@Composable
fun SinglePerson(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.user_holder),
            contentDescription = "user",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape),
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = "Demo Username",
            style = TextStyle(
                color = MaterialTheme.colors.onBackground,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        )
    }
}

enum class SubComposeID {
    PRE_CALCULATE_ITEM,
    ITEM,
    INDICATOR
}

data class TabPosition(
    val left: Dp, val width: Dp
)


@Composable
fun TabRow(
    containerColor: Color = Color.White,
    indicatorColor: Color = MaterialTheme.colors.primary,
    containerShape: Shape = CircleShape,
    indicatorShape: Shape = CircleShape,
    paddingValues: PaddingValues = PaddingValues(4.dp),
    animationSpec: AnimationSpec<Dp> = tween(durationMillis = 250, easing = FastOutSlowInEasing),
    fixedSize: Boolean = true,
    selectedTabPosition: Int = 0,
    modifier: Modifier = Modifier,
    tabItem: @Composable () -> Unit
) {

    Surface(
        color = containerColor,
        shape = containerShape,
        modifier = modifier
    ) {
        SubcomposeLayout(
            modifier
                .padding(paddingValues)
                .selectableGroup()
        ) { constraints ->
            val tabMeasurable: List<Placeable> =
                subcompose(SubComposeID.PRE_CALCULATE_ITEM, tabItem)
                    .map { it.measure(constraints) }
            val itemsCount = tabMeasurable.size
            val maxItemWidth = tabMeasurable.maxOf { it.width }
            val maxItemHeight = tabMeasurable.maxOf { it.height }

            val tabPlacables = subcompose(SubComposeID.ITEM, tabItem).map {
                val c = if (fixedSize) constraints.copy(
                    minWidth = maxItemWidth,
                    maxWidth = maxItemWidth,
                    minHeight = maxItemHeight
                ) else constraints
                it.measure(c)
            }

            val tabPositions = tabPlacables.mapIndexed { index, placeable ->
                val itemWidth = if (fixedSize) maxItemWidth else placeable.width
                val x = if (fixedSize) {
                    maxItemWidth * index
                } else {
                    val leftTabWith = tabPlacables.take(index).sumOf { it.width }
                    leftTabWith
                }
                TabPosition(x.toDp(), itemWidth.toDp())
            }

            val tabRowWidth = if (fixedSize) maxItemWidth * itemsCount
            else tabPlacables.sumOf { it.width }

            layout(tabRowWidth, maxItemHeight) {
                subcompose(SubComposeID.INDICATOR) {
                    Box(
                        Modifier
                            .tabIndicator(tabPositions[selectedTabPosition], animationSpec)
                            .fillMaxWidth()
                            .height(maxItemHeight.toDp())
                            .background(color = indicatorColor, indicatorShape)
                    )
                }.forEach {
                    it.measure(Constraints.fixed(tabRowWidth, maxItemHeight)).placeRelative(0, 0)
                }

                tabPlacables.forEachIndexed { index, placeable ->
                    val x = if (fixedSize) {
                        maxItemWidth * index
                    } else {
                        val leftTabWith = tabPlacables.take(index).sumOf { it.width }
                        leftTabWith
                    }
                    placeable.placeRelative(x, 0)
                }
            }
        }
    }
}

fun Modifier.tabIndicator(
    tabPosition: TabPosition,
    animationSpec: AnimationSpec<Dp>,
): Modifier = composed(
    inspectorInfo = debugInspectorInfo {
        name = "tabIndicatorOffset"
        value = tabPosition
    }
) {
    val currentTabWidth by animateDpAsState(
        targetValue = tabPosition.width,
        animationSpec = animationSpec
    )
    val indicatorOffset by animateDpAsState(
        targetValue = tabPosition.left,
        animationSpec = animationSpec
    )
    fillMaxWidth()
        .wrapContentSize(Alignment.BottomStart)
        .offset(x = indicatorOffset)
        .width(currentTabWidth)
        .fillMaxHeight()
}

@Composable
fun TabItem(
    title: String,
    icon: Int,
    position: Int,
    onClick: (Int) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Image(
            painter = painterResource(id = icon), contentDescription = title,
            modifier = Modifier.size(20.dp),
            colorFilter = ColorFilter.tint(color = MaterialTheme.colors.onPrimary)
        )
        Text(
            text = title,
            Modifier
                .wrapContentWidth(Alignment.CenterHorizontally)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                ) { onClick(position) },
            color = MaterialTheme.colors.onPrimary
        )
    }
}