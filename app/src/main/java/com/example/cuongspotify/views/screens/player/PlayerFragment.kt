package com.example.cuongspotify.views.screens.player

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import androidx.compose.foundation.interaction.DragInteraction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.fragment.findNavController
import com.example.cuongspotify.R
import com.example.cuongspotify.views.screens.home.HomeViewModal
import com.example.cuongspotify.views.screens.home.MusicStateViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cuongspotify.configs.extensions.convertMillis
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlayerFragment : Fragment() {

    private val viewModel: MusicStateViewModel by activityViewModels()

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
                PlayerScreen(viewModel)
            }
        }
    }

    fun sendSeekToEventToService(seekTo: Int) {
        val serviceIntent = Intent(requireContext(), MusicPlayerService::class.java).apply {
            putExtra(MusicPlayerService.INTENT_KEY_COMMAND, MusicPlayerService.ServiceCommand.SEEK_TO.value)
            putExtra(MusicPlayerService.SERVICE_PARAMS_SEEK_TO, seekTo)
        }
        requireContext().startService(serviceIntent)
    }

    @Preview(showBackground = true)
    @Composable
    fun PlayerScreen(musicViewModel: MusicStateViewModel = viewModel()) {
        val isRotating by musicViewModel.isPlaying.collectAsStateWithLifecycle()
        val duration by musicViewModel.duration.collectAsStateWithLifecycle()
        val progress by musicViewModel.progress.collectAsStateWithLifecycle()
        val rotationAngle = remember { Animatable(0f) }

        LaunchedEffect(isRotating) {
            Log.d("DEBUG", "Recomposed with isRotating = $isRotating")
        }

        LaunchedEffect(isRotating) {
            if (isRotating) {
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
                    TrackProgress(progress, duration)
                    PlayerControl(isRotating)
                }
            }
        )
    }

    @Composable
    fun PlayerControl(isRotating: Boolean) {
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
                checked = isRotating,
                onCheckedChange = {
                    val serviceIntent = Intent(requireContext(), MusicPlayerService::class.java)
                    serviceIntent.putExtra(
                        MusicPlayerService.INTENT_KEY_COMMAND,
                        if (!isRotating) MusicPlayerService.ServiceCommand.RESUME.value else MusicPlayerService.ServiceCommand.PAUSE.value
                    )
                    requireContext().startService(serviceIntent)
                },
                modifier = Modifier.size(64.dp)
            ) {
                Icon(
                    painter = if (isRotating) painterResource(id = R.drawable.baseline_pause_24) else painterResource(
                        id = R.drawable.baseline_play_arrow_24
                    ),
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
                Icon(
                    painter = it,
                    contentDescription = contentDescription,
                    modifier = Modifier.size(size ?: 32.dp)
                )
            } ?: imageVector?.let {
                Icon(
                    it,
                    contentDescription = contentDescription,
                    modifier = Modifier.size(size ?: 32.dp)
                )
            }
        }
    }

    @Composable
    fun TrackNameAndArtist() {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 16.dp, 16.dp, 0.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "Mùa mưa ấy", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text(text = "Vũ.", fontSize = 16.sp)
            }
            IconButton(modifier = Modifier.size(24.dp), onClick = { /*TODO*/ }) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_add_circle_outline_24),
                    contentDescription = "Add icon"
                )
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun TrackProgress(progress: Int, duration: Int) {
        val interactionSource = remember { MutableInteractionSource() }
        var sliderPosition by remember { mutableFloatStateOf(0f) }
        var onDragged by remember { mutableStateOf(false) }

        LaunchedEffect(progress, duration) {
            sliderPosition = if (onDragged) sliderPosition
            else if (duration == 0) 0f
            else (progress.toFloat() / duration.toFloat())
        }

        LaunchedEffect(interactionSource, duration) {
            Log.d("stop::interactionSource", duration.toString())
            interactionSource.interactions.collect { interaction ->
                when (interaction) {
                    is DragInteraction.Start -> {
                        onDragged = true
                    }

                    is DragInteraction.Stop -> {
                        Log.d("stop::", sliderPosition.toString())
                        Log.d("stop::duration", duration.toString())
                        Log.d("stop::durationx", (sliderPosition * duration).toString())
                        sendSeekToEventToService((sliderPosition * duration).toInt())
                        CoroutineScope(Dispatchers.Main).launch {
                            delay(300)
                            onDragged = false
                        }
                    }
                }
            }
        }

        Column(modifier = Modifier.padding(8.dp, 0.dp, 8.dp, 0.dp)) {
            Slider(
                value = sliderPosition,
                onValueChange = { sliderPosition = it },
                colors = SliderDefaults.colors(
                    thumbColor = MaterialTheme.colorScheme.secondary,
                    activeTrackColor = MaterialTheme.colorScheme.secondary,
                    inactiveTrackColor = MaterialTheme.colorScheme.secondaryContainer,
                ),
                valueRange = 0f..1f,
                interactionSource = interactionSource
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp, 4.dp, 8.dp, 0.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = if (onDragged) (sliderPosition * duration).toInt().convertMillis() else progress.convertMillis())
                Text(text = duration.convertMillis())
            }
        }
    }
}