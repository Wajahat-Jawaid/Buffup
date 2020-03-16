package com.wajahat.buffdemo.ui.streamplayer

import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.MediaController
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.wajahat.buffdemo.R
import com.wajahat.buffdemo.base.BaseActivity
import com.wajahat.buffdemo.databinding.ActivityStreamPlayerBinding
import com.wajahat.buffdemo.injection.ViewModelFactory
import com.wajahat.buffdemo.utils.Constants
import com.wajahat.buffdemo.utils.DialogUtils
import com.wajahat.buffup.events.OnBuffAnswerListener
import com.wajahat.buffup.exception.BuffControlMissingException
import com.wajahat.buffup.exception.InvalidActivityViewGroupException
import com.wajahat.buffup.model.stream.Stream
import com.wajahat.buffup.model.stream.StreamDetails
import com.wajahat.buffup.utils.ViewUtils
import javax.inject.Inject

/**
 * Created by Wajahat Jawaid(wajahatjawaid@gmail.com)
 */
class StreamPlayerActivity : BaseActivity(), OnBuffAnswerListener {

    @Inject
    protected lateinit var dialogUtils: DialogUtils

    private lateinit var viewModel: StreamPlayerViewModel
    private lateinit var binding: ActivityStreamPlayerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        validateIntent()
        binding = DataBindingUtil.setContentView(this, getViewId())

        viewModel = ViewModelProviders.of(this, ViewModelFactory())
            .get(StreamPlayerViewModel::class.java)
        binding.viewModel = viewModel
        viewModel.fetchStreamDetails(stream)
        viewModel.streamDetails.observe(this, Observer {
            setVideoView(it)
        })
    }

    /** Validate Intent to check if we have valid stream data whose details we are fetching */
    private fun validateIntent() {
        if (intent == null) {
            finish()
        }
        stream = intent.getParcelableExtra(Constants.SELECTED_STREAM)
    }

    /** Once StreamDetails are fetched, set the VideoView
     * Note: For the streaming view, the below implementation is not enough. Javascript enable,
     * and many other advanced configs are required
     * */
    private fun setVideoView(streamDetails: StreamDetails) {
        try {
            val videoView = binding.videoView
            val videoUri = Uri.parse(stream.streamVideoUrl)
            videoView.setVideoURI(videoUri)
            // Setting Media Controls
            val mediaController = MediaController(this)
            mediaController.setAnchorView(videoView)
            videoView.setMediaController(mediaController)

            binding.videoView.setOnPreparedListener {
                binding.videoView.start()
                it.setOnInfoListener { mediaPlayer, i, i1 ->
                    // Don't set the controls when video is ready. This is not good from the
                    // UX aspect because sometimes it takes longer for the frames to show. We
                    // have to show controls once frames are ready

                    if (i == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
                        // Hide progress bar
                        hideProgressBar()

                        // Show BuffView after 6 seconds
                        Handler().postDelayed({ showBuffView(streamDetails) }, BUFF_VIEW_SHOW_DELAY)
                    }
                    false
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun showBuffView(streamDetails: StreamDetails) {
        val controller = binding.buffView
        try {
            controller.showBuffControls(this, this, streamDetails)
        } catch (e: InvalidActivityViewGroupException) {
            e.printStackTrace()
            dialogUtils.showSimpleDialog(
                this, getString(R.string.error_viewgroup), e.message!!
            )
        } catch (e: BuffControlMissingException) {
            e.printStackTrace()
            dialogUtils.showSimpleDialog(
                this, getString(R.string.error_buff_player), e.message!!
            )
        }
    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.GONE
    }

    override fun getViewId() = R.layout.activity_stream_player
    override fun injectViewModel(): StreamPlayerViewModel {
        return ViewModelProviders.of(this, ViewModelFactory())
            .get(StreamPlayerViewModel::class.java)
    }

    /** When a buff answer is selected, this call back method is fired
     * @param answer Selected answer from list
     * */
    override fun onBuffAnswerSelected(answer: StreamDetails.Answer) {
        ViewUtils.showToast(
            this,
            "Your answer ${answer.title} has been submitted",
            true
        )
    }

    companion object {
        private lateinit var stream: Stream
        private const val BUFF_VIEW_SHOW_DELAY = 6000L
    }
}