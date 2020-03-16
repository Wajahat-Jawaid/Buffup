package com.wajahat.buffdemo.base

import androidx.appcompat.app.AppCompatActivity
import com.wajahat.buffdemo.injection.component.DaggerDaggerInjector
import com.wajahat.buffdemo.injection.component.DaggerInjector
import com.wajahat.buffdemo.injection.module.ActivityModule
import com.wajahat.buffdemo.ui.streamplayer.StreamPlayerActivity
import com.wajahat.buffdemo.ui.streams.StreamListActivity

/**
 * Created by Wajahat Jawaid(wajahatjawaid@gmail.com)
 */
abstract class BaseActivity : AppCompatActivity() {

    /** Initialize Dagger Injector with required dagger modules */
    private val injector: DaggerInjector = DaggerDaggerInjector
        .builder()
        .activityModule(ActivityModule())
        .build()

    /** Called at the time of activity creation */
    init {
        inject()
    }

    /**
     * Injects the required dependencies
     */
    private fun inject() {
        when (this) {
            is StreamListActivity -> injector.inject(this)
            is StreamPlayerActivity -> injector.inject(this)
        }
    }

    /** Abstract methods */
    abstract fun getViewId(): Int

    abstract fun injectViewModel(): BaseViewModel
}