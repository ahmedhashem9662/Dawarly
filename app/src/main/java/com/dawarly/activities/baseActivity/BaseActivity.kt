package com.dawarly.activities.baseActivity

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import com.dawarly.MyApplication
import com.dawarly.activities.about.AboutActivity
import com.dawarly.util.LocaleHelper
import com.dawarly.util.Preferences
import com.example.dawarly.databinding.ActivityBaseBinding


abstract class BaseActivity : AppCompatActivity, BaseViewModel.Observer {

    var isShowAppbar = false
    var isShowBackICon = false
    var isShowActivityName = false
    var isShowAppImage = false
    var activityName = ""
    var isShowMainMenu = false

    constructor(
        isShowAppbar: Boolean,
        isShowBackICon: Boolean,
        isShowAppImage: Boolean,
        isShowActivityName: Boolean,
        activityName: String,
        isShowMainMenu: Boolean
    ) {
        this.isShowAppbar = isShowAppbar
        this.isShowBackICon = isShowBackICon
        this.isShowAppImage = isShowAppImage
        this.isShowActivityName = isShowActivityName
        this.activityName = activityName
        this.isShowMainMenu = isShowMainMenu
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
        baseBinding =
            DataBindingUtil.setContentView(this, com.example.dawarly.R.layout.activity_base)
        baseBinding.viewModel = ViewModelProvider(
            this, BaseViewModelFactory(application)
        ).get(BaseViewModel::class.java)
        baseBinding.viewModel!!.baseObserver = this
        baseBinding.lifecycleOwner = this


        setAppBar()
        onBack()
        doCreate()
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
            layoutInflater, layoutID, baseBinding.layoutOtherContent, true
        )
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
        baseBinding.viewModel!!.isShowMainMenu.value = isShowMainMenu
    }

    override fun onBack() {
        baseBinding.goBack.setOnClickListener(View.OnClickListener {
            onBackPressed()
        })
    }

    override fun onMenuClicked() {
        var pm = PopupMenu(this, baseBinding.mainMenu)
        menuInflater.inflate(com.example.dawarly.R.menu.main_menu, pm.menu)
        pm.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                com.example.dawarly.R.id.menu -> {
                    val intent = Intent(this, AboutActivity::class.java)
                    startActivity(intent)
                }
            }
            true
        }
        pm.show()

    }

}




