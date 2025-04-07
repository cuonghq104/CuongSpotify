package com.example.cuongspotify

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.cuongspotify.databinding.ActivityMainBinding
import com.example.cuongspotify.views.screens.auth.AuthActivity
import com.example.cuongspotify.views.screens.home.HomeViewModal
import com.example.cuongspotify.views.screens.home.MusicStateViewModel
import com.example.cuongspotify.views.screens.player.MusicPlayerService

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding?= null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModal by viewModels()
    private val musicViewModel: MusicStateViewModel by viewModels()

    private lateinit var toggle: ActionBarDrawerToggle

    private var musicStateReceiver = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            val action = p1?.getStringExtra(MusicPlayerService.BROADCAST_PARAMS_ACTION)
            when(action) {
                MusicPlayerService.BroadcastAction.UPDATE_PLAY_STATE.value -> {
                    val isPlaying = p1.getBooleanExtra(MusicPlayerService.BROADCAST_PARAMS_MUSIC_IS_PLAYING, false)
                    musicViewModel.setPlayingState(isPlaying)
                }
                MusicPlayerService.BroadcastAction.UPDATE_DURATION.value -> {
                    val duration = p1.getIntExtra(MusicPlayerService.BROADCAST_PARAMS_DURATION, 0)
                    musicViewModel.setDuration(duration)
                }
                MusicPlayerService.BroadcastAction.UPDATE_PROGRESS.value -> {
                    val progress = p1.getIntExtra(MusicPlayerService.BROADCAST_PARAMS_PROGRESS, 0)
                    musicViewModel.setProgress(progress)
                }
            }
        }
    }

    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val data: Intent? = result.data
            val action = data?.getStringExtra("action")
            if (action == "refresh") {
                binding.dlContainer.closeDrawer(GravityCompat.START)
                viewModel.fetchMusicTrack(this@MainActivity)
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupUI()
        addListener()

        subscribeToBroadcast()
    }

    private fun subscribeToBroadcast() {
        val filter = IntentFilter("com.example.snippets.ACTION_UPDATE_DATA")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalBroadcastManager.getInstance(this@MainActivity).registerReceiver(musicStateReceiver, filter)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(this@MainActivity).unregisterReceiver(musicStateReceiver)
    }

    private fun addListener() {
        with(binding) {
            tbHome.setNavigationOnClickListener {
                if (dlContainer.isDrawerOpen(GravityCompat.START)) {
                    dlContainer.closeDrawer(GravityCompat.START)
                } else {
                    dlContainer.openDrawer(GravityCompat.START)
                }
            }

            nvHome.setNavigationItemSelectedListener { item ->
                when(item.itemId) {
                    R.id.nav_item_log_in -> {
                        val intent = AuthActivity.getInstance(this@MainActivity)
                        resultLauncher.launch(intent)
                        true
                    }
                }
                false
            }
        }
    }

    private fun setupUI() {
        with(binding) {
            ViewCompat.setOnApplyWindowInsetsListener(dlContainer) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }
        }
        toolbarConfig()
    }

    private fun toolbarConfig() {
        with(binding) {
            setSupportActionBar(tbHome)
            toggle = ActionBarDrawerToggle(this@MainActivity, dlContainer, tbHome, R.string.log_in, R.string.log_in)
            toggle.isDrawerIndicatorEnabled = true
            dlContainer.addDrawerListener(toggle)
            // Toolbar icon
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            toggle.isDrawerIndicatorEnabled = false
            tbHome.navigationIcon = ContextCompat.getDrawable(this@MainActivity, R.drawable.baseline_menu_24)
            tbHome.navigationIcon?.setTint(ContextCompat.getColor(this@MainActivity, R.color.white))
            tbHome.setTitleTextColor(ContextCompat.getColor(this@MainActivity, R.color.white))

            supportActionBar?.setHomeButtonEnabled(true)
        }
    }
}