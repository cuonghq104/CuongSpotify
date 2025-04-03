package com.example.cuongspotify.views.screens.player

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Slider
import androidx.compose.material.SliderDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledIconToggleButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
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
        (activity as AppCompatActivity).supportActionBar?.hide()
        view?.let {
            ViewCompat.setOnApplyWindowInsetsListener(it) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }
        }
        return ComposeView(requireContext()).apply {
            // Dispose of the Composition when the view's LifecycleOwner
            // is destroyed
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                val search = rememberSaveable {
                    mutableStateOf("")
                }
                PlayerScreen()
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun PlayerScreen() {
        var isRotating = remember { mutableStateOf(true) }
        val rotationAngle = remember { Animatable(0f) }

        LaunchedEffect(isRotating.value) {
            if (isRotating.value) {
                rotationAngle.animateTo(
                    targetValue = rotationAngle.value + 360f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(durationMillis = 15000, easing = LinearEasing),
                        repeatMode = RepeatMode.Restart
                    )
                )
            } else {
                rotationAngle.stop()
            }
        }


        Scaffold(
            modifier = Modifier.windowInsetsPadding(WindowInsets.systemBars),
            topBar = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(onClick = {
                        findNavController().popBackStack()
                    }) {
                        Icon(Icons.Outlined.KeyboardArrowDown, contentDescription = "Down")
                    }
                }
            },
            content = { innerPadding ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.test),
                        contentDescription = "test",
                        modifier = Modifier
                            .size(300.dp)
                            .clip(CircleShape)
                            .graphicsLayer(rotationZ = rotationAngle.value),
                        contentScale = ContentScale.Crop
                    )
                    TrackNameAndArtist()
                    TrackProgress()
                    PlayerControl(isRotating)
//                    ElevatedButton(onClick = { isRotating = !isRotating }, modifier = Modifier.padding(0.dp, 16.dp, 0.dp, 0.dp)) {
//                        Text(text = "Click")
//                    }
                }
            }
        )
    }

    @Composable
    fun PlayerControl(isRotating: MutableState<Boolean>) {
        Row(
            modifier = Modifier
                .padding(0.dp, 16.dp, 0.dp, 0.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            PlayerIconButton(
                painter = painterResource(id = R.drawable.baseline_shuffle_24),
                contentDescription = "Shuffle",
                size = 24.dp
            ) {

            }
            PlayerIconButton(
                painter = painterResource(id = R.drawable.baseline_arrow_left_24),
                contentDescription = "Prev"
            ) {

            }
            FilledIconToggleButton(
                checked = isRotating.value,
                onCheckedChange = {
                    isRotating.value = it
                },
                modifier = Modifier.size(64.dp)
            ) {
                Icon(
                    painter = if (isRotating.value) painterResource(id = R.drawable.baseline_pause_24) else painterResource(id = R.drawable.baseline_play_arrow_24),
                    contentDescription = "Play", modifier = Modifier.size(32.dp)
                )
            }
            PlayerIconButton(
                painter = painterResource(id = R.drawable.baseline_arrow_right_24),
                contentDescription = "Next"
            ) {

            }
            PlayerIconButton(
                painter = painterResource(id = R.drawable.baseline_repeat_24),
                contentDescription = "Repeat",
                size = 24.dp
            ) {

            }
        }
    }

    @Composable
    fun PlayerIconButton(
        painter: Painter? = null,
        imageVector: ImageVector? = null,
        contentDescription: String,
        size: Dp? = null,
        onClick: () -> Unit
    ) {
        IconButton(onClick = onClick) {
            painter?.let {
                Icon(painter = it, contentDescription = contentDescription, modifier = Modifier.size(size ?: 32.dp))
            } ?: imageVector?.let {
                Icon(it, contentDescription = contentDescription, modifier = Modifier.size(size ?: 32.dp))
            }
        }
    }

    @Composable
    fun TrackNameAndArtist() {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp, 16.dp, 16.dp, 0.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "Mùa mưa ấy", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text(text = "Vũ.", fontSize = 16.sp)
            }
            IconButton(modifier = Modifier.size(24.dp), onClick = { /*TODO*/ }) {
                Icon(painter = painterResource(id = R.drawable.baseline_add_circle_outline_24), contentDescription = "Add icon")
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun TrackProgress() {
        var sliderPosition by remember {
            mutableFloatStateOf(0.5f)
        }
        Column(modifier = Modifier.padding(8.dp, 0.dp, 8.dp, 0.dp)) {
            Slider(
                value = sliderPosition,
                onValueChange = { sliderPosition = it },
                modifier = Modifier.height(16.dp),
            )
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp, 4.dp, 8.dp, 0.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = "00:00")
                Text(text = "04:00")
            }
        }
    }

