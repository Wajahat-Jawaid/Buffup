package com.wajahat.buffdemo.ui.streams

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxbinding.view.RxView
import com.wajahat.buffdemo.R
import com.wajahat.buffdemo.databinding.ItemStreamBinding
import com.wajahat.buffup.model.stream.Stream
import rx.subjects.PublishSubject

/**
 * Created by Wajahat Jawaid(wajahatjawaid@gmail.com)
 */
class StreamsListAdapter : RecyclerView.Adapter<StreamsListAdapter.ViewHolder>() {

    private lateinit var streamList: List<Stream>
    val streamClickSubject: PublishSubject<Stream> = PublishSubject.create<Stream>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemStreamBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_stream, parent, false
        )
        RxView.clicks(binding.root)
            .takeUntil(RxView.detaches(parent))
            .map { binding }
            .subscribe { streamClickSubject.onNext(binding.stream) }
        return ViewHolder(binding)
    }

    /** Update Streams list when data is manipulated */
    fun updateStreamList(streamList: List<Stream>) {
        this.streamList = streamList
        notifyDataSetChanged()
    }

    override fun getItemCount() = if (::streamList.isInitialized) streamList.size else 0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(streamList[position])
    }

    class ViewHolder(private val binding: ItemStreamBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val viewModel = StreamViewModel()

        fun bind(stream: Stream) {
            viewModel.bind(stream)
            binding.stream = stream
            binding.viewModel = viewModel
        }
    }
}