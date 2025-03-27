package com.example.cuongspotify.views.screens.player

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.activityViewModels
import com.example.cuongspotify.R
import com.example.cuongspotify.views.screens.home.HomeViewModal

class PlayerFragment : Fragment() {

    private val viewModel: HomeViewModal by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            // Dispose of the Composition when the view's LifecycleOwner
            // is destroyed
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting(name = "Binz")
                }
            }
        }
    }

    @Composable
    fun Greeting(name: String, modifier: Modifier = Modifier) {
        Row {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(items = arrayOf(
                    Triple("Nam", Color.Red, Color.Green),
                    Triple("Ousmane", Color.Green, Color.Blue)
                )) { item ->
                    RowItem(item)
                }
            }
            Surface {
                ElevatedButton(
                    modifier = Modifier.padding(8.dp, 0.dp, 0.dp, 0.dp),
                    onClick = { /*TODO*/ }
                ) {
                    Text(text = "Add more")
                }
            }
        }

    }

    @Composable
    fun RowItem(item: Triple<String, Color, Color>) {
        val expanded = rememberSaveable  {
            mutableStateOf(false)
        }
        Card {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(item.second)
                ) {
                    Text(
                        text = "Hello ${item.first}", modifier = Modifier
                            .weight(1f)
                            .border(1.dp, item.third, RoundedCornerShape(8.dp))
                            .align(Alignment.CenterVertically)
                    )
                    ElevatedButton(
                        modifier = Modifier.width(100.dp),
                        onClick = {
                            expanded.value = !expanded.value
                        }) {
                        Text(text = if (!expanded.value) "Detail" else "Hide")
                    }
                }
                if (expanded.value) {
                    Surface {
                        Text(
                            fontSize = 16.sp,
                            text = "In Jetpack Compose, there's no direct equivalent of View.GONE from XML. Instead, you can control visibility using if conditions or Modifier.height(0.dp).")
                    }
                }
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        MaterialTheme {
            Greeting("Boiz")
        }
    }
}