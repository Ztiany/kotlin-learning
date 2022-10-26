package me.ztiany.kotlin.kmm.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.launch
import me.ztiany.kotlin.kmm.Greeting
import me.ztiany.kotlin.kmm.HttpUtil

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var content by mutableStateOf("无")

        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background
                ) {

                    Column(Modifier.fillMaxSize()) {

                        val scope = rememberCoroutineScope()

                        DateTime()

                        Button(onClick = {
                            scope.launch {
                                content = "加载中"
                                content = HttpUtil().getData()
                            }
                        }) {
                            Text(text = "加载")
                        }

                        Text(text = content)
                    }
                }
            }
        }

    }
}

@Composable
fun DateTime() {
    Text(text = Greeting().greeting())
}

@Preview
@Composable
fun DefaultPreview() {
    MyApplicationTheme {
        DateTime()
    }
}
