package com.wajahat.buffup.views

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Handler
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils.loadAnimation
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.view.children
import com.bumptech.glide.Glide
import com.jakewharton.rxbinding.view.RxView
import com.wajahat.buffup.R
import com.wajahat.buffup.events.OnBuffAnswerListener
import com.wajahat.buffup.exception.BuffControlMissingException
import com.wajahat.buffup.exception.InvalidActivityViewGroupException
import com.wajahat.buffup.model.stream.StreamDetails
import com.wajahat.buffup.utils.StringUtils
import com.wajahat.buffup.utils.ViewUtils
import douglasspgyn.com.github.circularcountdown.CircularCountdown
import douglasspgyn.com.github.circularcountdown.listener.CircularListener

/**
 * Created by Wajahat Jawaid(wajahatjawaid@gmail.com)
 */
class BuffView(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {

    /** To dynamically manage alignment, view configuration of answer row views */
    private lateinit var answerRowLayoutParams: LayoutParams
    /** List index for answers to show them in the form of alphabets. Logic is made with
     * ASCII tracking. Making it volatile so as it behaves correctly across different threads
     * Index starts with 65 as ASCII for 65 is 'A'
     * */
    @Volatile
    private var listAlphabeticalIndex = 65

    /** Initialize the BuffView */
    private val buffView: LinearLayout =
        View.inflate(context, R.layout.buff_view, this) as LinearLayout

    // Count down timer after which an alert like view is shown countdown
    private var countdownTimerAlertDuration = DEFAULT_COUNTDOWN_TIMER_ALERT_DURATION
    // countdown timer view enlarged scale
    private var countdownTimerAlertViewZoomedScale = DEFAULT_COUNTDOWN_TIMER_ALERT_VIEW_ZOOMED_SCALE
    // View properties
    private var authorInfoBackground: Drawable? = null
    private var answersCounterTextBackground: Drawable? = null
    private var questionBackground: Drawable? = null
    private var answerRowBackground: Drawable? = null
    private var answerRowIndexBackground: Drawable? = null
    private var closeIcon: Drawable? = null
    private var authorInfoTextColor: Int
    private var answersCounterTextColor: Int
    private var questionTextColor: Int
    private var answerRowIndexTextColor: Int
    private var answerRowTextColor: Int
    private var shouldAlertBeforeEnding: Boolean

    init {
        val attributes =
            context.obtainStyledAttributes(attrs, R.styleable.BuffView)

        // Initialize buff custom attributes
        // Drawables
        val authorInfoBackgroundId =
            attributes.getResourceId(
                R.styleable.BuffView_buff_author_info_bg,
                -1
            )
        authorInfoBackground = if (authorInfoBackgroundId == -1)
            ContextCompat.getDrawable(
                context,
                R.drawable.questionnaire_author_layout_bg
            )
        else
            attributes.getDrawable(R.styleable.BuffView_buff_author_info_bg)!!

        val answersCounterTextBackgroundId =
            attributes.getResourceId(
                R.styleable.BuffView_buff_answers_counter_text_bg,
                -1
            )
        answersCounterTextBackground = if (answersCounterTextBackgroundId == -1)
            ContextCompat.getDrawable(
                context,
                R.drawable.questionnaire_answers_counter_bg
            )
        else
            attributes.getDrawable(R.styleable.BuffView_buff_answers_counter_text_bg)!!

        val questionBackgroundId =
            attributes.getResourceId(R.styleable.BuffView_buff_question_bg, -1)
        questionBackground = if (questionBackgroundId == -1)
            ContextCompat.getDrawable(
                context,
                R.drawable.questionnaire_question_layout_bg
            )
        else
            attributes.getDrawable(R.styleable.BuffView_buff_question_bg)!!

        val answerRowBackgroundId =
            attributes.getResourceId(R.styleable.BuffView_buff_answer_row_bg, -1)
        answerRowBackground = if (answerRowBackgroundId == -1)
            ContextCompat.getDrawable(
                context,
                R.drawable.questionnaire_answers_layout_bg
            )
        else
            attributes.getDrawable(R.styleable.BuffView_buff_answer_row_bg)!!

        val answerRowIndexBackgroundId =
            attributes.getResourceId(
                R.styleable.BuffView_buff_answer_row_index_bg,
                -1
            )
        answerRowIndexBackground = if (answerRowIndexBackgroundId == -1)
            ContextCompat.getDrawable(
                context,
                R.drawable.questionnaire_answer_index_bg
            )
        else
            attributes.getDrawable(R.styleable.BuffView_buff_answer_row_index_bg)!!

        val closeIconId =
            attributes.getResourceId(R.styleable.BuffView_buff_close_icon, -1)
        closeIcon = if (closeIconId == -1)
            ContextCompat.getDrawable(context, R.drawable.ic_close_buff)
        else
            attributes.getDrawable(R.styleable.BuffView_buff_close_icon)!!

        // Colors
        authorInfoTextColor = attributes.getColor(
            R.styleable.BuffView_buff_author_info_text_color,
            ContextCompat.getColor(context, defaultDarkTextColor)
        )
        answersCounterTextColor = attributes.getColor(
            R.styleable.BuffView_buff_answers_counter_text_color,
            ContextCompat.getColor(context, DEFAULT_TEXT_COLOR_LIGHT)
        )
        questionTextColor = attributes.getColor(
            R.styleable.BuffView_buff_question_text_color,
            ContextCompat.getColor(context, DEFAULT_TEXT_COLOR_LIGHT)
        )
        answerRowIndexTextColor = attributes.getColor(
            R.styleable.BuffView_buff_answer_row_index_text_color,
            ContextCompat.getColor(context, DEFAULT_TEXT_COLOR_LIGHT)
        )
        answerRowTextColor = attributes.getColor(
            R.styleable.BuffView_buff_answer_row_text_color,
            ContextCompat.getColor(context, defaultDarkTextColor)
        )

        // Primitives
        shouldAlertBeforeEnding = attributes.getBoolean(
            R.styleable.BuffView_buff_should_alert_before_ending, false
        )
        countdownTimerAlertDuration = attributes.getInt(
            R.styleable.BuffView_buff_countdown_timer_alert_duration,
            DEFAULT_COUNTDOWN_TIMER_ALERT_DURATION
        )
        countdownTimerAlertViewZoomedScale = attributes.getFloat(
            R.styleable.BuffView_buff_countdown_timer_alert_view_zoomed_scale,
            DEFAULT_COUNTDOWN_TIMER_ALERT_VIEW_ZOOMED_SCALE
        )

        attributes.recycle()
    }

    /* VIEWS SETUP STARTS */
    /** Dynamically add the buff view over an activity. Since overlapping in practice should
     * be done on the ViewGroup only which support overlapping like RelativeLayout or so whereas
     * ViewGroup like LinearLayout does not. So we first check that whether the ViewGroup i.e.
     * parent view supports overlapping.
     * @param activity Activity to host on the buff controls
     * @param buffAnswerListener OnBuffAnswerListener to monitor answer click event
     * @param streamDetails StreamDetails containing buff information
     * */
    @Throws(BuffControlMissingException::class, InvalidActivityViewGroupException::class)
    fun showBuffControls(
        activity: Activity,
        buffAnswerListener: OnBuffAnswerListener,
        streamDetails: StreamDetails
    ) {
        if (!validateStreamDetails(streamDetails))
            return
        // We can use objects from #streamDetails safely now as this point is reached if every
        // buff object is non-null

        // Checking ViewGroup type to verify it supports BuffView
        val viewGroup = (activity
            .findViewById(android.R.id.content) as ViewGroup).getChildAt(0) as ViewGroup
        if (viewGroup is RelativeLayout) {
            applyParentViewConfiguration(activity)

            // Set Buff Header
            setHeaderView()

            // Set Author Details
            setAuthorView(streamDetails.author!!)

            // Set Question Details
            setQuestionView(streamDetails.question!!)

            // Set Question Details
            setCountDownTimerView(streamDetails.timeToShow)

            // Set Answer Details
            setAnswersView(streamDetails.answers!!, buffAnswerListener)
        } else {
            throw InvalidActivityViewGroupException(
                Throwable(context.getString(R.string.only_relative_layout_supported))
            )
        }
    }

    /** Configure alignment parameters of the buffView so that it behaves as expected on any
     * given view
     * @param activity Activity on which buffView to host on
     * */
    private fun applyParentViewConfiguration(activity: Activity) {
        // As demonstrated in the sample snapshot, I've set arbitrary width to buffView which
        // is 40% of the screen width thereby dynamically adjusting to device of any given
        // screen resolution and width
        val width = ViewUtils.getBuffContainerWidth(activity)
        val viewParams = RelativeLayout.LayoutParams(
            width, RelativeLayout.LayoutParams.WRAP_CONTENT
        )
        // Apply consistent margin from each border
        val margin = resources.getDimension(R.dimen.buffLayoutMargin).toInt()
        // Place the view down to the left bottom
        viewParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 1)
        viewParams.setMargins(margin, margin, margin, margin)
        // Finally apply configuration to the buff's root view
        buffView.layoutParams = viewParams
    }

    /** Configures the buffView's header which contains author info, answers counter and the
     * close button
     * */
    private fun setHeaderView() {
        val headerView = buffView.findViewById<RelativeLayout>(R.id.header_view)
        // Close ImageButton
        val closeImageButton =
            headerView.findViewById<ImageButton>(R.id.close_buff_icon)
        closeImageButton.setImageDrawable(closeIcon)
        RxView.clicks(closeImageButton).subscribe { closeQuestionnaire() }

        headerView.visibility = View.VISIBLE
        val animation = loadAnimation(buffView.context, R.anim.slide_down)
        headerView.startAnimation(animation)
    }

    /** Set the Author View and add it with animation into root view
     * @param author Author Details obtained by StreamDetails
     * */
    private fun setAuthorView(author: StreamDetails.Author) {
        // Null-safe string interpolation
        val authorFirstName = author.firstName ?: ""
        val authorLastName = author.lastName ?: ""
        val authorName = "$authorFirstName $authorLastName"

        val authorTextView = buffView.findViewById<TextView>(R.id.author_text)
        authorTextView.text = authorName
        authorTextView.setTextColor(authorInfoTextColor)

        // Author Image
        Glide.with(buffView)
            .load(author.image)
            .placeholder(R.drawable.default_author)
            .into(buffView.findViewById(R.id.author_image))

        // Set Author View Background
        buffView.findViewById<LinearLayout>(R.id.author_info_container)
            .background = authorInfoBackground
    }

    /** Set the Question View and add it with animation into root view
     * @param question Question obtained by StreamDetails
     * */
    private fun setQuestionView(question: StreamDetails.Question) {
        val questionTextView =
            buffView.findViewById<TextView>(R.id.question_text)
        questionTextView.text = question.title
        questionTextView.setTextColor(questionTextColor)

        val questionLayout =
            buffView.findViewById<RelativeLayout>(R.id.question_layout)
        questionLayout.background = questionBackground
        questionLayout.visibility = View.VISIBLE

        val animation = loadAnimation(buffView.context, R.anim.slide_right)
        questionLayout.startAnimation(animation)
    }

    private lateinit var timerView: CircularCountdown

    /** Set the countdown timer view for a buff. Once timer i.e. seconds is over, it fires
     * #closeQuestionnaire() to close buffView
     * */
    private fun setCountDownTimerView(seconds: Int) {
        timerView = buffView.findViewById(R.id.circular_progress)

        // Disable repeated iterations
        timerView.disableLoop()

        // Rectifying wrong inputs by the user to ensure that even unexpected values
        // don't disturb the buffView
        rectifyPossibleCountdownTimerAlertSeconds(seconds)
        rectifyPossibleCountdownTimerAlertViewZoomedScale()

        timerView
            .create(1, seconds, CircularCountdown.TYPE_SECOND)
            .listener(object : CircularListener {
                override fun onTick(progress: Int) {
                    // Check if end alerting is enabled by user or not.
                    // If yes, then check how much time is left to start alerting
                    if (shouldAlertBeforeEnding
                        &&
                        (progress > (seconds - (countdownTimerAlertDuration + 1)))
                    ) {
                        // Scale up view for #COUNTDOWN_ALERT_VIEW_SCALE_DURATION duration
                        ViewUtils.scaleView(timerView, countdownTimerAlertViewZoomedScale)
                        Handler().postDelayed({
                            ViewUtils.scaleView(timerView, 1.0f)
                        }, COUNTDOWN_ALERT_VIEW_SCALE_DURATION)
                    }
                    // If the last second is ticked, close questionnaire
                    if (progress == seconds - 1) {
                        // Still wait for another second because now been 1 second is left
                        // whereas the timer won't tick again once this condition is reached
                        Handler().postDelayed({ closeQuestionnaire() }, 1000)
                    }
                }

                override fun onFinish(newCycle: Boolean, cycleCount: Int) {
                }
            })
            .start()
    }

    /** Called either by the user by clicking #closeImageButton or when timer has finished.
     * Hide the buffView now
     * */
    private fun closeQuestionnaire() {
        disableBuffView()
        buffView.visibility = View.GONE
        val animation = loadAnimation(buffView.context, R.anim.slide_left)
        buffView.startAnimation(animation)
    }

    /** Generate the answers layout by generating views from #inflateAnswerRowView() and then
     * adding them to pre-defined answersLayout. Number of views generated is ensured to be
     * equal to the size of answers list
     * @param answers List of StreamDetails.Answer model obtained from Details API
     * @param buffAnswerListener Callback implemented by the host activity to monitor answer
     *                           selection event
     * */
    private fun setAnswersView(
        answers: List<StreamDetails.Answer>,
        buffAnswerListener: OnBuffAnswerListener
    ) {
        val rectifiedAnswersList = rectifyPossibleInvalidAnswersListSize(answers)
        // Set answer count
        val answersCountTextView =
            buffView.findViewById<TextView>(R.id.answers_count_text)
        answersCountTextView.text = rectifiedAnswersList.size.toString()
        answersCountTextView.setTextColor(answersCounterTextColor)
        answersCountTextView.background = answersCounterTextBackground

        // Add answer rows to container
        val answersLayout =
            buffView.findViewById<LinearLayout>(R.id.answers_layout)
        for (a in rectifiedAnswersList) {
            val rowAnswer = inflateAnswerRowView(answersLayout)

            // Set Alphabetical index
            val indexTextView = rowAnswer.findViewById<TextView>(R.id.index_text)
            indexTextView.text = StringUtils.asciiToAlphabetBuffAnswerIndexes(listAlphabeticalIndex)
            indexTextView.setTextColor(answerRowIndexTextColor)
            indexTextView.background = answerRowIndexBackground

            // Set answer title
            val answerTextView =
                rowAnswer.findViewById<TextView>(R.id.answer_text)
            answerTextView.text = a.title
            answerTextView.setTextColor(answerRowTextColor)

            // Set answer click listener
            RxView.clicks(rowAnswer)
                .subscribe {
                    rowAnswer.background = ContextCompat.getDrawable(
                        buffView.context,
                        R.drawable.questionnaire_selected_answers_layout_bg
                    )

                    stopTimer()
                    disableBuffView()

                    // Close questionnaire after 2 seconds
                    Handler().postDelayed({
                        closeQuestionnaire()
                    }, POST_ANSWER_BUFF_DISMISS_DURATION)

                    // Fire callback event implemented in the host activity
                    buffAnswerListener.onBuffAnswerSelected(a)
                }

            // Add row to the layout
            answersLayout.addView(rowAnswer)
            val animation = loadAnimation(buffView.context, R.anim.slide_up)
            rowAnswer.startAnimation(animation)

            // Increment #listAlphabeticalIndex to generate next alphabet index
            listAlphabeticalIndex = listAlphabeticalIndex.inc()
        }
    }

    /** Stop the countdown timer */
    private fun stopTimer() {
        timerView.stop()
        timerView.animate()
            .setDuration(1000)
            .alpha(0.0f)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    timerView.visibility = View.GONE
                }
            })
    }

    /** Inflate answer row and apply layout params set in #getAnswerRowLayoutParams()
     * @return View Answer View
     * */
    private fun inflateAnswerRowView(answersLayout: LinearLayout): View {
        val view = LayoutInflater.from(answersLayout.context)
            .inflate(R.layout.row_questionnaire_answer, answersLayout, false)
        view.layoutParams = getAnswerRowLayoutParams()
        view.background = answerRowBackground
        return view
    }

    /** LayoutParams for the dynamically generated answer view row generated by
     *  #inflateAnswerRowView()
     * @return LayoutParams for row view
     * */
    private fun getAnswerRowLayoutParams(): LayoutParams {
        // Check if kotlin's lateinit is initialized. If not, then initialize it
        if (!::answerRowLayoutParams.isInitialized) {
            answerRowLayoutParams = LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
            )
            answerRowLayoutParams.bottomMargin =
                resources.getDimension(R.dimen.answerRowMarginBottom).toInt()
        }
        return answerRowLayoutParams
    }

    /* RECTIFICATIONS START */
    /** Kotlin Extension function to disable #buffView once call for #closeQuestionnaire() is made */
    private fun View.disableBuffView() {
        isEnabled = false
        if (this is ViewGroup) children.forEach { child -> child.disableBuffView() }
    }

    /** Rectification for the wrong user input for #countdownTimerAlertDuration. Since
     * #countdownTimerAlertDuration must not be greater than the interval of showing buffView.
     * So if it's greater, then reset it to the value of timeToShow
     * @param timeToShow actual timeToShow
     * */
    private fun rectifyPossibleCountdownTimerAlertSeconds(timeToShow: Int) {
        if (countdownTimerAlertDuration > timeToShow)
            countdownTimerAlertDuration = timeToShow
    }

    /** Rectification for the wrong user input for #countdownTimerAlertViewZoomedScale. Since
     * #countdownTimerAlertViewZoomedScale must not be lesser than 1.0f.
     * So if it's lesser, then reset it to 1.0f
     * @param timeToShow actual timeToShow
     * */
    private fun rectifyPossibleCountdownTimerAlertViewZoomedScale() {
        if (countdownTimerAlertViewZoomedScale < 1.0f)
            countdownTimerAlertViewZoomedScale = 1.0f
    }

    /** Since as per Buff's business logic, answers cannot be more than 5. For unexpected inputs,
     * checking if the size is greater than 5, then return the list with initial 5 elements only
     * (Business logic may change here in production library)
     * */
    private fun rectifyPossibleInvalidAnswersListSize(answers: List<StreamDetails.Answer>)
            : List<StreamDetails.Answer> {
        if (answers.size <= ANSWERS_LIST_MAX_SIZE)
            return answers
        return answers.subList(0, ANSWERS_LIST_MAX_SIZE)
    }
    /* RECTIFICATIONS END */
    /* VIEWS SETUP ENDS */

    /* BUILDER PATTERN FOR CUSTOM ATTRIBUTES STARTS */
    fun setAuthorInfoBackground(value: Drawable): BuffView {
        authorInfoBackground = value
        return this
    }

    fun setAnswersCounterTextBackground(value: Drawable): BuffView {
        answersCounterTextBackground = value
        return this
    }

    fun setQuestionBackground(value: Drawable): BuffView {
        questionBackground = value
        return this
    }

    fun setAnswerRowBackground(value: Drawable): BuffView {
        answerRowBackground = value
        return this
    }

    fun setAnswerRowIndexBackground(value: Drawable): BuffView {
        answerRowIndexBackground = value
        return this
    }

    fun setAuthorInfoTextColor(@ColorRes value: Int): BuffView {
        authorInfoTextColor = ContextCompat.getColor(context, value)
        return this
    }

    fun setAnswersCounterTextColor(@ColorRes value: Int): BuffView {
        answersCounterTextColor = value
        return this
    }

    fun setAnswerRowTextColor(@ColorRes value: Int): BuffView {
        answerRowTextColor = value
        return this
    }

    fun setQuestionTextColor(@ColorRes value: Int): BuffView {
        questionTextColor = value
        return this
    }

    fun setAnswerRowIndexTextColor(@ColorRes value: Int): BuffView {
        answerRowIndexTextColor = value
        return this
    }

    fun setCountdownTimerAlertSeconds(seconds: Int): BuffView {
        countdownTimerAlertDuration = seconds
        return this
    }

    fun setCountdownTimerAlertViewZoomedScale(scale: Float): BuffView {
        countdownTimerAlertViewZoomedScale = scale
        return this
    }
    /* BUILDER PATTERN FOR CUSTOM ATTRIBUTES ENDS */

    /** Every object of the buff must be present to generate valid buffView it. */
    @Throws(BuffControlMissingException::class)
    private fun validateStreamDetails(streamDetails: StreamDetails): Boolean {
        if (streamDetails.author == null) {
            throw BuffControlMissingException(Throwable(context.getString(R.string.invalid_author_data)))
        }
        if (streamDetails.question == null) {
            throw BuffControlMissingException(Throwable(context.getString(R.string.invalid_question_data)))
        }
        if (streamDetails.answers == null || streamDetails.answers.isEmpty()) {
            throw BuffControlMissingException(Throwable(context.getString(R.string.invalid_answers_data)))
        }

        return true
    }

    companion object {
        private const val POST_ANSWER_BUFF_DISMISS_DURATION = 2000L
        private const val ANSWERS_LIST_MAX_SIZE = 5
        private const val COUNTDOWN_ALERT_VIEW_SCALE_DURATION = 500L // In milliseconds
        private const val DEFAULT_TEXT_COLOR_LIGHT = android.R.color.white
        private const val DEFAULT_COUNTDOWN_TIMER_ALERT_DURATION = 3
        private const val DEFAULT_COUNTDOWN_TIMER_ALERT_VIEW_ZOOMED_SCALE = 1.15f
        private val defaultDarkTextColor = R.color.textDark
    }
}