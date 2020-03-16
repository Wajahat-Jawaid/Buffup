package com.wajahat.buffdemo.base

import androidx.lifecycle.ViewModel
import com.wajahat.buffdemo.injection.component.DaggerDaggerInjector
import com.wajahat.buffdemo.injection.component.DaggerInjector
import com.wajahat.buffdemo.injection.module.ActivityModule
import com.wajahat.buffdemo.ui.streamplayer.StreamPlayerViewModel
import com.wajahat.buffdemo.ui.streams.StreamListViewModel
import com.wajahat.buffdemo.ui.streams.StreamViewModel

/**
 * Created by Wajahat Jawaid(wajahatjawaid@gmail.com)
 */
abstract class BaseViewModel : ViewModel() {

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
            is StreamListViewModel -> injector.inject(this)
            is StreamViewModel -> injector.inject(this)
            is StreamPlayerViewModel -> injector.inject(this)
        }
    }
}