package tw.edu.pu.csim.s1120053.touch

import android.os.Bundle
import android.view.MotionEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import tw.edu.pu.csim.s1120053.touch.ui.theme.ColorBlue
import tw.edu.pu.csim.s1120053.touch.ui.theme.ColorGreen
import tw.edu.pu.csim.s1120053.touch.ui.theme.ColorIndigo
import tw.edu.pu.csim.s1120053.touch.ui.theme.ColorOrange
import tw.edu.pu.csim.s1120053.touch.ui.theme.ColorPurple
import tw.edu.pu.csim.s1120053.touch.ui.theme.ColorRed
import tw.edu.pu.csim.s1120053.touch.ui.theme.ColorYellow
import tw.edu.pu.csim.s1120053.touch.ui.theme.TouchTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TouchTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                    DrawCircle()
                    DrawPath()
                }
            }
        }
    }
}
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
    Row {
        Text(text = "多指觸控Compose實例",
            fontFamily = FontFamily(Font(R.font.finger)),
            fontSize = 25.sp,
            color = Color.Blue)
        Image(
            painter = painterResource(id = R.drawable.hand),
            contentDescription = "手掌圖片",
            alpha = 0.7f,
            modifier = Modifier
                .clip(CircleShape)
                .background(Color.Blue)
        )
    }
        Text(text = "作者：張惠芯",
            fontFamily = FontFamily(Font(R.font.finger)),
            fontSize = 25.sp,
            color = Color.Black)
    }
}
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DrawCircle() {
//    var X by remember { mutableStateOf(0f) }
//    var Y by remember { mutableStateOf(0f) }
//    val handImage = ImageBitmap.imageResource(R.drawable.hand)
    var X = remember { mutableStateListOf(0f) }
    var Y = remember { mutableStateListOf(0f) }
    var Fingers by remember {  mutableStateOf (0)  }
    var PaintColor:Color
    var colors = arrayListOf(
        ColorRed, ColorOrange, ColorYellow, ColorGreen,
        ColorBlue, ColorIndigo, ColorPurple
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInteropFilter { event ->
//                X = event.getX(0)
//                Y = event.getY(0)
                Fingers = event.getPointerCount()
                X.clear()
                Y.clear()
                for (i in 0..Fingers - 1) {
                    X.add( event.getX(i))
                    Y.add (event.getY(i))
                }
                true
            }
    ){
        Canvas(modifier = Modifier){
//            drawCircle(Color.Yellow,100f,Offset(X, Y))
//            drawImage(handImage, Offset(X-handImage.width/2,Y-handImage.height/2))
            for (i in 0 until Fingers) {
                val paintColor = colors[i % colors.size]
                drawCircle(paintColor, 100f, Offset(X[i], Y[i]))
            }
        }
    }
}
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DrawPath() {
    val paths = remember { mutableStateListOf<Points>() }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInteropFilter { event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        paths.clear()
                        true
                    }
                    MotionEvent.ACTION_MOVE -> {
                        paths += Points(event.x, event.y)
                        true
                    }
                    else -> false
                }
            }
    ){
        Canvas(modifier = Modifier){
            val p = Path()
//            p.moveTo(500f, 300f)
//            p.lineTo(300f,600f)
            var j = 0
            for (path in paths) {
                if (j==0){  //第一筆不畫
                    p.moveTo(path.x, path.y)
                }
                else{
                    p.lineTo(path.x, path.y)
                }
                j++
            }
            drawPath(p, color = Color.Black,
                style = Stroke(width = 30f, join = StrokeJoin.Round)
            )
        }
    }
}
data class Points(
    val x: Float,
    val y: Float
)