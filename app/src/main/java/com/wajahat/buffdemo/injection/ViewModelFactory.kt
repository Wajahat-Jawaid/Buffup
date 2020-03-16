package com.wajahat.buffdemo.injection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.wajahat.buffdemo.ui.streamplayer.StreamPlayerViewModel
import com.wajahat.buffdemo.ui.streams.StreamListViewModel

/**
 * Created by Wajahat Jawaid(wajahatjawaid@gmail.com)
 */
class ViewModelFactory : ViewModelProvider.Factory {

    /** A Factory to build and then manage the ViewModels. Though it's not mandatory, but
     * in order to avoid multiple creations of ViewModel on every activity configuration
     * change, Factory is provided */
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StreamListViewModel::class.java)) {
            return StreamListViewModel() as T
        }
        if (modelClass.isAssignableFrom(StreamPlayerViewModel::class.java)) {
            return StreamPlayerViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}