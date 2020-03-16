package com.wajahat.buffdemo.ui.streams

import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.wajahat.buffdemo.R
import com.wajahat.buffdemo.base.BaseActivity
import com.wajahat.buffdemo.databinding.ActivityStreamsBinding
import com.wajahat.buffdemo.injection.ViewModelFactory
import com.wajahat.buffdemo.ui.streamplayer.StreamPlayerActivity
import com.wajahat.buffdemo.utils.Constants

/**
 * Created by Wajahat Jawaid(wajahatjawaid@gmail.com)
 */
class StreamListActivity : BaseActivity() {

    private lateinit var binding: ActivityStreamsBinding
    private lateinit var viewModel: StreamListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, getViewId())

        binding.streamList.layoutManager = LinearLayoutManager(this)
        viewModel = injectViewModel()
        viewModel.streamClickSubject.observe(this, Observer {
            val intent = Intent(this, StreamPlayerActivity::class.java)
            intent.putExtra(Constants.SELECTED_STREAM, it)
            startActivity(intent)
        })
        binding.viewModel = viewModel
    }

    override fun getViewId() = R.layout.activity_streams
    override fun injectViewModel(): StreamListViewModel {
        return ViewModelProviders.of(this, ViewModelFactory())
            .get(StreamListViewModel::class.java)
    }
}