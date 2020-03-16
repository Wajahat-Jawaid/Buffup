package com.wajahat.buffdemo.ui.streams

import androidx.lifecycle.MutableLiveData
import com.wajahat.buffdemo.base.BaseViewModel
import com.wajahat.buffup.model.stream.Stream
import com.wajahat.buffup.model.stream.StreamResponseImageHolder

/**
 * Created by Wajahat Jawaid(wajahatjawaid@gmail.com)
 */
class StreamViewModel : BaseViewModel() {

    private val id = MutableLiveData<Int>()
    private val title = MutableLiveData<String>()
    private val description = MutableLiveData<String>()
    private val streamVideoUrl = MutableLiveData<String>()
    private val image = MutableLiveData<StreamResponseImageHolder>()

    fun bind(stream: Stream) {
        id.value = stream.id
        title.value = stream.title
        description.value = stream.description
        streamVideoUrl.value = stream.streamVideoUrl
        image.value = stream.image
    }

    fun getId() = id
    fun getTitle() = title
    fun getDescription() = description
    fun getStreamVideoUrl() = streamVideoUrl
    fun getImages() = image
}