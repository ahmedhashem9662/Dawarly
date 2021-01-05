package com.dawarly.activities.baseActivity

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import com.dawarly.MyApplication
import com.dawarly.activities.about.AboutActivity
import com.dawarly.util.LocaleHelper
import com.dawarly.util.Preferences
import com.example.dawarly.R
import com.example.dawarly.databinding.ActivityBaseBinding

abstract class BaseActivity : AppCompatActivity, BaseViewModel.Observer {

    var isShowAppbar = false
    var isShowBackICon = false
    var isShowActivityName = false
    var isShowAppImage = false
    var activityName = ""

    constructor(
        isShowAppbar: Boolean,
        isShowBackICon: Boolean,
        isShowAppImage: Boolean,
        isShowActivityName: Boolean,
        activityName: String )

    {
        this.isShowAppbar = isShowAppbar
        this.isShowBackICon = isShowBackICon
        this.isShowAppImage = isShowAppImage
        this.isShowActivityName = isShowActivityName
        this.activityName = activityName
    }

    lateinit var baseBinding: ActivityBaseBinding
    lateinit var application: MyApplication

    fun updateLocale() {
        if (Preferences.getApplicationLocale().compareTo("en") == 0) {
            forceRTL()
        } else {
            forceLTR()
        }
        // Update the locale here before loading the layout to get localized strings.
        LocaleHelper.updateLocale(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        updateLocale()
        application = getApplication() as MyApplication
        baseBinding = DataBindingUtil.setContentView(this, R.layout.activity_base)
        baseBinding.viewModel = ViewModelProvider(
            this, BaseViewModelFactory(application)
        ).get(BaseViewModel::class.java)
        baseBinding.viewModel!!.baseObserver = this
        baseBinding.lifecycleOwner = this

        setAppBar()
        onBack()
        doCreate()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (item.itemId) {
            R.id.menu -> {
                val intent = Intent(this, AboutActivity::class.java)
                this.startActivity(intent)
            }
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

    override fun applyOverrideConfiguration(overrideConfiguration: Configuration?) {
        super.applyOverrideConfiguration(
            LocaleHelper.applyOverrideConfiguration(
                baseContext,
                overrideConfiguration
            )
        )
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LocaleHelper.updateLocale(newBase))
    }

    open fun putContentView(layoutID: Int): ViewDataBinding? {
        return DataBindingUtil.inflate(
            layoutInflater, layoutID, baseBinding.layoutOtherContent, true)
    }

    abstract fun doCreate()

    fun forceRTL() {
        window.decorView.layoutDirection = View.LAYOUT_DIRECTION_RTL
    }

    fun forceLTR() {
        window.decorView.layoutDirection = View.LAYOUT_DIRECTION_LTR
    }

    override fun setAppBar() {
        baseBinding.viewModel!!.isShowAppBar.value = isShowAppbar
        baseBinding.viewModel!!.isShowBackIcon.value = isShowBackICon
        baseBinding.viewModel!!.isShowAppImage.value = isShowAppImage
        baseBinding.viewModel!!.isShowActivityName.value = isShowActivityName
        baseBinding.viewModel!!.activityName.value = activityName
    }

    override fun onBack() {
        baseBinding.goBack.setOnClickListener(View.OnClickListener {
            onBackPressed()
        })
    }
}




