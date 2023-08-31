package com.isco.collapsingtoolbarexample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.isco.collapsingtoolbarexample.ui.theme.CollapsingToolbarExampleTheme
import com.isco.collapsingtoolbarlibrary.CollapsingAppBar

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CollapsingToolbarExampleTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    CollapsingAppBar(
                        title = "Title"
                    ) {
                        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                            repeat(50) {
                                Text(
                                    text = it.toString(),
                                    color = Color.Red,
                                    modifier = Modifier
                                        .padding(4.dp)
                                        .fillMaxWidth()
                                        .background(Color.Yellow)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}