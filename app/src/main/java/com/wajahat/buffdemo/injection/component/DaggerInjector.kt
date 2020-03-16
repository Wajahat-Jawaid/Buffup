package com.wajahat.buffdemo.injection.component

import com.wajahat.buffdemo.injection.module.ActivityModule
import com.wajahat.buffdemo.ui.streamplayer.StreamPlayerActivity
import com.wajahat.buffdemo.ui.streamplayer.StreamPlayerViewModel
import com.wajahat.buffdemo.ui.streams.StreamListActivity
import com.wajahat.buffdemo.ui.streams.StreamListViewModel
import com.wajahat.buffdemo.ui.streams.StreamViewModel
import dagger.Component
import javax.inject.Singleton

/**
 * Component providing inject() methods for presenters.
 */
@Singleton
@Component(modules = [(ActivityModule::class)])
interface DaggerInjector {
    /**
     * Injects required dependencies into the specified ViewModel & BaseActivity
     */
    fun inject(streamListViewModel: StreamListViewModel)
    fun inject(streamListActivity: StreamListActivity)
    fun inject(streamPlayerActivity: StreamPlayerActivity)
    fun inject(streamViewModel: StreamViewModel)
    fun inject(streamPlayerViewModel: StreamPlayerViewModel)

    @Component.Builder
    interface Builder {
        fun build(): DaggerInjector
        fun activityModule(activityModule: ActivityModule): Builder
    }
}