package com.dawarly.activities.about

import androidx.lifecycle.ViewModelProvider
import com.dawarly.activities.baseActivity.BaseActivity
import com.example.dawarly.R
import com.example.dawarly.databinding.ActivityAboutMapBinding

abstract class AboutActivity : BaseActivity(
    true, true,true, true, "About Dawarly"
), AboutViewModel.Observer {

    lateinit var binding: ActivityAboutMapBinding

    override fun doCreate() {
        binding = putContentView(R.layout.activity_about_map) as ActivityAboutMapBinding
        binding.viewModel = ViewModelProvider(
            this, AboutViewModelFactory(this.application)
        ).get(AboutViewModel::class.java)
        binding.viewModel!!.observer = this
        binding.lifecycleOwner = this
    }
}

