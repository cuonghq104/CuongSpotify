package com.example.cuongspotify.views.screens.player

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilledIconToggleButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cuongspotify.R
import com.example.cuongspotify.views.screens.home.MusicStateViewModel

@Preview(showBackground = true)
@Composable
fun MiniPlayerComposeView(musicViewModel: MusicStateViewModel = viewModel()) {
    val context = LocalContext.current
    val isRotating by musicViewModel.isPlaying.collectAsStateWithLifecycle()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(WindowInsets.systemBars.asPaddingValues())
            .clip(RoundedCornerShape(12.dp))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.test),
                contentDescription = "test",
                modifier = Modifier
                    .width(48.dp)
                    .height(48.dp),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp, 0.dp, 0.dp, 0.dp)
            ) {
                Text(text = "Em như dòng nước trong veo", maxLines = 1)
                Text(text = "Bruno Mars", maxLines = 1)
            }
            IconToggleButton(
                checked = isRotating,
                onCheckedChange = {
                    val serviceIntent = Intent(context, MusicPlayerService::class.java)
                    serviceIntent.putExtra(
                        MusicPlayerService.INTENT_KEY_COMMAND,
                        if (!isRotating) MusicPlayerService.ServiceCommand.RESUME.value else MusicPlayerService.ServiceCommand.PAUSE.value
                    )
                    context.startService(serviceIntent)
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
        }
    }
}