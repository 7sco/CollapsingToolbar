package com.isco.collapsingtoolbarlibrary

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextMotion
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.unit.sp

private enum class DragAnchors(val height: Dp) { START(112.dp), END(56.dp) }
private const val THRESHOLD_MULTIPLIER = 0.5f

/**
 * Material Design small top app bar with title size animation
 * Params:
 * title - the title to be displayed in the top app bar
 * fontSizeStart - title font size on the expanded state
 * fontSizeEnd - title font size on the collapsed state
 * navigationIcon - the navigation icon displayed at the start of the top app bar.
 * actions - the actions displayed at the end of the top app bar
 * content - content of your screen.
 */
@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun CollapsingAppBar(
    title: String,
    fontSizeStart: TextUnit = 24.sp,
    fontSizeEnd: TextUnit = 16.sp,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    content: @Composable () -> Unit = {}
) {
    val density = LocalDensity.current

    val state = remember {
        AnchoredDraggableState(
            initialValue = DragAnchors.START,
            positionalThreshold = { distance: Float -> distance * THRESHOLD_MULTIPLIER },
            velocityThreshold = { with(density) { 0.dp.toPx() } },
            animationSpec = tween(),
        ).apply {
            updateAnchors(
                DraggableAnchors {
                    DragAnchors.START at with(density) { DragAnchors.START.height.toPx() }
                    DragAnchors.END at with(density) { DragAnchors.END.height.toPx() }
                }
            )
        }
    }
    val connection = remember {
        nestedConnectionSetup(
            onDrag = { delta -> state.dispatchRawDelta(delta = delta).toOffset() }, onFling = {}
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(connection)
    ) {
        val progress = remember {
            derivedStateOf {
                if (state.targetValue == DragAnchors.END) state.progress
                else 1f - state.progress
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(state.toDp(density))
                .background(
                    if (progress.value > 0.0) MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)
                    else Color.Transparent
                )
        ) {
            TopAppBar(
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color.Transparent),
                title = {
                    AnimatedSizedText(
                        title = title,
                        fontSizeStart = fontSizeStart,
                        fontSizeEnd = fontSizeEnd,
                        progress = progress.value
                    )
                },
                navigationIcon = navigationIcon,
                actions = actions,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomStart),
            )
        }

        content()
    }
}

@Composable
private fun AnimatedSizedText(
    title: String,
    fontSizeStart: TextUnit,
    fontSizeEnd: TextUnit,
    progress: Float
) {
    Text(
        text = title,
        modifier = Modifier.wrapContentHeight(),
        color = MaterialTheme.colorScheme.onSurface,
        fontSize = lerp(fontSizeStart, fontSizeEnd, progress),
        style = MaterialTheme.typography.titleLarge.copy(textMotion = TextMotion.Animated),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}

@OptIn(ExperimentalFoundationApi::class)
private fun AnchoredDraggableState<DragAnchors>.toDp(density: Density) =
    with(density) { this@toDp.requireOffset().toDp() }

private fun Float.toOffset() = Offset(0f, this)

internal fun nestedConnectionSetup(
    onDrag: (Float) -> Offset,
    onFling: suspend (Float) -> Unit
) = object : NestedScrollConnection {
    override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
        val delta: Float = available.y
        return if (delta < 0) onDrag(delta) else Offset.Zero
    }

    override fun onPostScroll(
        consumed: Offset,
        available: Offset,
        source: NestedScrollSource
    ): Offset {
        val delta = available.y
        return onDrag(delta)
    }

    override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
        onFling(available.y)
        return super.onPostFling(consumed, available)
    }
}
