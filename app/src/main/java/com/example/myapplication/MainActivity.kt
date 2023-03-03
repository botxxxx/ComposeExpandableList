@file:OptIn(ExperimentalMaterial3Api::class)
@file:Suppress("OPT_IN_IS_NOT_ENABLED")

package com.example.myapplication

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.BasicsCodelabTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BaseApp("Android")
        }
    }
}

@Composable
fun BaseApp(name: String, preView: Boolean = true) {
    BasicsCodelabTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            var shouldShowOnboard by rememberSaveable { mutableStateOf(preView) }
            val scrollState = rememberScrollState(1)
            if (shouldShowOnboard) {
                OnboardScreen(callback = { shouldShowOnboard = false })
            } else {
                Column(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp)
                ) {
                    BaseCardView(name, scrollState)
                    Menu(scrollState = scrollState)
                }
            }
        }
    }
}

@Composable
fun Menu(names: List<String> = List(50) { "$it" }, scrollState: ScrollState) {
    LazyColumn(
        modifier = Modifier.padding(vertical = 1.dp)
    ) {
        items(items = names) { name ->
            BaseCardView(name, scrollState)
        }
    }
}

@Composable
fun BaseCardView(name: String, scrollState: ScrollState) {
    val expanded = rememberSaveable { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val scrollToPosition = remember { mutableStateOf(0F) }

    val onClickCallback: () -> Unit = {
        expanded.value = !expanded.value
        coroutineScope.launch {
            Log.e("test", "scrollState:${scrollState.value}")
        }
    }

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            contentColor = MaterialTheme.colorScheme.primary,
        ),
        onClick = onClickCallback,
        modifier = Modifier.padding(top = 5.dp)
    ) {
        BlueButtonRow(name = name, onClickCallback, expanded = expanded, scrollToPosition = scrollToPosition)
    }
}

@Preview(showBackground = true, widthDp = 320, uiMode = UI_MODE_NIGHT_NO)
@Composable
fun DefaultPreview() {
    BaseApp("Android", false)
}

@Composable
fun BlueButtonRow(
    name: String, callback: () -> Unit, expanded: MutableState<Boolean>, scrollToPosition: MutableState<Float>
) {
    Surface(
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier
            .padding(vertical = 1.dp)
            .onGloballyPositioned { coordinates ->
                scrollToPosition.value = coordinates.positionInParent().y
            }
    ) {
        val animationContentSize: (Modifier) -> Modifier = {
            it.animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
        }
        Row(modifier = animationContentSize.invoke(Modifier.padding(24.dp))) {
            Column(
            ) {
                Text(text = "Hi")
                Text(
                    text = name,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.ExtraBold
                    ),
                )
                if (expanded.value) {
                    Text(
                        text = "Composem ipsum color sit lazy, " +
                                "padding theme elit, sed do bouncy. ".repeat(10)
                    )
                }
            }
            IconButton(onClick = callback) {
                val expandLess = Icons.Filled.ExpandLess
                val expandMore = Icons.Filled.ExpandMore
                Icon(
                    imageVector = if (expanded.value) expandLess else expandMore,
                    contentDescription = "",
                    modifier = Modifier.padding(12.dp)
                )
            }
        }
    }
}

@Composable
fun OnboardScreen(modifier: Modifier = Modifier, callback: () -> Unit) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Welcome to the Basics Codelab!")
        Button(
            modifier = Modifier.padding(vertical = 24.dp),
            onClick = callback
        ) {
            Text("Continue")
        }
    }
}