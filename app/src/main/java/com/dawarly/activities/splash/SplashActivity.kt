package com.dawarly.activities.splash

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import com.dawarly.activities.map.MapsActivity
import com.dawarly.activities.baseActivity.BaseActivity
import com.example.dawarly.R
import com.example.dawarly.databinding.ActivitySplashBinding

class SplashActivity : BaseActivity(
    false, false, false, false, "" , false
), SplashViewModel.Observer {

    lateinit var binding: ActivitySplashBinding

    override fun doCreate() {
        binding = putContentView(R.layout.activity_splash) as ActivitySplashBinding
        binding.viewModel = ViewModelProvider(
            this, SplashViewModelFactory(this.application)
        ).get(SplashViewModel::class.java)
        binding.viewModel!!.observer = this
        binding.lifecycleOwner = this

        binding.viewModel!!.startWaitingTimer()
    }

    override fun openNextScreen() {
        val mainIntent = Intent(this@SplashActivity, MapsActivity::class.java)
        this@SplashActivity.startActivity(mainIntent)
        finish()
    }
}



