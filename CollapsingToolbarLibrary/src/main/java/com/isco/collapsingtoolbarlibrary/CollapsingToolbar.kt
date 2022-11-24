package com.isco.collapsingtoolbarlibrary

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.jetbrains.annotations.TestOnly

@Composable
fun CollapsingToolbar() {
    Box(modifier = androidx.compose.ui.Modifier.size(50.dp))
}

@Preview
@Composable
private fun TestOnly() {
    CollapsingToolbar()
}