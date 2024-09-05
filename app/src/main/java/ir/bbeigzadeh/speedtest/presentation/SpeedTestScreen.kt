package ir.bbeigzadeh.speedtest.presentation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.keyframes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ir.bbeigzadeh.speedtest.model.UiState
import ir.bbeigzadeh.speedtest.ui.theme.DarkGradient
import ir.bbeigzadeh.speedtest.ui.theme.Green200
import ir.bbeigzadeh.speedtest.ui.theme.Pink
import ir.bbeigzadeh.speedtest.ui.theme.SpeedTestTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.max
import kotlin.math.roundToInt

@Composable
fun SpeedTestScreen() {
    val coroutinScope = rememberCoroutineScope()
    val animation = remember { androidx.compose.animation.core.Animatable(0f) }
    val maxSpeed = remember {
        mutableStateOf(0f)
    }
    maxSpeed.value = max(maxSpeed.value, animation.value * 100f)

    SpeedTestScreen(animation.toUiState(maxSpeed.value)) {

        coroutinScope.launch {
            maxSpeed.value
            startAnimation(animation)
        }

    }
}

suspend fun startAnimation(animation: Animatable<Float, AnimationVector1D>) {
    animation.animateTo(0.84f, keyframes {
        durationMillis = 9000
        0f at 0 with CubicBezierEasing(0f, 1.5f, 0.8f, 1f)
        0.72f at 1000 with CubicBezierEasing(0.2f, -1.5f, 0f, 1f)
        0.76f at 2000 with CubicBezierEasing(0.2f, -2f, 0f, 1f)
        0.78f at 3000 with CubicBezierEasing(0.2f, -1.5f, 0f, 1f)
        0.82f at 4000 with CubicBezierEasing(0.2f, -2f, 0f, 1f)
        0.85f at 5000 with CubicBezierEasing(0.2f, -2f, 0f, 1f)
        0.89f at 6000 with CubicBezierEasing(0.2f, -1.2f, 0f, 1f)
        0.82f at 7500 with LinearOutSlowInEasing
    })
}

fun Animatable<Float, AnimationVector1D>.toUiState(maxSpeed: Float) = UiState(
    arcValue = value,
    speed = "%.1f".format(value * 100),
    ping = if (value > 0.2f) "${(value * 15).roundToInt()} ms" else "-",
    maxSpeed = if (maxSpeed > 0f) "%.1f mbps".format(maxSpeed) else "-",
    inProgress = isRunning
)


@Composable
fun SpeedIndicator(state: UiState, onClick: () -> Unit) {
    Box(
        contentAlignment = Alignment.Center, modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .padding(40.dp)
    ) {
        SpeedIndicatorCanvas(dgree = state.arcValue)
        SpeedText(state.speed)
    }
}

@Composable
fun BoxScope.SpeedText(speed: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.align(Alignment.BottomCenter)
    ) {
        Text("DOWNLOAD", style = MaterialTheme.typography.labelLarge)
        Text(
            text = speed,
            fontSize = 45.sp,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
        Text("mbps", style = MaterialTheme.typography.labelLarge)

    }
}

@Composable
private fun SpeedTestScreen(state: UiState, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxSize()
            .background(
                DarkGradient
            )
    ) {
        ir.bbeigzadeh.speedtest.presentation.Header()
        SpeedIndicator(state = state, onClick = onClick)
        StartButton(isEnabled = !state.inProgress, onClick)
        AdditionalInfo(state.ping, state.maxSpeed)
        NavigationView()
    }
}

@Composable
fun AdditionalInfo(ping: String, maxSpeed: String) {
    @Composable
    fun RowScope.InfoColumn(title: String, value: String) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
            Text(text = title, color = Color.Gray)
            Text(
                text = value,
                color = Color.White,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
    Row(
        Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
    ) {
        InfoColumn(title = "PING", value = ping)
        VerticalDivider()
        InfoColumn(title = "MAX SPEED", value = maxSpeed)

    }

}

@Composable
fun StartButton(isEnabled: Boolean, onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        enabled = isEnabled,
        modifier = Modifier.padding(bottom = 25.dp)
    ) {
        Text(
            text = if (isEnabled) "START" else "Testing...",
            color = if (isEnabled) Color(0xff1C80F9) else Pink,
            modifier = Modifier.padding(horizontal = 30.dp, vertical = 10.dp),
            fontSize = 20.sp
        )
    }
}

@Composable
fun VerticalDivider() {
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .background(Color(0xff414d66))
            .width(1.dp)
    )
}


@Composable
fun Header() {
    Text(
        text = "SPEED TEST",
        color = Color.White,
        style = MaterialTheme.typography.headlineMedium,
        modifier = Modifier.padding(top = 65.dp, bottom = 16.dp),
    )

}

@Preview
@Composable
fun DefaultPreview() {
    SpeedTestTheme {
        Surface {
            SpeedTestScreen(
                UiState(
                    speed = "120.5",
                    ping = "5 ms",
                    maxSpeed = "150.0 mbps",
                    arcValue = 1f
                ), {})
        }
    }
}

@Composable
fun SpeedIndicatorCanvas(dgree: Float) {
    var currentLine by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        delay(500)
        for (i in 1..40) {
            currentLine = i
            delay(30)
        }
    }

    androidx.compose.foundation.Canvas(
        modifier = Modifier
            .fillMaxSize()
    ) {
        drawLiness(currentLine)
        drawArcss(dgree)
    }
}

fun DrawScope.drawArcss(progress: Float) {
    val startAngle = 270f - 240 / 2
    val sizeMain = size.copy(width = size.width - 100, height = size.height - 100)
    for (i in 0..20) {
        drawArc(
            color = Green200.copy(alpha = i / 1000f),
            startAngle = startAngle,
            sweepAngle = (progress * 240),
            useCenter = false,
            size = sizeMain,
            topLeft = Offset(50f, 50f),
            style = Stroke(width = 80f + (20 - i) * 20, cap = StrokeCap.Round)

        )
    }
    drawArc(
        color = Color(0xffB9FBCF),
        startAngle = startAngle,
        sweepAngle = (progress * 240),
        useCenter = false,
        size = sizeMain,
        topLeft = Offset(50f, 50f),
        style = Stroke(width = 70f, cap = StrokeCap.Round)
    )
    val pointB = Offset(200f, size.height / 2)
    rotate((progress * 240f) - 30) {

        for (i in 5..25 step 5) {
            drawLine(
                Color.White,

                start = pointB,
                end = center.copy(y = center.y + i), cap = StrokeCap.Round, strokeWidth = 10f
            )
            drawLine(
                Color.White,

                start = pointB,
                end = center.copy(y = center.y - i), cap = StrokeCap.Round, strokeWidth = 10f
            )
        }

    }

}


fun DrawScope.drawLiness(currentLine: Int) {
    val angleSize = 240f;
    val lines = 40
    val onRotation = angleSize / lines
    val startingAngle = 90 - angleSize / 2
    val pointB = Offset(0f, size.height / 2)
    val pointC = Offset(size.width / 10, size.height / 2)


    for ((i, line) in (0..currentLine).withIndex()) {
        rotate((onRotation * line + startingAngle).toFloat()) {

            drawLine(
                Color.White.copy(alpha = 0.5f),
                start = if (i % 5 == 0) pointC else pointC.copy(x = size.width / 20),
                end = pointB, cap = StrokeCap.Round, strokeWidth = 5f
            )
        }
    }
}
