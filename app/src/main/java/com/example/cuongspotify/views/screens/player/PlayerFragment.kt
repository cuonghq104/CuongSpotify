package com.example.cuongspotify.views.screens.player

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.activityViewModels
import coil3.compose.AsyncImage
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
                val search = rememberSaveable {
                    mutableStateOf("")
                }

                Scaffold(
                    content = { innerPadding ->
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding),
                        ) {
                            SearchBar(search)
                            ListRow()
                            GridView(search)
                        }
                    }
                )

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
        val expanded = rememberSaveable {
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
    fun ListRow() {
        Column (modifier = Modifier.fillMaxWidth()) {
            LazyRow (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(items = arrayOf(
                    Triple("Ian McKellen", "https://upload.wikimedia.org/wikipedia/commons/thumb/1/15/SDCC13_-_Ian_McKellen.jpg/1200px-SDCC13_-_Ian_McKellen.jpg", Color.Green),
                    Triple("Robert Downey JR", "https://upload.wikimedia.org/wikipedia/commons/thumb/1/15/SDCC13_-_Ian_McKellen.jpg/1200px-SDCC13_-_Ian_McKellen.jpg", Color.Green),
                    Triple("Ian McKellen", "https://upload.wikimedia.org/wikipedia/commons/thumb/1/15/SDCC13_-_Ian_McKellen.jpg/1200px-SDCC13_-_Ian_McKellen.jpg", Color.Green)
                )) {
                    item -> Column {
                        AsyncImage(
                            modifier = Modifier
                                .size(80.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop,
                            model = item.second,
                            contentDescription = "Profile"
                        )
                        Text(text = item.first, modifier = Modifier
                            .width(80.dp)
                            .padding(0.dp, 8.dp, 0.dp, 0.dp), textAlign = TextAlign.Center)
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun GridView(search: MutableState<String>) {
        val items = arrayOf(
            Triple("Ian McKellen", "https://upload.wikimedia.org/wikipedia/commons/thumb/1/15/SDCC13_-_Ian_McKellen.jpg/1200px-SDCC13_-_Ian_McKellen.jpg", Color.Green),
            Triple("Robert Downey JRRRRR", "https://upload.wikimedia.org/wikipedia/commons/thumb/1/15/SDCC13_-_Ian_McKellen.jpg/1200px-SDCC13_-_Ian_McKellen.jpg", Color.Green),
            Triple("Ian McKellen", "https://upload.wikimedia.org/wikipedia/commons/thumb/1/15/SDCC13_-_Ian_McKellen.jpg/1200px-SDCC13_-_Ian_McKellen.jpg", Color.Green)
        ).filter {
            it.first.startsWith(search.value, ignoreCase = true)
        }
        Column(modifier = Modifier.fillMaxWidth()) {
            LazyHorizontalGrid(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                rows = GridCells.Fixed(2),
                modifier = Modifier
                    .height(128.dp)
                    .scrollable(
                        state = rememberScrollState(),
                        orientation = Orientation.Horizontal,
                        overscrollEffect = null
                    )
            ) {
                items(items = items) {
                    item -> Card(modifier = Modifier.wrapContentHeight()) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.wrapContentHeight()) {
                            AsyncImage(
                                modifier = Modifier
                                    .size(60.dp),
                                contentScale = ContentScale.Crop,
                                model = item.second,
                                contentDescription = "Profile"
                            )
                            Text(text = item.first, modifier = Modifier
                                .width(160.dp), textAlign = TextAlign.Center)
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun SearchBar(search: MutableState<String>) {
        val focusManager = LocalFocusManager.current
        TextField(
            value = search.value,
            onValueChange = {
                search.value = it
            },
            singleLine = true,
            textStyle = TextStyle(
                fontSize = 20.sp,
                color = Color.Blue
            ),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Red,
                unfocusedLabelColor = Color.Green
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 0.dp, 16.dp, 0.dp)
                .clip(RoundedCornerShape(16.dp))
                .border(1.dp, Color.Black, RoundedCornerShape(16.dp)),
            placeholder = {
                Text(text = "Search here", color = Color.Cyan, fontSize = 14.sp)
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "searchIcon",
                    tint = Color.Cyan,
                    modifier = Modifier
                        .width(64.dp)
                        .height(64.dp)
                )
            },
            keyboardActions = KeyboardActions(
                onDone = { focusManager.clearFocus() }
            )
        )
    }

    @Composable
    fun GreetingPreview() {
        MaterialTheme {
            Greeting("Boiz")
        }
    }
}