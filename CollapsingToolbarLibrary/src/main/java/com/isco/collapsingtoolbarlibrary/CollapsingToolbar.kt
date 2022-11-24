package com.isco.collapsingtoolbarlibrary

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.jetbrains.annotations.TestOnly

@Composable
fun CollapsingToolbar() {
    Box(modifier = Modifier.size(50.dp).background(Color.Yellow))
}

@Preview
@Composable
private fun TestOnly() {
    CollapsingToolbar()
}