//    @Composable
//    fun Greeting(name: String, modifier: Modifier = Modifier) {
//        Row {
//            LazyColumn(
//                verticalArrangement = Arrangement.spacedBy(8.dp),
//                modifier = Modifier.weight(1f)
//            ) {
//                items(items = arrayOf(
//                    Triple("Nam", Color.Red, Color.Green),
//                    Triple("Ousmane", Color.Green, Color.Blue)
//                )) { item ->
//                    RowItem(item)
//                }
//            }
//            Surface {
//                ElevatedButton(
//                    modifier = Modifier.padding(8.dp, 0.dp, 0.dp, 0.dp),
//                    onClick = { /*TODO*/ }
//                ) {
//                    Text(text = "Add more")
//                }
//            }
//        }
//    }
//
//    @Composable
//    fun RowItem(item: Triple<String, Color, Color>) {
//        val expanded = rememberSaveable {
//            mutableStateOf(false)
//        }
//        Card {
//            Column {
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .background(item.second)
//                ) {
//                    Text(
//                        text = "Hello ${item.first}", modifier = Modifier
//                            .weight(1f)
//                            .border(1.dp, item.third, RoundedCornerShape(8.dp))
//                            .align(Alignment.CenterVertically)
//                    )
//                    ElevatedButton(
//                        modifier = Modifier.width(100.dp),
//                        onClick = {
//                            expanded.value = !expanded.value
//                        }) {
//                        Text(text = if (!expanded.value) "Detail" else "Hide")
//                    }
//                }
//                if (expanded.value) {
//                    Surface {
//                        Text(
//                            fontSize = 16.sp,
//                            text = "In Jetpack Compose, there's no direct equivalent of View.GONE from XML. Instead, you can control visibility using if conditions or Modifier.height(0.dp).")
//                    }
//                }
//            }
//        }
//    }
//
//    @Preview(showBackground = true)
//    @Composable
//    fun ListRow() {
//        Column (modifier = Modifier.fillMaxWidth()) {
//            LazyRow (
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.spacedBy(8.dp)
//            ) {
//                items(items = arrayOf(
//                    Triple("Ian McKellen", "https://upload.wikimedia.org/wikipedia/commons/thumb/1/15/SDCC13_-_Ian_McKellen.jpg/1200px-SDCC13_-_Ian_McKellen.jpg", Color.Green),
//                    Triple("Robert Downey JR", "https://upload.wikimedia.org/wikipedia/commons/thumb/1/15/SDCC13_-_Ian_McKellen.jpg/1200px-SDCC13_-_Ian_McKellen.jpg", Color.Green),
//                    Triple("Ian McKellen", "https://upload.wikimedia.org/wikipedia/commons/thumb/1/15/SDCC13_-_Ian_McKellen.jpg/1200px-SDCC13_-_Ian_McKellen.jpg", Color.Green)
//                )) {
//                    item -> Column {
//                        AsyncImage(
//                            modifier = Modifier
//                                .size(80.dp)
//                                .clip(CircleShape),
//                            contentScale = ContentScale.Crop,
//                            model = item.second,
//                            contentDescription = "Profile"
//                        )
//                        Text(text = item.first, modifier = Modifier
//                            .width(80.dp)
//                            .padding(0.dp, 8.dp, 0.dp, 0.dp), textAlign = TextAlign.Center)
//                    }
//                }
//            }
//        }
//    }
//
//    @OptIn(ExperimentalFoundationApi::class)
//    @Composable
//    fun GridView(search: MutableState<String>) {
//        val items = arrayOf(
//            Triple("Ian McKellen", "https://upload.wikimedia.org/wikipedia/commons/thumb/1/15/SDCC13_-_Ian_McKellen.jpg/1200px-SDCC13_-_Ian_McKellen.jpg", Color.Green),
//            Triple("Robert Downey JRRRRR", "https://upload.wikimedia.org/wikipedia/commons/thumb/1/15/SDCC13_-_Ian_McKellen.jpg/1200px-SDCC13_-_Ian_McKellen.jpg", Color.Green),
//            Triple("Ian McKellen", "https://upload.wikimedia.org/wikipedia/commons/thumb/1/15/SDCC13_-_Ian_McKellen.jpg/1200px-SDCC13_-_Ian_McKellen.jpg", Color.Green)
//        ).filter {
//            it.first.startsWith(search.value, ignoreCase = true)
//        }
//        Column(modifier = Modifier.fillMaxWidth()) {
//            LazyHorizontalGrid(
//                horizontalArrangement = Arrangement.spacedBy(8.dp),
//                verticalArrangement = Arrangement.spacedBy(8.dp),
//                rows = GridCells.Fixed(2),
//                modifier = Modifier
//                    .height(128.dp)
//                    .scrollable(
//                        state = rememberScrollState(),
//                        orientation = Orientation.Horizontal,
//                        overscrollEffect = null
//                    )
//            ) {
//                items(items = items) {
//                    item -> Card(modifier = Modifier.wrapContentHeight()) {
//                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.wrapContentHeight()) {
//                            AsyncImage(
//                                modifier = Modifier
//                                    .size(60.dp),
//                                contentScale = ContentScale.Crop,
//                                model = item.second,
//                                contentDescription = "Profile"
//                            )
//                            Text(text = item.first, modifier = Modifier
//                                .width(160.dp), textAlign = TextAlign.Center)
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//    @Composable
//    fun SearchBar(search: MutableState<String>) {
//        val focusManager = LocalFocusManager.current
//        TextField(
//            value = search.value,
//            onValueChange = {
//                search.value = it
//            },
//            singleLine = true,
//            textStyle = TextStyle(
//                fontSize = 20.sp,
//                color = Color.Blue
//            ),
//            colors = TextFieldDefaults.colors(
//                focusedContainerColor = Color.Red,
//                unfocusedLabelColor = Color.Green
//            ),
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(16.dp, 0.dp, 16.dp, 0.dp)
//                .clip(RoundedCornerShape(16.dp))
//                .border(1.dp, Color.Black, RoundedCornerShape(16.dp)),
//            placeholder = {
//                Text(text = "Search here", color = Color.Cyan, fontSize = 14.sp)
//            },
//            leadingIcon = {
//                Icon(
//                    imageVector = Icons.Default.Search,
//                    contentDescription = "searchIcon",
//                    tint = Color.Cyan,
//                    modifier = Modifier
//                        .width(64.dp)
//                        .height(64.dp)
//                )
//            },
//            keyboardActions = KeyboardActions(
//                onDone = { focusManager.clearFocus() }
//            )
//        )
//    }
//
//    @Composable
//    fun GreetingPreview() {
//        MaterialTheme {
//            Greeting("Boiz")
//        }
//    }
}